package MAS.Bean;

import MAS.Common.Cabin;
import MAS.Common.Constants;
import MAS.Common.SeatConfigObject;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NoItemsCreatedException;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateless(name = "FlightScheduleEJB")
@LocalBean
public class FlightScheduleBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    FareRuleBean fareRuleBean;
    @EJB
    BookingClassBean bookingClassBean;
    @EJB
    CostsBean costsBean;
    @EJB
    AttributesBean attributesBean;
    @EJB
    FleetBean fleetBean;

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

    public long createFlight(String code, Date departureTime, Date arrivalTime, long aircraftAssignmentId, boolean createBkingClass) throws NotFoundException {
        long flightId = createFlight(code, departureTime, arrivalTime, aircraftAssignmentId);
        if (createBkingClass)
            createDefaultBookingClasses(flightId);

        return  flightId;
    }

    private void createDefaultBookingClasses(long flightId) throws  NotFoundException{
        Flight flight = em.find(Flight.class, flightId);
        FareRule fareN = fareRuleBean.getFareRuleByName(Constants.FARE_NORMAL);
        FareRule fareE = fareRuleBean.getFareRuleByName(Constants.FARE_EARLY);
        FareRule fareL = fareRuleBean.getFareRuleByName(Constants.FARE_LATE);
        FareRule fareD = fareRuleBean.getFareRuleByName(Constants.FARE_DOUBLE);
        FareRule fareEx = fareRuleBean.getFareRuleByName(Constants.FARE_EXPENSIVE);
        SeatConfigObject seatConfigObject = new SeatConfigObject();
        seatConfigObject.parse(flight.getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        double costPerSeat = costsBean.calculateCostPerSeat(flightId);

        for (int i = 0; i < Cabin.TRAVEL_CLASSES.length; i++) {
            int seatsInClass = seatConfigObject.getSeatsInClass(i);
            int seatsLeft = seatsInClass;
            double cabinPrice = costPerSeat * Constants.TRAVEL_CLASS_PRICE_MULTIPLIER[i];

            bookingClassBean.createBookingClass(Constants.BOOKING_CLASS_NAMES[i * 5 + 0], seatsLeft, i, fareEx.getId(), flightId, Utils.makeNiceMoney(cabinPrice * fareEx.getPriceMul()));
            seatsLeft -= costsBean.getSeatTraffic(seatsInClass, cabinPrice, Utils.makeNiceMoney(cabinPrice * 1.5));
            bookingClassBean.createBookingClass(Constants.BOOKING_CLASS_NAMES[i * 5 + 1], seatsLeft, i, fareL.getId(), flightId, Utils.makeNiceMoney(cabinPrice * fareL.getPriceMul()));
            seatsLeft -= costsBean.getSeatTraffic(seatsInClass, cabinPrice, Utils.makeNiceMoney(cabinPrice * 1.35));
            bookingClassBean.createBookingClass(Constants.BOOKING_CLASS_NAMES[i * 5 + 2], seatsLeft, i, fareN.getId(), flightId, Utils.makeNiceMoney(cabinPrice * fareN.getPriceMul()));
            bookingClassBean.createBookingClass(Constants.BOOKING_CLASS_NAMES[i * 5 + 3], seatsLeft, i, fareD.getId(), flightId, Utils.makeNiceMoney(cabinPrice * fareD.getPriceMul()));
            seatsLeft -= costsBean.getSeatTraffic(seatsInClass, cabinPrice, Utils.makeNiceMoney(cabinPrice * 1));
            bookingClassBean.createBookingClass(Constants.BOOKING_CLASS_NAMES[i * 5 + 4], seatsLeft, i, fareE.getId(), flightId, Utils.makeNiceMoney(cabinPrice * fareE.getPriceMul()));
        }
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

    public List<Flight> getFlightOfAc(long acId) throws NotFoundException {
        Aircraft aircraft = em.find(Aircraft.class, acId);
        if (aircraft == null) throw new NotFoundException();
        return em.createQuery("SELECT f from Flight f WHERE f.aircraftAssignment.aircraft = :aircraft", Flight.class).setParameter("aircraft", aircraft).getResultList();
    }

    public List<Flight> getFlightWithinDate(Date start, Date end) {
        return em.createQuery("SELECT f from Flight f WHERE f.departureTime >= :start AND f.departureTime <= :end", Flight.class)
                .setParameter("start", start, TemporalType.TIMESTAMP)
                .setParameter("end", end, TemporalType.TIMESTAMP).getResultList();
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

        long days = TimeUnit.DAYS.convert(recurringEndDate.getTime() - recurringStartDate.getTime(), TimeUnit.MILLISECONDS);

        if (days < 0) {
            throw new NoItemsCreatedException();
        }

        Date currDepartureDate = null;
        Date currArrivalDate = null;

        try {
            currDepartureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(new SimpleDateFormat("yyyy-MM-dd").format(recurringStartDate) + " " + departureTime);
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

                Flight flight = new Flight();
                flight.setCode(code);
                flight.setArrivalTime((Date) currArrivalDate.clone());
                flight.setDepartureTime((Date) currDepartureDate.clone());
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

    public long createRecurringFlight(String code, long aircraftAssignmentId, String departureTime, int flightDuration, Date recurringStartDate, Date recurringEndDate, int[] recurringDays, boolean createBkingClass) throws NotFoundException, NoItemsCreatedException {
        long flightGrpId = createRecurringFlight(code, aircraftAssignmentId, departureTime, flightDuration, recurringStartDate, recurringEndDate, recurringDays);

        if (createBkingClass) {
            FlightGroup flightGroup = em.find(FlightGroup.class, flightGrpId);
            for (int i = 0; i < flightGroup.getFlights().size(); i++) {
                createDefaultBookingClasses(flightGroup.getFlights().get(i).getId());
            }
        }

        return flightGrpId;
    }


    public FlightGroup getFlightGroup(long id) throws NotFoundException {
        FlightGroup flightGroup = em.find(FlightGroup.class, id);
        if (flightGroup == null) throw new NotFoundException();
        return flightGroup;
    }

    public void removeFlightFromFlightGroup(long id) throws NotFoundException {
        Flight flight = em.find(Flight.class, id);
        if (flight == null) throw new NotFoundException();
        flight.setFlightGroup(null);
        em.persist(flight);
    }

    public void updateRecurringFlight(long flightGroupId, String code, String departureTime, int flightDuration) throws NotFoundException {
        FlightGroup flightGroup = getFlightGroup(flightGroupId);
        for (Flight flight : flightGroup.getFlights()) {
            flight.setCode(code);
            try {
                flight.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(new SimpleDateFormat("yyyy-MM-dd").format(flight.getDepartureTime()) + " " + departureTime));
                flight.setArrivalTime(Utils.minutesLater(flight.getDepartureTime(), flightDuration));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            em.persist(flight);
        }
    }

    public List<Flight> findDepartingFlightsByAirport(Airport baseAirport) {
        return em.createQuery("SELECT f FROM Flight f, AircraftAssignment aa, Route r " +
                "WHERE f.aircraftAssignment = aa AND aa.route = r AND r.origin = :baseAirport AND f.departureTime > current_timestamp " +
                "AND f.departureTime < :date", Flight.class)
                .setParameter("baseAirport", baseAirport)
                .setParameter("date", Utils.hoursFromNow(24), TemporalType.TIMESTAMP)
                .getResultList();
    }

    public List<ETicket> getETicketsForFlight(Flight flight) {
        return em.createQuery("SELECT et FROM ETicket et WHERE et.flight = :flight", ETicket.class)
                .setParameter("flight", flight)
                .getResultList();
    }

    public List<Integer> getSeatsTakenForFlight(Flight flight) {
        ArrayList<Integer> seatsTaken = new ArrayList<>();
        List<ETicket> eTickets = getETicketsForFlight(flight);
        for (ETicket eTicket : eTickets) {
            if (eTicket.getSeatNumber() != -1) {
                seatsTaken.add(eTicket.getSeatNumber());
            }
        }
        return seatsTaken;
    }

    public ETicket getETicket(long id) throws NotFoundException {
        ETicket eTicket = em.find(ETicket.class, id);
        if (eTicket == null) throw new NotFoundException();
        return eTicket;
    }

    public List<ETicket> getRelatedETickets(ETicket eTicket) {
        return em.createQuery("SELECT et FROM ETicket et WHERE et.pnr = :pnr AND et.flight = :flight", ETicket.class)
                .setParameter("flight", eTicket.getFlight())
                .setParameter("pnr", eTicket.getPnr())
                .getResultList();
    }

    public void updateETicket(ETicket eTicket) throws NotFoundException {
        if (em.find(ETicket.class, eTicket.getId()) == null) throw new NotFoundException();
        em.merge(eTicket);
    }

    public long createBaggageItem(double weight) {
        Baggage baggage = new Baggage();
        baggage.setWeight(weight);
        em.persist(baggage);
        em.flush();
        return baggage.getId();
    }

    public Baggage getBaggageItem(long id) throws NotFoundException {
        Baggage baggage = em.find(Baggage.class, id);
        if (baggage == null) throw new NotFoundException();
        return baggage;
    }

    public void updateSingleFlight(Flight flight) throws NotFoundException {
        if (em.find(Flight.class, flight.getId()) == null) throw new NotFoundException();
        em.merge(flight);
    }

    public boolean isSeatAvailable(Flight flight, int seat) {
        return em.createQuery("SELECT COUNT(et) FROM ETicket et WHERE et.flight = :flight AND et.seatNumber = :seatNumber", Long.class)
                .setParameter("flight", flight)
                .setParameter("seatNumber", seat)
                .getSingleResult() == 0;
    }

}
