package MAS.Bean;

import MAS.Common.Utils;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Flight;
import MAS.Entity.FlightGroup;
import MAS.Exception.NoItemsCreatedException;
import MAS.Exception.NotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Stateless(name = "FlightScheduleEJB")
public class FlightScheduleBean {
    @PersistenceContext
    EntityManager em;

    public FlightScheduleBean() {
    }

    //-----------------Flights---------------------------
    public long createFlight(String code, Date departureTime, Date arrivalTime, long aircraftAssignmentId) throws NotFoundException {
        Flight flight = new Flight();
        flight.setCode(code);
        flight.setArrivalTime(arrivalTime);
        flight.setDepartureTime(departureTime);
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, aircraftAssignmentId);
        if (aircraftAssignment == null) throw new NotFoundException();
        flight.setAircraftAssignment(aircraftAssignment);
        em.persist(flight);
        em.flush();
        return flight.getId();
    }

    public void changeFlightCode(long id, String code) throws NotFoundException {
        Flight flight = em.find(Flight.class, id);
        if (flight == null) throw new NotFoundException();
        flight.setCode(code);
        em.persist(flight);
    }

    public void changeFlightTimings(long id, Date departureTime, Date arrivalTime) throws NotFoundException {
        Flight flight = em.find(Flight.class, id);
        if (flight == null) throw new NotFoundException();
        flight.setArrivalTime(arrivalTime);
        flight.setDepartureTime(departureTime);
        em.persist(flight);
        em.flush();
    }

    public void removeFlight(long id) throws NotFoundException {
        Flight flight = em.find(Flight.class, id);
        if (flight == null) throw new NotFoundException();
        em.remove(flight);
    }

    public Flight getFlight(long id) throws NotFoundException {
        Flight flight = em.find(Flight.class, id);
        if (flight == null) throw new NotFoundException();
        return flight;
    }

    public List<Flight> getAllFlights() {
        return em.createQuery("SELECT f from Flight f", Flight.class).getResultList();
    }

    public List<Flight> findFlightByAA(long aaId) throws NotFoundException {
        AircraftAssignment aa = em.find(AircraftAssignment.class, aaId);
        if (aa == null) throw new NotFoundException();
        return em.createQuery("SELECT f from Flight f WHERE f.aircraftAssignment = :aa", Flight.class).setParameter("aa", aa).getResultList();
    }

    public String findSeatConfigOfFlight(long flightId) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        return flight.getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig();
    }

    public long createRecurringFlight(String code, long aircraftAssignmentId, String departureTime, int flightDuration, Date recurringStartDate, Date recurringEndDate, int[] recurringDays) throws NotFoundException, NoItemsCreatedException {
        AircraftAssignment aircraftAssignment = em.find(AircraftAssignment.class, aircraftAssignmentId);
        if (aircraftAssignment == null) throw new NotFoundException();
        int days = Period.between(recurringStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), recurringEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays();

        if (days < 0) {
            throw new NoItemsCreatedException();
        }

        Date currDepartureDate = null;
        Date currArrivalDate = null;

        try {
            currDepartureDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(new SimpleDateFormat("yyyy-MM-dd").format(recurringStartDate) + " " + departureTime);
            currArrivalDate = Utils.minutesLater(currDepartureDate, flightDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> recurringDaysList = new ArrayList<>();
        for (int i = 0; i < recurringDays.length; i++) {
            recurringDaysList.add(recurringDays[i]);
        }

        ArrayList<Flight> flights = new ArrayList<>();
        FlightGroup flightGroup = new FlightGroup();

        for (int i = 0; i <= days; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currDepartureDate);
            if (recurringDaysList.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                // @TODO: Check if flight can be scheduled at this time
                System.out.println(currDepartureDate);
                System.out.println(currArrivalDate);

                Flight flight = new Flight();
                flight.setCode(code);
                flight.setArrivalTime(currArrivalDate);
                flight.setDepartureTime(currDepartureDate);
                flight.setAircraftAssignment(aircraftAssignment);
                flight.setFlightGroup(flightGroup);
                flights.add(flight);

            }
            currDepartureDate.setTime(currDepartureDate.getTime() + 86400000);
            currArrivalDate.setTime(currArrivalDate.getTime() + 86400000);
        }

        if (flights.size() == 0) {
            throw new NoItemsCreatedException();
        }

        flightGroup.setFlights(flights);
        em.persist(flightGroup);
        em.flush();

        return flightGroup.getId();
    }
}
