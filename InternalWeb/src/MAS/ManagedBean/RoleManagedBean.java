package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Entity.Role;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class RoleManagedBean {
    @EJB
    private RoleBean roleBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Role> getAllRoles() {
        return roleBean.getAllRoles();
    }

    public void delete(long id) {
        try {
            String roleName = roleBean.getRole(id).getName();
            roleBean.removeRole(id);
            authManagedBean.createAuditLog("Deleted role: " + roleName, "delete_role");
            FacesMessage m = new FacesMessage("Successfully deleted role: " + roleName);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Unable to delete role, please check if there are any " +
                    "existing permissions created for this role.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
