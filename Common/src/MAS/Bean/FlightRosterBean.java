package MAS.Bean;

import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;
import MAS.RosterDev.HypoCrew;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless(name = "FlightRosterEJB")
@LocalBean
public class FlightRosterBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    FlightBidBean flightBidBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    RouteBean routeBean;
    @EJB
    CrewCertificationBean crewCertificationBean;
    @EJB
    UserBean userBean;

    public FlightRosterBean() {
    }

    public long createFlightRoster(long flightId, List<Long> userIds, boolean complete) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        List<User> members = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            User member = em.find(User.class, userIds.get(i));
            if (member == null) throw new NotFoundException();
            members.add(member);
        }
        FlightRoster flightRoster = new FlightRoster();
        flightRoster.setFlight(flight);
        flightRoster.setMembers(members);
        flightRoster.setComplete(complete);
        em.persist(flightRoster);
        em.flush();
        return flightRoster.getId();
    }

    public long createFlightRoster(long flightId, List<Long> userIds) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        List<User> members = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            User member = em.find(User.class, userIds.get(i));
            if (member == null) throw new NotFoundException();
            members.add(member);
        }
        FlightRoster flightRoster = new FlightRoster();
        flightRoster.setFlight(flight);
        flightRoster.setMembers(members);
        em.persist(flightRoster);
        em.flush();
        updateFlightRosterComplete(flightRoster.getId());
        return flightRoster.getId();
    }

    public FlightRoster getFlightRoster(long id) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, id);
        if (flightRoster == null) throw new NotFoundException();
        return flightRoster;
    }

    public void changeFlightRosterMembers(long id, List<User> members) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, id);
        if (flightRoster == null) throw new NotFoundException();
        flightRoster.setMembers(members);
        updateFlightRosterComplete(id);
    }

    public void updateFlightRosterComplete(long id) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, id);
        if (flightRoster == null) throw new NotFoundException();
        int cabinReq = flightRoster.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCabinCrewReq();
        int cockpitReq = flightRoster.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCockpitCrewReq();
        int inCabin = 0;
        int inCockpit = 0;
        List<User> crew = flightRoster.getMembers();
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getJob() == Constants.cabinCrewJobId)
                inCabin++;
            else if (crew.get(i).getJob() == Constants.cockpitCrewJobId)
                inCockpit++;
        }
        flightRoster.setComplete(inCabin >= cabinReq && inCockpit >= cockpitReq);
    }

    public void removeFlightRoster(long id) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, id);
        if (flightRoster == null) throw new NotFoundException();
        em.remove(flightRoster);
    }

    public FlightRoster getFlightRosterForFlight(long flightId) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        List<FlightRoster> result = em.createQuery("SELECT fr from FlightRoster fr WHERE fr.flight = :flight", FlightRoster.class)
                .setParameter("flight", flight)
                .setMaxResults(1).getResultList();
        if (result.size() == 0) throw new NotFoundException();
        return result.get(0);
    }

    public List<FlightRoster> getFlightRostersOfUser(long userId) throws NotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) throw new NotFoundException();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return em.createQuery("SELECT fr from FlightRoster fr WHERE fr.members IN :userList").setParameter("userList", userList).getResultList();
    }

    public List<FlightRoster> getAllFutureFlightRosters() {
        return em.createQuery("SELECT fr from FlightRoster fr WHERE fr.flight.departureTime > :date").setParameter("date", new Date(), TemporalType.TIMESTAMP).getResultList();
    }

    public List<FlightRoster> getAllFlightRosters() {
        return em.createQuery("SELECT fr from FlightRoster fr").getResultList();
    }

