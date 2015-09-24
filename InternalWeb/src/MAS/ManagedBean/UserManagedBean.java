package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class UserManagedBean {
    @EJB
    private UserBean userBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<User> getAllUsers() {
        return userBean.getAllUsers();
    }

    public void setLocked(long id, boolean isLocked) {
        try {
            userBean.setLocked(id, isLocked);
            if(isLocked) {
                authManagedBean.createAuditLog("Locked user: " + userBean.getUser(id).getUsername(), "lock_user");
            } else {
                authManagedBean.createAuditLog("Unlocked user: " + userBean.getUser(id).getUsername(), "unlock_user");
            }
        } catch (NotFoundException e) {
        }
    }

    public void delete(long id) {
        try {
            String username = userBean.getUser(id).getUsername();
            userBean.removeUser(id);
            authManagedBean.createAuditLog("Deleted user: " + username, "delete_user");
            FacesMessage m = new FacesMessage("Successfully deleted user: " + username);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Unable to delete this user.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
