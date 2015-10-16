package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;
import MAS.ScheduleDev.HypoAircraft;
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

    private List<HypoAircraft> aircraftsToFly;
    private List<Airport> airportsToGo;
    private List<Airport> hubs;
    private List<Double> hubSavings;
    private List<Route> suggestedRoutes;
    private List<HypoRoute> allRoutes;
    private List<AircraftAssignment> suggestedAA;
    private List<AircraftMaintenanceSlot> suggestedMaint;

    private List<List<Airport>> tierList;

    private int reserveAircraft;
    //private double hubSavings = 0.25;
    private double maxRange = 0;

    public ScheduleDevelopmentBean() {
        aircraftsToFly = new ArrayList<>();
        airportsToGo = new ArrayList<>();
        allRoutes = new ArrayList<>();
        suggestedRoutes = new ArrayList<>();
        suggestedAA = new ArrayList<>();
        suggestedMaint = new ArrayList<>();
        hubs = new ArrayList<>();
        hubSavings = new ArrayList<>();
    }

    public void addAircrafts(List<Long> acIds, List<String> apIds) throws NotFoundException {
        if (acIds.size() != apIds.size()) throw new NotFoundException("Aircraft and airport list must be same length!");
        int l = acIds.size();
        for (int i = 0; i < l; i++) {
            Aircraft ac = em.find(Aircraft.class, acIds.get(i));
            if (ac == null) throw new NotFoundException();
            Airport ap = em.find(Airport.class, apIds.get(i));
            if (ap == null) throw new NotFoundException();
            else {
                HypoAircraft hypoAircraft = new HypoAircraft();
                hypoAircraft.aircraft = ac;
                hypoAircraft.aircraft.setRange(ac.getSeatConfig().getAircraftType().getMaxRange() * Constants.OPERATIONAL_RANGE);
                hypoAircraft.homeBase = ap;
                costsBean.calculateCostEstimate(hypoAircraft, 100);
                aircraftsToFly.add(hypoAircraft);
                if (hypoAircraft.aircraft.getRange() > maxRange)
                    maxRange = hypoAircraft.aircraft.getRange();
            }
        }
    }

    public void addAirports(List<String> apIds) throws NotFoundException {
        int l = apIds.size();
        for (int i = 0; i < l; i++) {
            Airport ap = em.find(Airport.class, apIds.get(i));
            if (ap == null)
                throw new NotFoundException();
            else
                airportsToGo.add(ap);
        }
    }

    public void addHubs(List<String> apIds, List<Double> hubStrength) throws NotFoundException {
        if (apIds.size() != hubStrength.size()) throw new NotFoundException("Hub strength and airport list must be same length!");
        int l = apIds.size();
        for (int i = 0; i < l; i++) {
            Airport ap = em.find(Airport.class, apIds.get(i));
            if (ap == null)
                throw new NotFoundException();
            else if (airportsToGo.indexOf(ap) != -1) {
                hubs.add(ap);
                hubSavings.add(1.0 - hubStrength.get(i));
            }
        }
    }

    private void generateRoutes() {
        int l = airportsToGo.size();
        for (int i = 0; i < l; i++) {
            Airport origin = airportsToGo.get(i);
            for (int j = 0; j < l; j++) {
                if (j == i) continue;
                Airport destination = airportsToGo.get(j);
                HypoRoute hypoRoute = createNewHypoRoute(origin, destination);
                if (isHub(origin))
                    hypoRoute.costDistance *= hubSavings.get(hubs.indexOf(origin));
                if (isHub(destination))
                    hypoRoute.costDistance *= hubSavings.get(hubs.indexOf(destination));
                if (hypoRoute.actualDistance > maxRange)
                    continue;
                allRoutes.add(hypoRoute);
            }
        }
    }

    private HypoRoute createNewHypoRoute (Airport origin, Airport destination) {
        HypoRoute hypoRoute = new HypoRoute();
        Route route = new Route();
        route.setOrigin(origin);
        route.setDestination(destination);
        double distance = Utils.calculateDistance(origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude());
        route.setDistance(distance);
        hypoRoute.routes.add(route);
        hypoRoute.actualDistance = distance;
        hypoRoute.costDistance = distance;// * Constants.RANGE_INERTIA;
        return hypoRoute;
    }

    private void selectGoodRoutes() {
        int l = airportsToGo.size();
        List<HypoRoute> efficientRoutes = new ArrayList<>();
        for (int i = 0; i < l; i++) { //For all airports
            Airport origin = airportsToGo.get(i);
            List<HypoRoute> originRoutes = getHypoRoutesStarting(origin); //Get the routes starting from this airport
            for (int j = 0; j < originRoutes.size(); j++) { //Iterate through it
                HypoRoute baseRoute = originRoutes.get(j);
                double baseCost = baseRoute.costDistance;
                Airport destination = baseRoute.route().getDestination();
                //Check any other more efficient routes vs. baseRoute
                HypoRoute cheapRoute = getCheapestRoute(origin, destination, baseCost, originRoutes);
                if (cheapRoute != null)
                    efficientRoutes.add(cheapRoute);
            }
        }
        addToSuggestedRoutes(efficientRoutes);
    }

    private void addToSuggestedRoutes(List<HypoRoute> hypoRoutes) {
        for (int i = 0; i < hypoRoutes.size(); i ++) {
            for (int j = 0; j < hypoRoutes.get(i).routes.size(); j++) {
                Route route = hypoRoutes.get(i).routes.get(j);
                if (!suggestedRouteExists(route)) {
                    suggestedRoutes.add(route);
                }
                Route reverseRoute = reverseRoute(route);
                if (!suggestedRouteExists(reverseRoute)) {
                    suggestedRoutes.add(reverseRoute);
                }
            }
        }
    }

    private boolean suggestedRouteExists(Route route) {
        for (int i = 0; i < suggestedRoutes.size(); i++) {
            if (route.isSame(suggestedRoutes.get(i)))
                return true;
        }
        return false;
    }

    private Route reverseRoute(Route route) {
        Route newRoute = new Route();
        newRoute.setDestination(route.getOrigin());
        newRoute.setOrigin(route.getDestination());
        newRoute.setDistance(route.getDistance());
        return newRoute;
    }

    private HypoRoute getCheapestRoute(Airport origin, Airport destination, double baseCost, List<HypoRoute> startRoutes) {
        HypoRoute result = new HypoRoute();
        double minCost = baseCost;
        //System.out.println("--------------- " + origin.getName() + " to "+ destination.getName() + " -----------------------");
        //Destination is near a hub
        Airport nearHub = nearHub(destination);
        if (nearHub != null && nearHub != origin) {
            //System.out.println("Setting: NONE");
            if (isHub(origin)) {
                if (hubSavings.get(hubs.indexOf(origin)) > hubSavings.get(hubs.indexOf(nearHub)))
                    return null;
            } else
                return null;
        }
        for (int i = 0; i < startRoutes.size(); i++) {
            HypoRoute currRoute = startRoutes.get(i);
            //Recursive search for route to destination
            //Stop if cost of route is > basecost
            HypoRoute calcRoute = getRouteTo(destination, minCost, currRoute);
            if (calcRoute != null && calcRoute.costDistance <= minCost) {
                result = calcRoute;
                minCost = result.costDistance;
            }
        }
        //System.out.println("Setting: " + result.print() + "(" + result.costDistance + ")");
        return result;
    }

    private HypoRoute getRouteTo(Airport destination, double minCost, HypoRoute calcRoute) {
        if (calcRoute.latestRoute().getDestination() == destination) {//Reached end
            if (calcRoute.costDistance <= minCost) { //This is the most effective route
                //System.out.println("END : " + calcRoute.print() + " | " + destination.getName() + " (" + calcRoute.costDistance + ")");
                return calcRoute;
            }
        }
        List<HypoRoute> allOptions = new ArrayList<>();
        List<HypoRoute> branches = getHypoRoutesStarting(calcRoute.latestRoute().getDestination());
        for (int i = 0; i < branches.size(); i++) {
            HypoRoute currBranch = branches.get(i);
            if (!calcRoute.isOriginAlongRoute(currBranch.route().getDestination())) { //Prevent going backwards
                if ((calcRoute.costDistance + currBranch.costDistance) * Constants.RANGE_MOMENTUM <= minCost) { //More efficient route than baseline so far
                    //System.out.println("FINDING: " + calcRoute.print());
                    HypoRoute newRoute = getRouteTo(destination, minCost, calcRoute.addShortRRoute(currBranch));
                    if (newRoute != null)// && newRoute.costDistance <= minCost)
                        allOptions.add(newRoute);
                }
//                else {
//                    System.out.println("DEAD END: " + calcRoute.print() + " & " + currBranch.print() + " vs " + destination.getName());
//                }
            }
        }

        if (allOptions.size() > 0) {
            HypoRoute bestRoute = minRouteInArray(allOptions);
            return bestRoute;
        }
        return null;
    }

    private HypoRoute minRouteInArray(List<HypoRoute> routeList) {
        if (routeList == null) return null;
        if (routeList.size() < 1) return null;

        HypoRoute result = routeList.get(0);
        for (int i = 1; i < routeList.size(); i++) {
            HypoRoute current = routeList.get(i);
            if (current.costDistance < result.costDistance)
                result = current;
        }
        //System.out.println("BEST: " + result.print() + ": " + result.costDistance);
        return result;
    }

    private List<HypoRoute> getHypoRoutesStarting(Airport origin) {
        List<HypoRoute> result = new ArrayList<>();
        for (int i = 0; i < allRoutes.size(); i++) {
            if (allRoutes.get(i).route().getOrigin() == origin)
                result.add(allRoutes.get(i));
        }
        return result;
    }

    private HypoRoute getHypoRoute(Airport origin, Airport destination) {
        for (int i = 0; i < allRoutes.size(); i++) {
            HypoRoute current = allRoutes.get(i);
            if (current.route().getOrigin() == origin) {
                if (current.route().getDestination() == destination)
                    return current;
            }
        }
        return null;
    }

    private boolean isHub(Airport airport) {
        if (hubs.indexOf(airport) != -1)
            return true;
        return false;
    }

    private Airport nearHub(Airport airport) {
        HypoRoute hubRoute = null;
        for (int i = 0; i < hubs.size(); i++) {
            List<HypoRoute> routesFromHub = getHypoRoutesStarting(hubs.get(i));
            for (int j = 0; j < routesFromHub.size(); j++) {
                if (routesFromHub.get(j).route().getDestination() == airport) {
                    if (hubRoute == null)
                        hubRoute = routesFromHub.get(j);
                    else if (routesFromHub.get(j).actualDistance < hubRoute.actualDistance)
                        hubRoute = routesFromHub.get(j);
                }
            }
        }
        if (hubRoute != null)
            return hubRoute.route().getOrigin();
        return null;
    }

    private void allocateAircraft() {
        //Do routes starting from the hub first
        //Find cheapest aircraft to allocate going out of the hub.
        //  If it has been allocated, its priority shifts down a notch
        //
    }

    private void generateTierList() {
        List<Route> flyRoutes = new ArrayList<>(suggestedRoutes);
        List<Airport> flyAirports = new ArrayList<>(airportsToGo);


    }

    public void saveSuggestedRoutes() {
        for (int i = 0; i < suggestedRoutes.size(); i++) {
            em.persist(suggestedRoutes.get(i));
        }
        em.flush();
    }

    private void debugAllRoutes(List<HypoRoute> routeList) {
        System.out.println("-----------------------ALL ROUTES-----------------------");
        for (int i = 0; i < routeList.size(); i++) {
            System.out.println(routeList.get(i).print() + " : " + routeList.get(i).costDistance);
        }
    }

    private void debugAllSuggRoutes() {
        System.out.println("-----------------------ALL SUGGESTED ROUTES-----------------------");
        for (int i = 0; i < suggestedRoutes.size(); i++) {
            System.out.println(suggestedRoutes.get(i).getOrigin().getName() + " - " + suggestedRoutes.get(i).getDestination().getName());
        }
    }

    public void process() {
        generateRoutes();
        //debugAllRoutes(allRoutes);
        selectGoodRoutes();
        allocateAircraft();
        saveSuggestedRoutes();
        System.gc();
    }
}
