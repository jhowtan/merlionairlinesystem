package MAS.ManagedBean;

import MAS.Bean.WorkgroupBean;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class WorkgroupManagedBean {
    @EJB
    private WorkgroupBean workgroupBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Workgroup> getAllOwnedWorkgroups() throws NotFoundException {
        return workgroupBean.getOwnedWorkgroups(authManagedBean.getUserId());
    }

    public void delete(long id) {
        try {
            String workgroupName = workgroupBean.getWorkgroup(id).getName();
            workgroupBean.removeWorkgroup(id);
            authManagedBean.createAuditLog("Deleted workgroup: " + workgroupName, "delete_workgroup");
            FacesMessage m = new FacesMessage("Successfully deleted workgroup: " + workgroupName);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            FacesMessage m = new FacesMessage("Unable to delete workgroup, please check" +
                    " if there are existing users in the workgroup");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The workgroup cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }


    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

}
