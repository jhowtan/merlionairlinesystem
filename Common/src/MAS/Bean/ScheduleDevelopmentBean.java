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
    private int maxDays;

    public ScheduleDevelopmentBean() {
    }

    public void addAircrafts(long[] acIds) throws NotFoundException {
        int l = acIds.length;
        for (int i = 0; i < l; i++) {

        }
    }
}
