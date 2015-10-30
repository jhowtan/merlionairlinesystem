package MAS.ManagedBean.CrewOperations;

import MAS.Bean.CrewCertificationBean;
import MAS.Bean.UserBean;
import MAS.Common.Permissions;
import MAS.Entity.Certification;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@ViewScoped
public class DownloadCrewCertificationManagedBean implements Serializable {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    @EJB
    CrewCertificationBean crewCertificationBean;
    @EJB
    UserBean userBean;

    public void download() throws Exception {
        if(!authManagedBean.isAuthenticated()) {
            throw new Exception();
        }

        Certification certification;
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long id = Long.parseLong(params.get("id"));
        certification = crewCertificationBean.getCrewCertification(id);
        if (certification.getOwner().getId() != authManagedBean.getUserId() && !authManagedBean.hasPermission(Permissions.MANAGE_CREW_CERTIFICATION)) {
            throw new NotFoundException();
        }

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        ec.responseReset();
        ec.setResponseContentType("application/pdf");

        OutputStream outputStream = ec.getResponseOutputStream();
        FileInputStream inputStream = new FileInputStream("uploads/" + certification.getDocument());
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

        FacesContext.getCurrentInstance().responseComplete();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
