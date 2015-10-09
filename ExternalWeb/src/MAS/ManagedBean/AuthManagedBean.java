package MAS.ManagedBean;

import MAS.Bean.CustomerBean;
import MAS.Common.Constants;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

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

    public Customer retrieveCustomer() {
        if (!this.authenticated) {
            return null;
        }
        try {
            return customerBean.getCustomer(this.customerId);
        } catch (NotFoundException e) {
            return null;
        }
    }

    public void forwardToLogin() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.WEB_ROOT_EXT + "login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerDisplayName() {
        return customerDisplayName;
    }

    public void setCustomerDisplayName(String customerDisplayName) {
        this.customerDisplayName = customerDisplayName;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
