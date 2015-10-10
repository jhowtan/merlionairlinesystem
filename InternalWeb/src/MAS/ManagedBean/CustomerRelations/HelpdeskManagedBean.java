package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CustomerBean;
import MAS.Entity.Customer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class HelpdeskManagedBean implements Serializable {
    @EJB
    CustomerBean customerBean;

    private Customer customer;
    private Long customerId;
    private boolean customerVerified = false;
    private boolean animateComponents = false;

    @PostConstruct
    public void init() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            customerId = Long.parseLong(params.get("customerId"));
            customer = customerBean.getCustomer(customerId);
            customerVerified = true;
            if (params.get("animate") != null) {
                animateComponents = true;
            }
        } catch (Exception e) {
            // Cannot find customer!
        }
    }

    public String animateClass() {
        if (animateComponents) {
            return "animated";
        }
        return "";
    }

    public void customerIdAjaxListener(AjaxBehaviorEvent event) {
        HtmlInputText input = (HtmlInputText) event.getSource();
        String inputValue = (String) input.getValue();
        if (inputValue.length() != 8) {
            return;
        }
        try {
            customerId = Long.parseLong(inputValue);
            customer = customerBean.getCustomer(customerId);
        } catch (Exception e) {
            // Cannot find customer!
        }
    }

    public String timeOfDayGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            return "morning";
        } else if (hour < 18) {
            return "afternoon";
        } else {
            return "evening";
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public boolean isCustomerVerified() {
        return customerVerified;
    }

    public void setCustomerVerified(boolean customerVerified) {
        this.customerVerified = customerVerified;
    }

    public boolean isAnimateComponents() {
        return animateComponents;
    }

    public void setAnimateComponents(boolean animateComponents) {
        this.animateComponents = animateComponents;
    }
}
