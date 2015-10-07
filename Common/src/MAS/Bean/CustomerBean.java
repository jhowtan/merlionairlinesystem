package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless(name = "CustomerEJB")
@LocalBean
public class CustomerBean {
    @PersistenceContext
    private EntityManager em;

    public CustomerBean() {
    }

    public Customer createCustomer(Customer customer, String password) {
        customer.setSalt(Utils.generateSalt());
        customer.setPasswordHash(Utils.hash(password, customer.getSalt()));
        customer.setLocked(false);

        em.persist(customer);
        em.flush();

        return customer;
    }

    public Customer getCustomer(long id) throws NotFoundException {
        Customer customer = em.find(Customer.class, id);
        if (customer == null) throw new NotFoundException();
        return customer;
    }

    public Customer getCustomerByMembershipNumber(long membershipNumber) throws NotFoundException {
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.membershipNumber = :membershipNumber", Customer.class).setParameter("membershipNumber", membershipNumber).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
    }

}
