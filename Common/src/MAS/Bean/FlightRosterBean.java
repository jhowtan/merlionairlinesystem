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

    public FlightRoster getFlightRoster(long id) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, id);
        if (flightRoster == null) throw new NotFoundException();
        return flightRoster;
    }

    public void removeFlightRoster(long id) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, id);
        if (flightRoster == null) throw new NotFoundException();
        em.remove(flightRoster);
    }

    public FlightRoster getFlightRosterForFlight(long flightId) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        FlightRoster result = em.createQuery("SELECT fr from FlightRoster fr WHERE fr.flight = :flight", FlightRoster.class)
                .setParameter("flight", flight)
                .setMaxResults(1).getResultList().get(0);
        if (result == null) throw new NotFoundException();
        return result;
    }

    public void signInFR(long flightRosterId, long userId) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, flightRosterId);
        User user = em.find(User.class, userId);
        if (flightRoster == null || user == null) throw new NotFoundException();
        List<User> signedIn = flightRoster.getSignedIn();
        if (signedIn == null) signedIn = new ArrayList<>();
        if (signedIn.indexOf(user) == -1) signedIn.add(user);
        flightRoster.setSignedIn(signedIn);
        em.persist(flightRoster);
    }

    public void signOutFR(long flightRosterId, long userId) throws NotFoundException {
        FlightRoster flightRoster = em.find(FlightRoster.class, flightRosterId);
        User user = em.find(User.class, userId);
        if (flightRoster == null || user == null) throw new NotFoundException();
        List<User> signedOut = flightRoster.getSignedOut();
        if (signedOut == null) signedOut = new ArrayList<>();
        if (signedOut.indexOf(user) == -1) signedOut.add(user);
        flightRoster.setSignedOut(signedOut);
        //@TODO: set crew current location to here
        em.persist(flightRoster);
    }

    public void allocateFlightJobs() {
        List<FlightBid> flightBids = flightBidBean.getFlightBidsWithStatus(0);
        List<Flight> flights = flightScheduleBean.getFlightWithinDate(Utils.monthStart(1), Utils.monthEnd(1));
        //Sort flights by date
        Collections.sort(flights);
        List<Airport> airports = new ArrayList<>();
        List<List<HypoCrew>> airportBuckets = new ArrayList<>();
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
        }
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            FlightRoster flightRoster = new FlightRoster();
            List<User> cabinRoster = new ArrayList<>();
            List<User> cockpitRoster = new ArrayList<>();
            Airport origin = flights.get(i).getAircraftAssignment().getRoute().getOrigin();
            Airport dest = flights.get(i).getAircraftAssignment().getRoute().getDestination();
            int cabinReq = flights.get(i).getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCabinCrewReq();
            int cockpitReq = flights.get(i).getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCockpitCrewReq();
            List<HypoCrew> available = airportBuckets.get(airports.indexOf(origin));
            List<HypoCrew> prospective = new ArrayList<>();
            for (int j = 0; j < available.size(); j++) {
                if (cabinRoster.size() == cabinReq && cockpitRoster.size() == cockpitReq) {
                    break;
                }
                else {
                    HypoCrew crew = available.get(j);
                    if (cabinRoster.size() < cabinReq && crew.user.getJob() == Constants.cabinCrewJobId) {
                        try {//if this user has chosen this flight, add
                            if (getBidFromUser(crew.user, flightBids).getFlights().indexOf(flight) != -1) {
                                prospective.add(0, crew);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //else, if user is qualified but did not choose
                        //HERE
                        //else if dont need then dont add
                    }
                    if (cockpitRoster.size() < cockpitReq && crew.user.getJob() == Constants.cockpitCrewJobId) {

                    }
                }
            }
        }
    }

    private FlightBid getBidFromUser(User user, List<FlightBid> flightBids) throws NotFoundException {
        for (int i = 0; i < flightBids.size(); i++) {
            if (flightBids.get(i).getBidder().equals(user))
                return flightBids.get(i);
        }
        throw new NotFoundException();
    }

    private void addToRoster(HypoCrew hypoCrew, Flight flight, List<User> roster, double priority) {

    }
}
