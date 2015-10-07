package MAS.Bean;

import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "CostsEJB")
@LocalBean
public class CostsBean {
    @PersistenceContext
    EntityManager em;

    public CostsBean() {
    }

    public long createAircraftSingleCost(double amount, String comments, long aircraftId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (aircraft == null) throw new NotFoundException();
        CostAircraftSingle costAircraftSingle = new CostAircraftSingle();
        costAircraftSingle.setAmount(amount);
        costAircraftSingle.setAircraft(aircraft);
        costAircraftSingle.setComments(comments);
        em.persist(costAircraftSingle);
        em.flush();

        return costAircraftSingle.getId();
    }

    public long createCostPerFlight(double amount, String comments, long aaId) throws NotFoundException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, aaId);
        if (aircraftAssignment == null) throw new NotFoundException();
        CostAAOccurrence costAAOccurrence = new CostAAOccurrence();
        costAAOccurrence.setAmount(amount);
        costAAOccurrence.setComments(comments);
        costAAOccurrence.setAircraftAssignment(aircraftAssignment);
        em.persist(costAAOccurrence);
        em.flush();

        return aircraftAssignment.getId();
    }

    public long createCostPerMaint(double amount, String comments, long aircraftId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, aircraftId);
        if (aircraft == null) throw new NotFoundException();
        CostMaintenanceOccurrence costMaintenanceOccurrence = new CostMaintenanceOccurrence();
        costMaintenanceOccurrence.setAmount(amount);
        costMaintenanceOccurrence.setComments(comments);
        costMaintenanceOccurrence.setAircraft(aircraft);
        em.persist(costMaintenanceOccurrence);
        em.flush();

        return costMaintenanceOccurrence.getId();
    }
}
