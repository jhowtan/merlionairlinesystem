package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Stateless(name = "FFPEJB")
@LocalBean
public class FFPBean {
    @PersistenceContext
    private EntityManager em;

    public FFPBean() {
    }

    public Customer creditMiles(long customerId, int miles) throws NotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer == null) throw new NotFoundException();
        customer.setMiles(customer.getMiles() + miles);
        em.persist(customer);
        return customer;
    }

    public Customer creditEliteMiles(long customerId, int eliteMiles) throws NotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer == null) throw new NotFoundException();
        if (customer.getQualificationEndDate() == null) {
            customer.setQualificationEndDate(Utils.oneYearLater());
        }
        customer.setEliteMiles(customer.getEliteMiles() + eliteMiles);
        if (customer.getTier() == Constants.FFP_TIER_BLUE && customer.getEliteMiles() >= Constants.FFP_TIER_SILVER_REQUIREMENT) {
            customer.setQualificationEndDate(Utils.oneYearLater());
            customer.setEliteMiles(0);
            customer.setTier(Constants.FFP_TIER_SILVER);
            customer.setStatusExpiry(Utils.oneYearLater());
        } else if (customer.getTier() == Constants.FFP_TIER_SILVER && customer.getEliteMiles() >= Constants.FFP_TIER_GOLD_REQUIREMENT) {
            customer.setQualificationEndDate(Utils.oneYearLater());
            customer.setEliteMiles(0);
            customer.setTier(Constants.FFP_TIER_GOLD);
            customer.setStatusExpiry(Utils.oneYearLater());
        }
        return customer;
    }

    public Customer redeemMiles(long customerId, int miles) throws NotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        if (customer == null) throw new NotFoundException();
        customer.setMiles(Math.max(0, customer.getMiles() - miles));
        em.persist(customer);
        return customer;
    }

    public void periodicUpdateFFP() {
        Date dayStart = Utils.addTimeToDate(new Date(), "00:00");
        Date dayEnd = Utils.minutesLater(dayStart, 1440);
        List<Customer> customers = em.createQuery("SELECT c FROM Customer c WHERE c.qualificationEndDate < :dayEnd AND c.tier = :tier", Customer.class)
                .setParameter("dayEnd", dayEnd, TemporalType.TIMESTAMP)
                .setParameter("tier", Constants.FFP_TIER_BLUE)
                .getResultList();
        for (Customer customer : customers) {
            customer.setEliteMiles(0);
            customer.setQualificationEndDate(null);
            em.persist(customer);
        }
        customers = em.createQuery("SELECT c FROM Customer c WHERE c.statusExpiry < :dayEnd", Customer.class)
                .setParameter("dayEnd", dayEnd, TemporalType.TIMESTAMP)
                .getResultList();
        for (Customer customer : customers) {
            if (customer.getTier() == Constants.FFP_TIER_GOLD && customer.getEliteMiles() >= Constants.FFP_TIER_GOLD_REQUIREMENT) {
                customer.setQualificationEndDate(Utils.oneYearLater());
                customer.setStatusExpiry(Utils.oneYearLater());
                customer.setEliteMiles(0);
                em.persist(customer);
            } else if (customer.getTier() == Constants.FFP_TIER_GOLD && customer.getEliteMiles() < Constants.FFP_TIER_GOLD_REQUIREMENT) {
                customer.setQualificationEndDate(Utils.oneYearLater());
                customer.setStatusExpiry(Utils.oneYearLater());
                customer.setEliteMiles(0);
                customer.setTier(Constants.FFP_TIER_SILVER);
                em.persist(customer);
            } else if (customer.getTier() == Constants.FFP_TIER_SILVER && customer.getEliteMiles() >= Constants.FFP_TIER_SILVER_REQUIREMENT) {
                customer.setQualificationEndDate(Utils.oneYearLater());
                customer.setStatusExpiry(Utils.oneYearLater());
                customer.setEliteMiles(0);
                em.persist(customer);
            } else if (customer.getTier() == Constants.FFP_TIER_SILVER && customer.getEliteMiles() < Constants.FFP_TIER_SILVER_REQUIREMENT) {
                customer.setQualificationEndDate(null);
                customer.setStatusExpiry(null);
                customer.setEliteMiles(0);
                customer.setTier(Constants.FFP_TIER_BLUE);
                em.persist(customer);
            }
        }
    }
}
