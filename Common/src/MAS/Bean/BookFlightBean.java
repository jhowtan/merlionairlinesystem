package MAS.Bean;

import MAS.Common.SeatConfigObject;
import MAS.Entity.*;
import MAS.Exception.BookingException;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "BookFlightBeanEJB")
public class BookFlightBean {
    @PersistenceContext
    private EntityManager em;
    @EJB
    PNRBean pnrBean;

    public BookFlightBean() {
    }

    public PNR bookFlights(List<BookingClass> bookingClasses, List<String> passengerNames) throws BookingException {
        // Ensure enough seats available on all booking class and flights before proceeding
        for (BookingClass bookingClass : bookingClasses) {
            if (bookingClass.getAllocation() - bookingClass.getOccupied() < passengerNames.size()) {
                throw new BookingException("Not enough seats on booking class.");
            }
            if (!bookingClass.isOpen()) {
                throw new BookingException("Booking class not open!");
            }
            SeatConfigObject seatConfigObject = new SeatConfigObject();
            seatConfigObject.parse(bookingClass.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
            int totalSeatsInClass = seatConfigObject.getSeatsInClass(bookingClass.getTravelClass());
            long seatsBookedInClass = (long) em.createQuery("SELECT COUNT(e) FROM ETicket e WHERE e.flight = :flight AND e.travelClass = :travelClass").setParameter("flight", bookingClass.getFlight()).setParameter("travelClass", bookingClass.getTravelClass()).getSingleResult();
            long seatsLeftInClass = totalSeatsInClass - seatsBookedInClass;
            if (seatsLeftInClass < passengerNames.size()) {
                throw new BookingException("Not enough seats in travel class.");
            }
        }

        // Subtract seats from booking class
        for (BookingClass bookingClass : bookingClasses) {
            bookingClass.setOccupied(bookingClass.getOccupied() + passengerNames.size());
            em.merge(bookingClass);
        }

        PNR pnr = new PNR();
        List<Itinerary> itineraries = new ArrayList<>();
        List<SpecialServiceRequest> SSRs = new ArrayList<>();

        // Populate itinerary
        for (BookingClass bookingClass : bookingClasses) {
            Flight flight = bookingClass.getFlight();
            Itinerary itinerary = new Itinerary();
            itinerary.setOrigin(flight.getAircraftAssignment().getRoute().getOrigin().getId());
            itinerary.setDestination(flight.getAircraftAssignment().getRoute().getDestination().getId());
            itinerary.setDepartureDate(flight.getDepartureTime());
            itinerary.setArrivalDate(flight.getArrivalTime());
            itinerary.setBookingClass(bookingClass.getName());
            itinerary.setFlightCode(flight.getCode());
            itineraries.add(itinerary);
        }
        pnr.setItineraries(itineraries);

        // Set passengers
        pnr.setPassengers(passengerNames);

        ArrayList<ETicket> eTickets = new ArrayList<>();

        // Issue eTicket for each flight
        for (BookingClass bookingClass : bookingClasses) {
            for (String passengerName : passengerNames) {
                ETicket eTicket = new ETicket();
                eTicket.setBookingClass(bookingClass);
                eTicket.setFlight(bookingClass.getFlight());
                eTicket.setPassengerName(passengerName);
                eTicket.setTravelClass(bookingClass.getTravelClass());
                em.persist(eTicket);
                em.flush();
                try {
                    SpecialServiceRequest ssr = new SpecialServiceRequest();
                    ssr.setPassengerNumber(pnrBean.getPassengerNumber(pnr, passengerName));
                    ssr.setItineraryNumber(pnrBean.getItineraryNumber(pnr, bookingClass.getFlight().getCode()));
                    ssr.setActionCode("TKNA");
                    ssr.setValue(eTicket.getId().toString());
                    SSRs.add(ssr);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                eTickets.add(eTicket);
            }
        }
        pnr.setCreated(new Date());
        pnr.setSpecialServiceRequests(SSRs);
        em.persist(pnr);
        em.flush();
        for (ETicket eTicket : eTickets) {
            eTicket.setPnr(pnr);
            em.persist(eTicket);
        }
        return pnr;
    }

}
