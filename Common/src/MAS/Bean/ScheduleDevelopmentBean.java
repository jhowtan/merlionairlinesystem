package MAS.Bean;

import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "ScheduleDevelopmentEJB")
@LocalBean
public class ScheduleDevelopmentBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    CostsBean costsBean;

    private List<Aircraft> aircraftsToFly;
    private List<Airport> airportsToGo;
    private List<Route> suggestedRoutes;
    private List<AircraftAssignment> suggestedAA;
    private List<AircraftMaintenanceSlot> suggestedMaint;

    private int reserveAircraft;
    private double hubSavings = 0.20;
    private double maxRange = 0;

    public ScheduleDevelopmentBean() {
    }

    public void addAircrafts(long[] acIds) throws NotFoundException {
        int l = acIds.length;
        for (int i = 0; i < l; i++) {
            Aircraft ac = em.find(Aircraft.class, acIds[i]);
            if (ac == null) throw new NotFoundException();
            else {
                aircraftsToFly.add(ac);
            }
        }
    }

    public void addAirports(long[] apIds) throws NotFoundException {
        int l = apIds.length;
        for (int i = 0; i < l; i++) {
            Airport ap = em.find(Airport.class, apIds[i]);
            if (ap == null)
                throw new NotFoundException();
            else
                airportsToGo.add(ap);
        }
    }

    public void addHubs(long[] apIds) throws NotFoundException {
        int l = apIds.length;
        for (int i = 0; i < l; i++) {
            Airport ap = em.find(Airport.class, apIds[i]);
            if (ap == null)
                throw new NotFoundException();
            else if (airportsToGo.indexOf(ap) != -1)
                airportsToGo.add(ap);
        }
    }

    public void process() {
        //Steps:
        //Adding aircraft & airports
        //Set hubs
        //Setting variables
        //Create all combinations of possible AAs

    }
}
