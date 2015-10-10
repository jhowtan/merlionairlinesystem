package MAS.Bean;

import MAS.Common.Constants;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless(name = "CostsEJB")
@LocalBean
public class CostsBean {
    @PersistenceContext
    EntityManager em;

    public CostsBean() {
    }

    public long createCost(int type, double amount, String comments, long assocId) throws NotFoundException {
        Cost cost = new Cost();
        cost.setType(type);
        cost.setAmount(amount);
        cost.setComments(comments);
        if (type == Constants.COST_PER_AIRCRAFT || type == Constants.COST_PER_MAINTENANCE) {
            Aircraft aircraft = em.find(Aircraft.class, assocId);
            if (aircraft == null) throw new NotFoundException();
            cost.setAssocId(aircraft.getId());
        }
        else if (type == Constants.COST_PER_FLIGHT) {
            AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, assocId);
            if (aircraftAssignment == null) throw new NotFoundException();
            cost.setAssocId(aircraftAssignment.getId());
        }
        else if (type == Constants.COST_SALARY) {
            User user = em.find(User.class, assocId);
            if (user == null) throw new NotFoundException();
            cost.setAssocId(user.getId());
        }
        cost.setDate(new Date());
        em.persist(cost);
        em.flush();

        return cost.getId();
    }

    public void removeCost(long id) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        em.remove(cost);
    }

    public void changeCostComments(long id, String comments) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        cost.setComments(comments);
        em.persist(cost);
    }

    public void changeCostAmount(long id, double amount) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        cost.setAmount(amount);
        em.persist(cost);
    }

    public Cost getCost(long id) throws NotFoundException {
        Cost cost = em.find(Cost.class, id);
        if (cost == null) throw new NotFoundException();
        return cost;
    }

    public List<Cost> getAllCostOfType(int type) {
        return em.createQuery("SELECT c from Cost c WHERE c.type = :type").setParameter("type", type).getResultList();
    }

    public double calculateCostPerAircraft(long aircraftId) throws NotFoundException {
        return 0;
    }
}
