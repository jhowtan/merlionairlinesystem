package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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
        List<Flight> flights = flightScheduleBean.getFlightWithinDate(Utils.currentMonthStart(), Utils.currentMonthEnd());
        List<Airport> airports = new ArrayList<>();
        List<List<User>> airportBuckets = new ArrayList<>();
        //Initialise airport buckets
        //For each flight, choose flight attendants. If unable, mark as uncomplete
    }
}
