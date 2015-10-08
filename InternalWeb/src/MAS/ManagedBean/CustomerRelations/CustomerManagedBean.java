package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CustomerBean;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean
public class CustomerManagedBean implements Serializable {
    @EJB
    CustomerBean customerBean;

    private Customer customer;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            long id = Long.parseLong(params.get("id"));
            customer = customerBean.getCustomer(id);
        } catch (Exception e) {
            customer = null;
        }
    }

    public List<Customer> getCustomers() {
        return customerBean.getAllCustomers();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
