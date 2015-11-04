package MAS.Bean;

import MAS.Common.FlightSearchItem;
import MAS.Common.FlightSearchResult;
import MAS.Common.Utils;
import MAS.Entity.Airport;
import MAS.Entity.BookingClass;
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
    @EJB
    BookFlightBean bookFlightBean;
    @EJB
    BookingClassBean bookingClassBean;

    public FlightSearchBean() {
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
            return new ArrayList<>();
        }
    }

    public List<FlightSearchResult> searchAvailableFlights(String origin, String destination, Date date, int passengerCount, int travelClass, int travelDuration) {
        ArrayList<FlightSearchResult> flightSearchResults = new ArrayList<>();
        List<List<Flight>> flightPairs = searchFlights(origin, destination, date);
        for (List<Flight> flightPair : flightPairs) {
            ArrayList<FlightSearchItem> flightSearchItems = new ArrayList<>();
            Boolean unavailable = false;
            for (Flight flight : flightPair) {
                if (bookFlightBean.seatsLeft(flight, travelClass) < passengerCount) {
                    unavailable = true;
                    break;
                }
                FlightSearchItem flightSearchItem = new FlightSearchItem(flight);
                try {
                    for (BookingClass bookingClass : bookingClassBean.findBookingClassByFlight(flight.getId())) {
                        if (!bookingClass.isOpen())
                            continue;
                        // Check fare rules
                        if (bookingClass.getFareRule().getMinimumPassengers() > passengerCount)
                            continue;
                        if (bookingClass.getFareRule().getMinimumStay() != 0 && bookingClass.getFareRule().getMinimumStay() > travelDuration)
                            continue;
                        if (bookingClass.getFareRule().getMaximumStay() != 0 && bookingClass.getFareRule().getMaximumStay() < travelDuration)
                            continue;
                        if (Utils.hoursFromNow(24 * bookingClass.getFareRule().getAdvancePurchase()).after(flight.getDepartureTime()))
                            continue;
                        flightSearchItem.addBookingClass(bookingClass);
                    }
                    if (flightSearchItem.getBookingClasses().size() == 0) {
                        unavailable = true;
                        break;
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                flightSearchItems.add(flightSearchItem);
            }
            if (unavailable) {
                continue;
            }
            FlightSearchResult flightSearchResult = new FlightSearchResult(flightSearchItems);
            flightSearchResults.add(flightSearchResult);
        }
        return flightSearchResults;
    }
}
