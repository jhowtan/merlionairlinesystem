package MAS.ManagedBean;

import MAS.Bean.CustomerBean;
import MAS.Common.Constants;
import MAS.Entity.Customer;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class AuthManagedBean {
    @EJB
    private CustomerBean customerBean;

    private long customerId;
    private String customerDisplayName;
    private boolean authenticated = false;

    public boolean login(long customerId, String password) {
        try {
            Customer customer = customerBean.login(customerId, password);
            this.customerId = customer.getId();
            this.customerDisplayName = customer.getDisplayName();
            this.authenticated = true;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        this.customerId = -1;
        this.customerDisplayName = "";
        this.authenticated = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        forwardToLogin();
    }

    public void checkPermission() {
        if (!this.authenticated) {
            forwardToLogin();
        }
    }

    public void forwardToLogin() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.WEB_ROOT + "/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
