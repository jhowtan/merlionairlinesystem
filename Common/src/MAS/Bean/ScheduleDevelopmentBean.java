package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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
    private List<Route> allRoutes;
    private List<AircraftAssignment> suggestedAA;
    private List<AircraftMaintenanceSlot> suggestedMaint;

    private int reserveAircraft;
    private double hubSavings = 0.20;
    private double maxRange = 0;

    public ScheduleDevelopmentBean() {
        aircraftsToFly = new ArrayList<>();
        airportsToGo = new ArrayList<>();
        allRoutes = new ArrayList<>();
        suggestedRoutes = new ArrayList<>();
        suggestedAA = new ArrayList<>();
        suggestedMaint = new ArrayList<>();
    }

    public void addAircrafts(List<Long> acIds) throws NotFoundException {
        int l = acIds.size();
        double range = 0;
        for (int i = 0; i < l; i++) {
            Aircraft ac = em.find(Aircraft.class, acIds.get(i));
            if (ac == null) throw new NotFoundException();
            else {
                aircraftsToFly.add(ac);
                range = ac.getSeatConfig().getAircraftType().getMaxRange();
                if (range > maxRange)
                    maxRange = range;
            }
        }
    }

    public void addAirports(List<Long> apIds) throws NotFoundException {
        int l = apIds.size();
        for (int i = 0; i < l; i++) {
            Airport ap = em.find(Airport.class, apIds.get(i));
            if (ap == null)
                throw new NotFoundException();
            else
                airportsToGo.add(ap);
        }
    }

    public void addHubs(List<Long> apIds) throws NotFoundException {
        int l = apIds.size();
        for (int i = 0; i < l; i++) {
            Airport ap = em.find(Airport.class, apIds.get(i));
            if (ap == null)
                throw new NotFoundException();
            else if (airportsToGo.indexOf(ap) != -1)
                airportsToGo.add(ap);
        }
    }

    private void generateRoutes() {
        int l = airportsToGo.size();
        for (int i = 0; i < l; i++) {
            Airport origin = airportsToGo.get(i);
            for (int j = 0; j < l; j++) {
                if (j == i) continue;
                Airport destination = airportsToGo.get(j);
                Route route = new Route();
                route.setOrigin(origin);
                route.setDestination(destination);
                double distance = Utils.calculateDistance(origin.getLatitude(), origin.getLongitude(),
                        destination.getLatitude(), destination.getLongitude());
                route.setDistance(distance);
                System.out.println(distance + " " + maxRange);
                if (distance > maxRange)
                    continue;
                allRoutes.add(route);
            }
        }
    }

    private void debugAllRoutes() {
        System.out.println("ALL ROUTES-----------------------");
        for (int i = 0; i < allRoutes.size(); i++) {
            System.out.println(allRoutes.get(i).getOrigin().getName() + " - " + allRoutes.get(i).getDestination().getName());
        }
    }

    public void process() {
        generateRoutes();
        debugAllRoutes();
    }
}
