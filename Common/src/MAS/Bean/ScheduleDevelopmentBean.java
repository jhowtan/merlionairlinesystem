package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;
import MAS.ScheduleDev.HypoRoute;

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
    private List<Airport> hubs;
    private List<Route> suggestedRoutes;
    private List<HypoRoute> allRoutes;
    private List<AircraftAssignment> suggestedAA;
    private List<AircraftMaintenanceSlot> suggestedMaint;

    private int reserveAircraft;
    private double hubSavings = 0.40;
    private double maxRange = 0;

    public ScheduleDevelopmentBean() {
        aircraftsToFly = new ArrayList<>();
        airportsToGo = new ArrayList<>();
        allRoutes = new ArrayList<>();
        suggestedRoutes = new ArrayList<>();
        suggestedAA = new ArrayList<>();
        suggestedMaint = new ArrayList<>();
        hubs = new ArrayList<>();
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
                hubs.add(ap);
        }
    }

    private void generateRoutes() {
        int l = airportsToGo.size();
        for (int i = 0; i < l; i++) {
            Airport origin = airportsToGo.get(i);
            for (int j = 0; j < l; j++) {
                if (j == i) continue;
                Airport destination = airportsToGo.get(j);
                HypoRoute hypoRoute = new HypoRoute();
                Route route = new Route();
                route.setOrigin(origin);
                route.setDestination(destination);
                double distance = Utils.calculateDistance(origin.getLatitude(), origin.getLongitude(),
                        destination.getLatitude(), destination.getLongitude());
                route.setDistance(distance);
                hypoRoute.routes.add(route);
                hypoRoute.actualDistance = distance;
                hypoRoute.costDistance = distance * Constants.RANGE_INERTIA;
                if (isHub(origin) || isHub(destination))
                    hypoRoute.costDistance *= hubSavings;
                if (distance > maxRange)
                    continue;
                allRoutes.add(hypoRoute);
            }
        }
    }

    private void selectGoodRoutes() {
        int l = airportsToGo.size();
        for (int i = 0; i < l; i++) { //For all airports
            Airport origin = airportsToGo.get(i);
            List<HypoRoute> originRoutes = getHypoRoutesStarting(origin); //Get the routes starting from this airport
            for (int j = 0; j < originRoutes.size(); j++) { //Iterate through it
                HypoRoute baseRoute = originRoutes.get(j);
                double baseCost = baseRoute.costDistance;
                Airport destination = baseRoute.route().getDestination();
                //Check any other more efficient routes vs. baseRoute
                getCheapestRoute(origin, destination, baseCost, originRoutes);
            }
        }
    }

    private HypoRoute getCheapestRoute(Airport origin, Airport destination, double baseCost, List<HypoRoute> startRoutes) {
        HypoRoute result = new HypoRoute();
        double minCost = baseCost;
        for (int i = 0; i < startRoutes.size(); i++) {
            HypoRoute currRoute = startRoutes.get(i);
            if (currRoute.route().getOrigin() == origin && currRoute.route().getDestination() == destination) {
                if (minCost == baseCost)
                    result = currRoute;
            }
            else {
                //Recursive search for route to destination
                //Stop if cost of route is > basecost
                HypoRoute calcRoute = getRouteTo(currRoute.route().getDestination(), currRoute.costDistance, currRoute);
                if (calcRoute != null)
                    result = calcRoute;
            }
        }
        return result;
    }

    private HypoRoute getRouteTo(Airport destination, double baseCost, HypoRoute startRoute) {
        return null;
    }

    private List<HypoRoute> getHypoRoutesStarting(Airport origin) {
        List<HypoRoute> result = new ArrayList<>();
        for (int i = 0; i < allRoutes.size(); i++) {
            if (allRoutes.get(i).route().getOrigin() == origin)
                result.add(allRoutes.get(i));
        }
        return result;
    }

    private boolean isHub(Airport airport) {
        if (hubs.indexOf(airport) != -1)
            return true;
        return false;
    }

    private void debugAllRoutes() {
        System.out.println("ALL ROUTES-----------------------");
        for (int i = 0; i < allRoutes.size(); i++) {
            System.out.println(allRoutes.get(i).route().getOrigin().getName() + " - " + allRoutes.get(i).route().getDestination().getName() + " : " + allRoutes.get(i).costDistance);
        }
    }

    public void process() {
        generateRoutes();
        debugAllRoutes();
    }
}
