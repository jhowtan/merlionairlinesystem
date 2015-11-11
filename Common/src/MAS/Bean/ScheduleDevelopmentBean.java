package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Airport;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;
import MAS.ScheduleDev.HypoAircraft;
import MAS.ScheduleDev.HypoRoute;
import MAS.ScheduleDev.HypoTransit;
import MAS.ScheduleDev.TransitAircrafts;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateful(name = "ScheduleDevelopmentEJB")
@LocalBean
public class ScheduleDevelopmentBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    CostsBean costsBean;
    @EJB
    RouteBean routeBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;

    private List<HypoAircraft> aircraftsToFly;
    private List<HypoAircraft> aircraftStillFlying;
    private List<Airport> airportsToGo;
    private List<List<HypoAircraft>> airportBuckets;
    private List<List<Route>> airportRoutesOut;
    private List<Airport> hubs;
    private List<Double> hubSavings;
    private List<Route> suggestedRoutes;
    private List<HypoRoute> allRoutes;

    private List<List<Airport>> tierList;
    private TransitAircrafts ta;

    private List<String[]> routeOutputTable;

    private int reserveAircraft;
    private double maxRange = 0;
    private double acRestTime = 180;
    private double acMaintTime = 1440;
    private int processCycle = 1;

    public ScheduleDevelopmentBean() {

    }

    @PostConstruct
    private void init() {
        aircraftsToFly = new ArrayList<>();
        airportsToGo = new ArrayList<>();
        allRoutes = new ArrayList<>();
        suggestedRoutes = new ArrayList<>();
        hubs = new ArrayList<>();
        hubSavings = new ArrayList<>();
        airportBuckets = new ArrayList<>();
        routeOutputTable = new ArrayList<>();
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
                hypoAircraft.aircraft.setMaxRange(ac.getSeatConfig().getAircraftType().getMaxRange() * Constants.OPERATIONAL_RANGE);
                hypoAircraft.startingAp = ap;
                costsBean.calculateCostEstimate(hypoAircraft, 100);
                aircraftsToFly.add(hypoAircraft);
                if (hypoAircraft.aircraft.getMaxRange() > maxRange)
                    maxRange = hypoAircraft.aircraft.getMaxRange();
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
        hypoRoute.costDistance = distance;
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
        String[] routeOutput = new String[3];
        routeOutput[0] = origin.getName() + " to " + destination.getName();
        //System.out.println("--------------- " + origin.getName() + " to "+ destination.getName() + " -----------------------");
        //Destination is near a hub
        Airport nearHub = nearHub(destination);
        if (nearHub != null && nearHub != origin) {
            //System.out.println("Setting: NONE");
            if (isHub(origin)) {
                if (hubSavings.get(hubs.indexOf(origin)) > hubSavings.get(hubs.indexOf(nearHub))) {
                    routeOutput[1] = "Destination is a nearer to another hub, routing not required.";
                    try {
                        HypoRoute hubRoute = getHypoRoute(nearHub, destination);
                        routeOutput[2] = hubRoute.print() + "(" + hubRoute.costDistance + ")";
                    } catch (Exception e) {
                        routeOutput[2] = "None";
                    }
                    routeOutputTable.add(routeOutput);
                    return null;
                }
            } else {
                routeOutput[1] = "Destination is a near a hub, routing not required.";
                try {
                    HypoRoute hubRoute = getHypoRoute(nearHub, destination);
                    routeOutput[2] = hubRoute.print() + "(" + hubRoute.costDistance + ")";
                } catch (Exception e) {
                    routeOutput[2] = "None";
                }
                routeOutputTable.add(routeOutput);
                return null;
            }
        }
        routeOutput[1] = "";
        for (int i = 0; i < startRoutes.size(); i++) {
            HypoRoute currRoute = startRoutes.get(i);
            //Recursive search for route to destination
            //Stop if cost of route is > basecost
            HypoRoute calcRoute = getRouteTo(destination, minCost, currRoute);
            if (calcRoute != null && calcRoute.costDistance <= minCost) {
                routeOutput[1] = routeOutput[1].concat("\n" + calcRoute.print() + " (" + calcRoute.costDistance + ")");
                result = calcRoute;
                minCost = result.costDistance;
            }
        }
        routeOutput[2] = result.print() + " (" + result.costDistance + ")";
        routeOutputTable.add(routeOutput);
        //System.out.println("Setting: " + result.print() + "(" + result.costDistance + ")");
        return result;
    }

    private HypoRoute getRouteTo(Airport destination, double minCost, HypoRoute calcRoute) {
        if (calcRoute.latestRoute().getDestination().equals(destination)) {//Reached end
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
            }
        }

        if (allOptions.size() > 0) {
            HypoRoute bestRoute = minHypoRouteInArray(allOptions);
            return bestRoute;
        }
        return null;
    }

    private HypoRoute minHypoRouteInArray(List<HypoRoute> routeList) {
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
            if (allRoutes.get(i).route().getOrigin().equals(origin))
                result.add(allRoutes.get(i));
        }
        return result;
    }

    private List<Route> getRoutesStarting(Airport origin, List<Route> routeList) {
        List<Route> result = new ArrayList<>();
        for (int i = 0; i < routeList.size(); i++) {
            if (routeList.get(i).getOrigin().equals(origin))
                result.add(routeList.get(i));
        }
        return result;
    }

    private Route getRouteToFrom(Airport origin, Airport destination) throws NotFoundException {
        for (int i = 0; i < suggestedRoutes.size(); i++) {
            if (suggestedRoutes.get(i).getOrigin().equals(origin))
                if (suggestedRoutes.get(i).getDestination().equals(destination))
                    return suggestedRoutes.get(i);
        }
        throw new NotFoundException();
    }

    private HypoRoute getHypoRoute(Airport origin, Airport destination) throws NotFoundException{
        for (int i = 0; i < allRoutes.size(); i++) {
            HypoRoute current = allRoutes.get(i);
            if (current.route().getOrigin().equals(origin)) {
                if (current.route().getDestination().equals(destination))
                    return current;
            }
        }
        throw new NotFoundException();
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
                if (routesFromHub.get(j).route().getDestination().equals(airport)) {
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

    private Route routeBackToHub(Airport airport) throws  NotFoundException {
        if (isHub(airport)) throw new NotFoundException("This airport is already a hub!");
        Airport nearestHub = nearHub(airport);
        if (nearestHub != null)
            return getRouteToFrom(airport, nearestHub);

        throw new NotFoundException();
    }

    private void addToBucket(HypoAircraft ac, Airport ap) throws Exception {
        int index = airportsToGo.indexOf(ap);
        if (index == -1) throw new Exception("Airport is not in list");
        removeFromBucket(ac);
        airportBuckets.get(index).add(ac);
    }

    private void removeFromBucket(HypoAircraft ac) {
        for (int i = 0; i < airportBuckets.size(); i++) {
            if (airportBuckets.get(i).indexOf(ac) != -1) {
                airportBuckets.get(i).remove(ac);
                return;
            }
        }
    }

    private void createFlightTimetable(int forTime) throws Exception {
        //Do routes starting from the hub first
        //Find cheapest aircraft to allocate going out of the hub.
        prepareApBuckets();
        prepareApRoutes();
        int timeAfterZero = 0;
        ta = new TransitAircrafts();
        while (timeAfterZero < forTime) {
            //Find most expensive actual distance out of this place
            //Get all routes out of this place
            for (int i = 0; i < airportsToGo.size(); i++) {//Each airport
                Airport currentAirport = airportsToGo.get(i);
                List<HypoAircraft> planesHere = airportBuckets.get(i);
                if (planesHere.size() == 0) continue; //Nothing here, move along
                List<Route> routesHere = airportRoutesOut.get(i);
                for (int j = 0; j < planesHere.size(); j++) {
                    HypoAircraft aircraft = planesHere.get(j);
                    if (aircraft.reqMaint) {
                        if (!isHub(currentAirport)) {
                            //ROUTE THIS PLANE TO NEAREST HUB
                            try {
                                Route rtb = routeBackToHub(currentAirport);
                                double duration = Utils.calculateDuration(rtb.getDistance(), aircraft.aircraft.getSeatConfig().getAircraftType().getSpeed());
                                ta.fly(aircraft, duration, rtb, timeAfterZero);
                                planesHere.remove(aircraft);
                                j--;
                                routesHere.remove(rtb);
                                routesHere.add(rtb);
                                continue;
                            } catch (Exception e) {
                                //Plane needs maint
                            }
                        } else {
                            List<HypoTransit> acUnderMaint = ta.maintAtAirport(currentAirport);
                            if (acUnderMaint.size() < currentAirport.getHangars()) {
                                ta.maintenance(aircraft, acMaintTime, currentAirport, timeAfterZero, true);
                            } else {
                                //Get plane with lowest time left for maint
                                ta.maintenance(aircraft, ta.getShortestTimeLeft(acUnderMaint)+1, currentAirport, timeAfterZero, false);
                            }
                        }
                    }
                    Route routeOut = routesHere.get(0);
                    //System.out.println(j + ": " + routeOut.getDestination().getName() + "_" + routeOut.getDistance() + "/" + aircraft.aircraft.getMaxRange());
                    while (routeOut.getDistance() > aircraft.aircraft.getMaxRange()) { //Get first route this plane is capable of flying
                        int index = routesHere.indexOf(routeOut);
                        if (index + 1 < routesHere.size())
                            routeOut = routesHere.get( index + 1 );
                        else {
                            routeOut = null;
                            break;
                        }
                    }
                    if (routeOut == null) {
                        ta.maintenance(aircraft, processCycle, currentAirport, timeAfterZero, false); //Keep plane stationary for 1 minute
                        planesHere.remove(aircraft);
                        j--;
                        continue;
                    }
                    double duration = Utils.calculateDuration(routeOut.getDistance(), aircraft.aircraft.getSeatConfig().getAircraftType().getSpeed());
                    ta.fly(aircraft, duration, routeOut, timeAfterZero); //Fly cheapest aircraft for most expensive route
                    planesHere.remove(aircraft);
                    j--;
                    routesHere.remove(routeOut);
                    routesHere.add(routeOut); //Move route to back of the line
                }
            }
            //Land aircrafts that are flying
            List<HypoAircraft> landingAircrafts = ta.land();
            timeAfterZero += ta.lastTime; //This amount of time has past
            for (int i = 0; i < landingAircrafts.size(); i++) {
                HypoAircraft landingAc = landingAircrafts.get(i);
                if (landingAc.location != landingAc.prevLocation) {
                    shiftRoute(landingAc);
                    if (landingAc.reqMaint) {
                        if (isHub(landingAc.location)) {
                            List<HypoTransit> acUnderMaint = ta.maintAtAirport(landingAc.location);
                            if (acUnderMaint.size() < landingAc.location.getHangars()) {
                                ta.maintenance(landingAc, acMaintTime, landingAc.location, timeAfterZero, true);
                            } else {
                                //Get plane with lowest time left for maint
                                ta.maintenance(landingAc, ta.getShortestTimeLeft(acUnderMaint)+1, landingAc.location, timeAfterZero, false);
                            }
                        }
                        else
                            ta.maintenance(landingAc, acRestTime, landingAc.location, timeAfterZero, false);
                    } else {
                        //Let plane rest
                        ta.maintenance(landingAc, acRestTime, landingAc.location, timeAfterZero, false);
                    }
                } else {
                    addToBucket(landingAc, landingAc.location);
                }
            }

        }
    }

    private void shiftRoute(HypoAircraft hypoAircraft) {
        List<Route> routesHere = airportRoutesOut.get(airportsToGo.indexOf(hypoAircraft.location));
        for (int i = 0; i <  routesHere.size(); i++) {
            if (routesHere.get(i).getDestination().equals(hypoAircraft.prevLocation)) {
                routesHere.add(routesHere.remove(i));
                return;
            }
        }
    }

    private void generateTierList() {
        tierList = new ArrayList<>();
        List<Route> flyRoutes = new ArrayList<>(suggestedRoutes);
        List<Airport> flyAirports = new ArrayList<>(airportsToGo);
        List<Airport> currentTier = new ArrayList<>(hubs); //Hubs are tier 0
        flyAirports.removeAll(hubs);
        tierList.add(currentTier);

        do {
            List<Airport> nextTier = new ArrayList<>();
            if (currentTier.size() == 0) break;
            for (int i = 0; i < currentTier.size(); i++) {
                List<Route> routesOut = getRoutesStarting(currentTier.get(i), suggestedRoutes);
                for (int j = 0; j < routesOut.size(); j++) {
                    Airport currAp = routesOut.get(j).getDestination();
                    if (flyAirports.indexOf(currAp) != -1) { //Airport still hasn't been slotted into a tier
                        nextTier.add(currAp);
                        flyAirports.remove(currAp);
                    }
                }
            }
            currentTier = nextTier;
            tierList.add(currentTier);
        } while (flyAirports.size() > 0);
    }

    private void prepareApBuckets() throws Exception {
        aircraftStillFlying = new ArrayList<>(aircraftsToFly);
        for (int i = 0; i < airportsToGo.size(); i++) { //Initialize airport buckets
            airportBuckets.add(new ArrayList<>());
        }
        //Add aircraft to starting points
        for (int i = 0; i < aircraftStillFlying.size(); i++) {
            addToBucket(aircraftStillFlying.get(i), aircraftStillFlying.get(i).startingAp);
        }
        for (int i = 0; i < airportsToGo.size(); i++) { //Initialize airport buckets
            Collections.sort(airportBuckets.get(i));
        }
    }
    private void prepareApRoutes() {
        airportRoutesOut = new ArrayList<>();
        for (int i = 0; i < airportsToGo.size(); i++) {
            Airport airport = airportsToGo.get(i);
            List<Route> routesOut = getRoutesStarting(airport, suggestedRoutes);
            Collections.sort(routesOut);
            Collections.reverse(routesOut);
            airportRoutesOut.add(routesOut);
        }
    }

    public void saveSuggestedRoutes() {
        for (int i = 0; i < suggestedRoutes.size(); i++) {
            Route route = suggestedRoutes.get(i);
            try {
                route = routeBean.findRouteByOriginAndDestination(route.getOrigin().getId(), route.getDestination().getId());
                suggestedRoutes.set(i, route);
            } catch (Exception e) {
                try {
                    route = routeBean.getRoute( routeBean.createRoute(route.getOrigin().getId(), route.getDestination().getId()) );
                    suggestedRoutes.set(i, route);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
        em.flush();
    }

    public int saveSuggestedFlights(Date startTime) {
            int result = 0;
            List<HypoTransit> allHappenings = ta.getAllHistory();
            for (int i = 0; i < allHappenings.size(); i++) {
                HypoTransit happenings = allHappenings.get(i);
                Aircraft aircraft = happenings.hypoAircraft.aircraft;
                for (int j = 0; j < happenings.pathHistory.size(); j++) {
                    double relativeTime = happenings.pathTimes.get(j);
                    Date dateTime = Utils.minutesLater(startTime, (int) relativeTime);
                    Route route = happenings.pathHistory.get(j);
                    AircraftAssignment aircraftAssignment;
                    try {
                        route = routeBean.matchRoute(route);
                        try {
                            aircraftAssignment = routeBean.findAAByAcAndRoute(aircraft.getId(), route.getId());
                        } catch (NotFoundException e) {
                            aircraftAssignment = routeBean.getAircraftAssignment(routeBean.createAircraftAssignment(aircraft.getId(), route.getId()));
                        }
                        flightScheduleBean.createFlight("MA" + aircraftAssignment.getId(), dateTime, Utils.minutesLater(dateTime, (int) Utils.calculateDuration(route.getDistance(), aircraft.getSeatConfig().getAircraftType().getSpeed())),
                                aircraftAssignment.getId(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result++;
                }
                for (int j = 0; j < happenings.maintHistory.size(); j++) {
                    double relativeTime = happenings.maintTimes.get(j);
                    Date dateTime = Utils.minutesLater(startTime, (int) relativeTime);
                    try {
                        aircraftMaintenanceSlotBean.createSlot(dateTime, acMaintTime, happenings.maintHistory.get(j).getId(), aircraft.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
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
            System.out.println(suggestedRoutes.get(i).getOrigin().getName() + " - " + suggestedRoutes.get(i).getDestination().getName() +"("+ suggestedRoutes.get(i).getDistance() +")");
        }
    }

    private void debugApList() {
        String result = "Airports: ";
        for (int i = 0; i < airportsToGo.size(); i++) {
            result = result.concat(airportsToGo.get(i).getName()) + ", ";
        }
        System.out.println(result);
    }

    private void debugTierList() {
        for (int i = 0; i < tierList.size(); i++) {
            String result = "Tier " + i + ": ";
            for (int j = 0; j < tierList.get(i).size(); j++) {
                result = result.concat(tierList.get(i).get(j).getName() + ", ");
            }
            System.out.println(result);
        }
    }

    private void debugFlightState() {
        String format = "%-40s%s%n";
        String result = "-----------FLIGHT STATE-----------";
        for (int i = 0; i < airportsToGo.size(); i++) {
            Airport airport = airportsToGo.get(i);
            result += "\n" + airport.getName() + ": ";
            for (int j = 0; j < airportBuckets.get(i).size(); j++) {
                result += "[AC: " + airportBuckets.get(i).get(j).aircraft.getTailNumber() +"(" +
                        airportBuckets.get(i).get(j).aircraft.getFlyingCost() + ")" + "], ";
            }
            result += " _ ";
            for (int j = 0; j < airportRoutesOut.get(i).size(); j++) {
                result += "[RO: " + airportRoutesOut.get(i).get(j).getDestination().getName() +
                         "(" + airportRoutesOut.get(i).get(j).getDistance() + ")" + "], ";
            }
        }
        result += "\n Flying aircraft: ";
        result += ta.toString();
        System.out.println(result);
    }

    public List<Route> processRoutes() {
        try {
            generateRoutes();
            selectGoodRoutes();
            System.gc();
            List<Route> prunedRoundTrips = new ArrayList<>(suggestedRoutes);
            for (int i = 0; i < prunedRoundTrips.size(); i++) {
                for (int j = i; j < prunedRoundTrips.size(); j++) {
                    if (prunedRoundTrips.get(i).equalsReversed(prunedRoundTrips.get(j))) {
                        //Is a round trip of the other
                        prunedRoundTrips.remove(j);
                        j--;
                    }
                }
            }
            return prunedRoundTrips;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateRoutes(List<Route> routeList) {
        suggestedRoutes = new ArrayList<>();
        for (int i = 0; i < routeList.size(); i ++) {
            Route route = routeList.get(i);
            if (!suggestedRouteExists(route)) {
                suggestedRoutes.add(route);
            }
            Route reverseRoute = reverseRoute(route);
            if (!suggestedRouteExists(reverseRoute)) {
                suggestedRoutes.add(reverseRoute);
            }
        }
        saveSuggestedRoutes();
    }

    public int processFlights(Date startTime, int duration) throws Exception {
        createFlightTimetable(duration);
        return saveSuggestedFlights(startTime);
    }

    public void testProcess(Date startTime, int duration) {
        try {
            System.out.println("Processing:.....");
            generateRoutes();
            System.out.println("Done:Generate all routes");
            selectGoodRoutes();
            System.out.println("Done:Select good ones");
            saveSuggestedRoutes();
            System.out.println("Processing2:.....");
            generateTierList();
            System.out.println("Done:Create tier list");
            createFlightTimetable(duration);
            debugFlightState();
            System.out.println("Done:Create flight timetable");
            saveSuggestedFlights(startTime);

            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String[]> getRouteOutputTable() {
        return routeOutputTable;
    }

    public void setRouteOutputTable(List<String[]> routeOutputTable) {
        this.routeOutputTable = routeOutputTable;
    }
}
