package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Entity.Role;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
        } catch (NotFoundException e) {
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
