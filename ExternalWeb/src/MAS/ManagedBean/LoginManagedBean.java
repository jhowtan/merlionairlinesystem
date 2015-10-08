package MAS.ManagedBean;

import MAS.Common.Constants;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

@ManagedBean
public class LoginManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String customerIdString;
    private String password;
    private boolean invalidLogin = false;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.get("invalid") != null) {
            this.invalidLogin = true;
        }
    }

    public void login() {
        try {
            boolean successfulLogin = authManagedBean.login(Long.parseLong(customerIdString), password);
            if (successfulLogin) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.WEB_ROOT_EXT + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                invalidLogin = true;
            }
        } catch (Exception e) {
            invalidLogin = true;
        }
    }

    public void loginNavBar() {
        try {
            boolean successfulLogin = authManagedBean.login(Long.parseLong(customerIdString), password);
            if (!successfulLogin) {
                invalidLogin = true;
            }
        } catch (Exception e) {
            invalidLogin = true;
        }
        if (invalidLogin) {
            // Redirect to login page with flag
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.WEB_ROOT_EXT + "login.xhtml?invalid=1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getCustomerIdString() {
        return customerIdString;
    }

    public void setCustomerIdString(String customerIdString) {
        this.customerIdString = customerIdString;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isInvalidLogin() {
        return invalidLogin;
    }

    public void setInvalidLogin(boolean invalidLogin) {
        this.invalidLogin = invalidLogin;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
