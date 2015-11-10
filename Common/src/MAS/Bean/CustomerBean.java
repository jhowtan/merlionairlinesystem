package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.CustomerAnalysis.AnalysedCustomer;
import MAS.Entity.Customer;
import MAS.Entity.ETicket;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        customer.setJoinDate(new Date());
        customer.setTier(Constants.FFP_TIER_BLUE);
        customer.setMiles(0);
        customer.setEliteMiles(0);
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

    public void updateCustomer(Customer customer) throws NotFoundException {
        if (customer.getId() == null || em.find(Customer.class, customer.getId()) == null) throw new NotFoundException();
        em.merge(customer);
    }

    public List<Customer> getAllCustomers() {
        return em.createQuery("SELECT c from Customer c", Customer.class).getResultList();
    }

    public boolean isEmailUnique(String email) {
        return (Long) em.createQuery("SELECT COUNT(c) FROM Customer c WHERE c.email = :email").setParameter("email", email.toLowerCase()).getSingleResult() == 0;
    }

    public Customer login(long customerId, String password) throws InvalidLoginException {
        try {
            Customer customer = em.find(Customer.class, customerId);
            if (!Utils.hash(password, customer.getSalt()).equals(customer.getPasswordHash().toString()))
                throw new InvalidLoginException();
            return customer;
        } catch (Exception e) {
            throw new InvalidLoginException();
        }
    }

    public List<AnalysedCustomer> analyseCustomer() {
        List<AnalysedCustomer> result = new ArrayList<>();
        List<Customer> customers = getAllCustomers();
        Date startDate = Utils.getStartOfMonth(-12, 0);
        Date endDate = new Date();
        for (Customer customer : customers) {
            List<ETicket> eTickets = em.createQuery("SELECT e from ETicket e WHERE e.ffpNumber = :ffp AND e.gateChecked = true AND e.flight.actualDepartureTime > :startDate AND " +
                    "e.flight.actualDepartureTime < :endDate", ETicket.class)
                    .setParameter("ffp", "MA/".concat(customer.getId().toString()))
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
            if (eTickets.size() >= 2) {
                AnalysedCustomer anaCustomer = new AnalysedCustomer();
                anaCustomer.customer = customer;
                anaCustomer.flightCount = eTickets.size();
                anaCustomer.revenuePerMile = 0;
                for (ETicket eTicket : eTickets) {
                    anaCustomer.revenuePerMile += Constants.TRAVEL_CLASS_PRICE_MULTIPLIER[eTicket.getBookingClass().getTravelClass()] *
                                                    eTicket.getBookingClass().getFareRule().getPriceMul();
                }
                anaCustomer.analyseSegment();
                result.add(anaCustomer);
            } else
                continue;
        }
        return result;
    }

}
