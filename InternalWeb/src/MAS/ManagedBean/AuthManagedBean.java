package MAS.ManagedBean;

import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Entity.Permission;
import MAS.Entity.Role;
import MAS.Entity.User;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

@ManagedBean
@SessionScoped
public class AuthManagedBean {
    @EJB
    private UserBean userBean;

    private long userId;
    private boolean authenticated = false;

    public boolean login(String username, String password) {
        try {
            userId = userBean.login(username, password);
            authenticated = true;
            return true;
        } catch (InvalidLoginException e) {
            return false;
        }
    }

    public void logout() {
        userId = -1;
        authenticated = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        forwardToLogin();
    }

    public void requiresAuth() {
        if (!authenticated) {
            forwardToLogin();
        }
    }

    public void forwardToLogin() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.WEB_ROOT + "/Auth");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPermission(String permissionString) {
        if (!authenticated) return false;
        try {
            for (Role role : userBean.getUser(userId).getRoles()) {
                for (Permission permission : role.getPermissions()) {
                    if (permission.getName().equals(permissionString)) {
                        return true;
                    }
                }
            }
        } catch (NotFoundException e) {
            return false;
        }
        return false;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
