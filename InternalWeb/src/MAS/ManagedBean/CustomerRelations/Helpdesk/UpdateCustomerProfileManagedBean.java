package MAS.ManagedBean.CustomerRelations.Helpdesk;

import MAS.Bean.CustomerBean;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.CustomerRelations.HelpdeskManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

@ManagedBean
public class UpdateCustomerProfileManagedBean implements Serializable {
    @EJB
    CustomerBean customerBean;

    @ManagedProperty(value="#{helpdeskManagedBean}")
    private HelpdeskManagedBean helpdeskManagedBean;

    private Customer customer;

    @PostConstruct
    public void init() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            customer = customerBean.getCustomer(Long.parseLong(params.get("customerId")));
        } catch (Exception e) {
            // Cannot find customer!
        }
    }

    public void save() {
        try {
            customerBean.updateCustomer(customer);

            // Update Customer in HelpdeskManagedBean to reflect the new changes
            helpdeskManagedBean.setCustomer(customer);

            FacesMessage m = new FacesMessage("Customer profile updated successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);

        } catch (NotFoundException e) {
            // Cannot find customer!
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setHelpdeskManagedBean(HelpdeskManagedBean helpdeskManagedBean) {
        this.helpdeskManagedBean = helpdeskManagedBean;
    }
}
