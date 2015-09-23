package MAS.Bean;

import MAS.Entity.FareRule;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "FareRuleEJB")
@LocalBean
public class FareRuleBean {
    @PersistenceContext
    private EntityManager em;

    public FareRuleBean() {
    }

    public FareRule getFareRule(long id) throws NotFoundException {
        FareRule fareRule = em.find(FareRule.class, id);
        if (fareRule == null) throw new NotFoundException();
        return fareRule;
    }

    public void removeFareRule(long id) throws NotFoundException {
        FareRule fareRule = em.find(FareRule.class, id);
        if (fareRule == null) throw new NotFoundException();
        em.remove(fareRule);
    }

    public long createFareRule(String name, int minimumStay, int maximumStay, int advancePurchase, int minimumPassengers, int milesAccrual, boolean freeCancellation) {
        FareRule fareRule = new FareRule();
        fareRule.setName(name);
        fareRule.setMinimumStay(minimumStay);
        fareRule.setMaximumStay(maximumStay);
        fareRule.setAdvancePurchase(advancePurchase);
        fareRule.setMinimumPassengers(minimumPassengers);
        fareRule.setMilesAccrual(milesAccrual);
        fareRule.setFreeCancellation(freeCancellation);
        em.persist(fareRule);
        em.flush();
        return fareRule.getId();
    }

    public List<FareRule> getAllFareRules() {
        return em.createQuery("SELECT f FROM FareRule f", FareRule.class).getResultList();
    }

}
