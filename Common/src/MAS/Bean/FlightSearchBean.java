package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.Airport;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Stateless(name = "FlightSearchEJB")
public class FlightSearchBean {

    @PersistenceContext
    EntityManager em;
    @EJB
    RouteBean routeBean;

    public FlightSearchBean() {
    }

    public List<Flight> searchDirectFlights(String origin, String destination, Date date) {
        try {
            Airport originAirport = routeBean.getAirport(origin);
            Airport destinationAirport = routeBean.getAirport(destination);
            Date dayStart = Utils.addTimeToDate(date, "00:00");
            Date dayEnd = Utils.minutesLater(dayStart, 1440);

            List<Flight> directFlights = em.createQuery("SELECT f from  Flight f WHERE f.aircraftAssignment.route.origin = :origin AND f.aircraftAssignment.route.destination = :destination AND f.departureTime >= :dayStart AND f.departureTime < :dayEnd", Flight.class)
                    .setParameter("origin", originAirport)
                    .setParameter("destination", destinationAirport)
                    .setParameter("dayStart", dayStart)
                    .setParameter("dayEnd", dayEnd)
                    .getResultList();
            return directFlights;
        } catch (NotFoundException e) {
            return new ArrayList<Flight>();
        }
    }

    public List<List<Flight>> searchFlights(String origin, String destination, Date date) {
        try {
            List<List<Flight>> results = new ArrayList<>();

            Airport originAirport = routeBean.getAirport(origin);
            Airport destinationAirport = routeBean.getAirport(destination);
            Date dayStart = Utils.addTimeToDate(date, "00:00");
            Date dayEnd = Utils.minutesLater(dayStart, 1440);

            List<Flight> directFlights = em.createQuery("SELECT f from  Flight f WHERE f.aircraftAssignment.route.origin = :origin AND f.aircraftAssignment.route.destination = :destination AND f.departureTime >= :dayStart AND f.departureTime < :dayEnd", Flight.class)
                    .setParameter("origin", originAirport)
                    .setParameter("destination", destinationAirport)
                    .setParameter("dayStart", dayStart)
                    .setParameter("dayEnd", dayEnd)
                    .getResultList();

            if (directFlights.size() != 0) {
                for (Flight flight : directFlights) {
                    results.add(Collections.singletonList(flight));
                }
                return results;
            }

            List<Flight[]> oneStopFlights = em.createQuery("SELECT f1, f2 from  Flight f1, Flight f2 " +
                    "WHERE f1.aircraftAssignment.route.origin = :origin " +
                    "AND f1.aircraftAssignment.route.destination = f2.aircraftAssignment.route.origin " +
                    "AND f2.aircraftAssignment.route.destination = :destination " +
                    "AND f1.departureTime >= :dayStart AND f1.departureTime < :dayEnd " +
                    "AND f2.departureTime > f1.arrivalTime " +
                    "AND f2.departureTime < FUNCTION('ADDDATE', f1.arrivalTime, 1)")
                    .setParameter("origin", originAirport)
                    .setParameter("destination", destinationAirport)
                    .setParameter("dayStart", dayStart)
                    .setParameter("dayEnd", dayEnd)
                    .getResultList();

            for (int i = 0; i < oneStopFlights.size(); i++) {
                results.add(Arrays.asList(oneStopFlights.get(i)));
            }
            return results;

        } catch (NotFoundException e) {
            return new ArrayList<List<Flight>>();
        }
    }

}
