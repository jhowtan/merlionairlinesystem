package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Entity.Permission;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class CreateRoleManagedBean {
    @EJB
    private RoleBean roleBean;

    private String roleName;
    private Long[] permissions;

    public List<Permission> getAllPermissions() {
        return roleBean.getAllPermissions();
    }

    public void createRole() {
        // Create Role Logic
        FacesMessage m = new FacesMessage("User created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
