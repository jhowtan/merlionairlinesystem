package MAS.ManagedBean;

import MAS.Common.Constants;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean
public class LoginManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String username;
    private String password;
    private boolean invalidLogin = false;

    public void login() {
        if (authManagedBean.login(username, password)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.WEB_ROOT + "/App");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            invalidLogin = true;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public boolean isInvalidLogin() {
        return invalidLogin;
    }

    public void setInvalidLogin(boolean invalidLogin) {
        this.invalidLogin = invalidLogin;
    }
}
