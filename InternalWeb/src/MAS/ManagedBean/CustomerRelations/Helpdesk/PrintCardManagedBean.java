package MAS.ManagedBean.CustomerRelations.Helpdesk;

import MAS.Bean.CustomerBean;
import MAS.Common.Constants;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.CustomerRelations.HelpdeskManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
public class PrintCardManagedBean implements Serializable {
    @EJB
    CustomerBean customerBean;

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
