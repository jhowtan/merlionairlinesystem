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
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ViewCrewCertificationManagedBean implements Serializable {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    @EJB
    CrewCertificationBean crewCertificationBean;
    @EJB
    UserBean userBean;

    private Certification certification;

    @PostConstruct
    public void init() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            long id = Long.parseLong(params.get("id"));
            certification = crewCertificationBean.getCrewCertification(id);
            if (certification.getOwner().getId() != authManagedBean.getUserId() && !authManagedBean.hasPermission(Permissions.MANAGE_CREW_CERTIFICATION)) {
                throw new NotFoundException();
            }
        } catch (Exception e) {e.printStackTrace();
            certification = null;
        }
    }

    public void approve() {
        try {
            certification = crewCertificationBean.updateCrewCertificationApprovalStatus(certification.getId(), 1, authManagedBean.getUserId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void reject() {
        try {
            certification = crewCertificationBean.updateCrewCertificationApprovalStatus(certification.getId(), 2, authManagedBean.getUserId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