//    public void signInFR(long flightRosterId, long userId) throws NotFoundException {
//        FlightRoster flightRoster = em.find(FlightRoster.class, flightRosterId);
//        User user = em.find(User.class, userId);
//        if (flightRoster == null || user == null) throw new NotFoundException();
//        List<User> signedIn = flightRoster.getSignedIn();
//        if (signedIn == null) signedIn = new ArrayList<>();
//        if (signedIn.indexOf(user) == -1) signedIn.add(user);
//        flightRoster.setSignedIn(signedIn);
//        em.persist(flightRoster);
//    }
//
//    public void signOutFR(long flightRosterId, long userId) throws NotFoundException {
//        FlightRoster flightRoster = em.find(FlightRoster.class, flightRosterId);
//        User user = em.find(User.class, userId);
//        if (flightRoster == null || user == null) throw new NotFoundException();
//        List<User> signedOut = flightRoster.getSignedOut();
//        if (signedOut == null) signedOut = new ArrayList<>();
//        if (signedOut.indexOf(user) == -1) signedOut.add(user);
//        flightRoster.setSignedOut(signedOut);
//        user.setCurrentLocation(flightRoster.getFlight().getAircraftAssignment().getRoute().getDestination());
//        em.persist(flightRoster);
//    }

    public void allocateFlightJobs() {
        List<FlightBid> flightBids = flightBidBean.getFlightBidsWithStatus(0);
        List<Flight> flights = flightScheduleBean.getFlightWithinDate(Utils.monthStart(1), Utils.monthEnd(1));
        //Sort flights by date
        Collections.sort(flights);
        List<Airport> airports = new ArrayList<>();
        List<List<HypoCrew>> airportBuckets = new ArrayList<>();
        //List<FlightRoster> flightRosters = new ArrayList<>();
        //Initialise airport buckets
        //For each flight, choose flight attendants. If unable, mark as incomplete
        for (int i = 0; i < flightBids.size(); i++) {
            User crew = flightBids.get(i).getBidder();
            Airport loc = crew.getCurrentLocation();
            if (airports.indexOf(loc) == -1) {
                airports.add(loc);
                airportBuckets.add(new ArrayList<>());
            }
            HypoCrew hypoCrew = new HypoCrew();
            hypoCrew.user = crew;
            hypoCrew.location = crew.getCurrentLocation();
            hypoCrew.lastSuccess = Utils.monthStart(1);
            hypoCrew.readyTime = Utils.monthStart(1);
            airportBuckets.get(airports.indexOf(loc)).add(hypoCrew);
            flightBids.get(i).setStatus(1);
            em.merge(flightBids.get(i));
        }

        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            List<User> cabinRoster = new ArrayList<>();
            List<User> cockpitRoster = new ArrayList<>();
            Airport origin = flights.get(i).getAircraftAssignment().getRoute().getOrigin();
            Airport dest = flights.get(i).getAircraftAssignment().getRoute().getDestination();
            int cabinReq = flights.get(i).getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCabinCrewReq();
            int cockpitReq = flights.get(i).getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCockpitCrewReq();
            List<HypoCrew> available = airportBuckets.get(airports.indexOf(origin));
            List<HypoCrew> prospectiveCabin = new ArrayList<>();
            List<HypoCrew> prospectiveCockpit = new ArrayList<>();
            for (int j = 0; j < available.size(); j++) {
                HypoCrew crew = available.get(j);
                try {
                    //flight crew is not ready yet
                    if (crew.readyTime.compareTo(flight.getDepartureTime()) == 1) continue;
                    //if this user has chosen this flight, add
                    if (getBidFromUser(crew.user, flightBids).getFlights().indexOf(flight) != -1) {
                        if (crew.user.getJob() == Constants.cabinCrewJobId)
                            prospectiveCabin.add(0, crew);
                        else if (crew.user.getJob() == Constants.cockpitCrewJobId)
                            prospectiveCockpit.add(0, crew);
                        crew.lastSuccess = flight.getDepartureTime();
                    }
                    //else, if user is qualified but did not choose
                    else if (crewCertificationBean.crewCertifiedFor(crew.user.getId(), flight.getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getId())) {
                        if (crew.user.getJob() == Constants.cabinCrewJobId)
                            prospectiveCabin.add(crew);
                        else if (crew.user.getJob() == Constants.cockpitCrewJobId)
                            prospectiveCockpit.add(crew);
                    }
                    //else if dont need then dont add
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int k = 0; k < prospectiveCabin.size(); k++) {
                if (cabinRoster.size() >= cabinReq) break;
                else addToRoster(prospectiveCabin.get(k), flight, cabinRoster, airportBuckets, airports);
            }
            for (int k = 0; k < prospectiveCockpit.size(); k++) {
                if (cockpitRoster.size() >= cockpitReq) break;
                else addToRoster(prospectiveCockpit.get(k), flight, cockpitRoster, airportBuckets, airports);
            }
            List<Long> fullRoster = new ArrayList<>();
            for (int k = 0; k < cabinRoster.size(); k++)
                fullRoster.add(cabinRoster.get(k).getId());
            for (int k = 0; k < cockpitRoster.size(); k++)
                fullRoster.add(cockpitRoster.get(k).getId());
            try {
                createFlightRoster(flight.getId(), fullRoster, cabinRoster.size() == cabinReq && cockpitRoster.size() == cockpitReq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getReplacement(Flight flight, int jobId) {
        List<User> result = new ArrayList<>();
        try {
            result = userBean.getUsersAtAirportWithJob(flight.getAircraftAssignment().getRoute().getOrigin().getId(), jobId);
            System.out.println(result);
        } catch (Exception e) {

        }
        return result;
    }

    private FlightBid getBidFromUser(User user, List<FlightBid> flightBids) throws NotFoundException {
        for (int i = 0; i < flightBids.size(); i++) {
            if (flightBids.get(i).getBidder().equals(user))
                return flightBids.get(i);
        }
        throw new NotFoundException();
    }

    private void addToRoster(HypoCrew hypoCrew, Flight flight, List<User> roster, List<List<HypoCrew>> airportBuckets, List<Airport> airports) {
        roster.add(hypoCrew.user);
        //Set users ready date, location, and have to move to another bucket
        hypoCrew.readyTime = Utils.minutesLater(flight.getArrivalTime(), 60 * 30);
        hypoCrew.location = flight.getAircraftAssignment().getRoute().getDestination();
        for (int i = 0; i < airportBuckets.size(); i++) {
            if (airportBuckets.get(i).indexOf(hypoCrew) != -1) {
                airportBuckets.get(i).remove(hypoCrew);
                break;
            }
        }
        airportBuckets.get(airports.indexOf(hypoCrew.location)).add(hypoCrew);
    }
}
