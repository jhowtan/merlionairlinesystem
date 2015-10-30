package MAS.ManagedBean.CrewOperations;

import MAS.Bean.CrewCertificationBean;
import MAS.Bean.FleetBean;
import MAS.Bean.UserBean;
import MAS.Common.Utils;
import MAS.Entity.AircraftType;
import MAS.Entity.Certification;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ManagedBean
@ViewScoped
public class CrewCertificationManagedBean implements Serializable {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    @EJB
    CrewCertificationBean crewCertificationBean;
    @EJB
    FleetBean fleetBean;
    @EJB
    UserBean userBean;

    private int certificationType = 0;
    private Date certificationExpiry = Utils.oneYearLater();
    private String certificationOthers;
    private String certificationLanguage;
    private Long certificationFlight;
    private Part certificationFile;


    public List<Certification> retrieveCrewCertifications() {
        try {
            return crewCertificationBean.getCrewCertifications(authManagedBean.getUserId());
        } catch (NotFoundException e) {
            return null;
        }
    }

    public List<AircraftType> retrieveAircraftTypes() {
        return fleetBean.getAllAircraftTypes();
    }

    public String getStatusString(int status) {
        switch (status) {
            case 0:
                return "Pending Approval";
            case 1:
                return "Approved";
            case 2:
                return "Rejected";
        }
        return "";
    }

    public String getCertificationString(Certification certification) {
        switch (certification.getType()) {
            case 0:
                return "Aircraft Certification: " + certification.getAircraftType().getName();
            case 1:
                return "Language Proficiency: " + certification.getLanguage();
            case 2:
                return certification.getOthers();
        }
        return "";
    }

    public void createCrewCertification() {

        try {
            String filename = UUID.randomUUID().toString() + ".pdf";
            InputStream inputStream = certificationFile.getInputStream();
            new File("uploads").mkdir();
            FileOutputStream outputStream = new FileOutputStream("uploads/" + filename);
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
            outputStream.close();
            inputStream.close();

            Certification certification = new Certification();
            certification.setSubmittedDate(new Date());
            certification.setExpiry(certificationExpiry);
            certification.setApprovalStatus(0);
            certification.setDocument(filename);
            certification.setOwner(userBean.getUser(authManagedBean.getUserId()));
            certification.setType(certificationType);
            switch (certificationType) {
                case 0:
                    certification.setAircraftType(fleetBean.getAircraftType(certificationFlight));
                    break;
                case 1:
                    certification.setLanguage(certificationLanguage);
                    break;
                case 2:
                    certification.setOthers(certificationOthers);
                    break;
            }

            certification = crewCertificationBean.createCrewCertification(certification);

            FacesMessage m = new FacesMessage("Certification created and is pending approval.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);

            certificationType = 0;
            certificationExpiry = Utils.oneYearLater();
            certificationOthers = null;
            certificationLanguage = null;
            certificationFlight = null;
            certificationFile = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public int getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(int certificationType) {
        this.certificationType = certificationType;
    }

    public Date getCertificationExpiry() {
        return certificationExpiry;
    }

    public void setCertificationExpiry(Date certificationExpiry) {
        this.certificationExpiry = certificationExpiry;
    }

    public String getCertificationOthers() {
        return certificationOthers;
    }

    public void setCertificationOthers(String certificationOthers) {
        this.certificationOthers = certificationOthers;
    }

    public String getCertificationLanguage() {
        return certificationLanguage;
    }

    public void setCertificationLanguage(String certificationLanguage) {
        this.certificationLanguage = certificationLanguage;
    }

    public Long getCertificationFlight() {
        return certificationFlight;
    }

    public void setCertificationFlight(Long certificationFlight) {
        this.certificationFlight = certificationFlight;
    }

    public Part getCertificationFile() {
        return certificationFile;
    }

    public void setCertificationFile(Part certificationFile) {
        this.certificationFile = certificationFile;
    }
}
