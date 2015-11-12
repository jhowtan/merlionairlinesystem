package MAS.ManagedBean.SystemAdmin;

import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

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
            e.getMessage();
            FacesMessage m = new FacesMessage("The user cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
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
            e.getMessage();
            FacesMessage m = new FacesMessage("The user cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public String displayJob(User user) {
        try {
            return Constants.JOB_NAMES[user.getJob()];
        } catch (Exception e) {
            return "Untitled";
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
