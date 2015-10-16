package MAS.Bean;

import MAS.Entity.CustomerLog;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless(name = "CustomerLogEJB")
@LocalBean
public class CustomerLogBean {
    @PersistenceContext
    private EntityManager em;

    public CustomerLogBean() {
    }

    public long createCustomerLog(long customerId, String description, String category) throws NotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer == null) throw new NotFoundException();

        CustomerLog customerLog = new CustomerLog();
        customerLog.setCustomer(customer);
        customerLog.setDescription(description);
        customerLog.setCategory(category);
        customerLog.setTimestamp(new Date());

        em.persist(customerLog);
        em.flush();

        return customerLog.getId();
    }

    public List<CustomerLog> getAllCustomerLogs() {
        return em.createQuery("SELECT c FROM CustomerLog c", CustomerLog.class).getResultList();
    }

    public List<CustomerLog> getCustomerLogForCustomer(long customerId) throws NotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer == null) throw new NotFoundException();
        return em.createQuery("SELECT cl FROM CustomerLog cl WHERE cl.customer = :customer", CustomerLog.class).setParameter("customer", customer).getResultList();
    }

}
