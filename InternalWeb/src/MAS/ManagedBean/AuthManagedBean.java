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

    private User user;
    private boolean authenticated = false;

    public boolean login(String username, String password) {
        try {
            user = userBean.login(username, password);
            authenticated = true;
            return true;
        } catch (InvalidLoginException e) {
            return false;
        }
    }

    public void logout() {
        user = null;
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
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getName().equals(permissionString)) {
                    return true;
                }
            }
        }
        return false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
