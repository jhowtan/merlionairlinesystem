package MAS.Bean;

import MAS.Common.Constants;
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
        em.getTransaction().begin();
        for (BookingClass bookingClass : bookingClasses) {
            if (bookingClass.getAllocation() < passengerNames.size()) {
                em.getTransaction().rollback();
                throw new BookingException();
            }
            bookingClass.setAllocation(bookingClass.getAllocation() - passengerNames.size());
            em.merge(bookingClass);
            SeatConfigObject seatConfigObject = new SeatConfigObject();
            seatConfigObject.parse(bookingClass.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
            int totalSeatsInClass = seatConfigObject.getSeatsInClass(bookingClass.getTravelClass());
            int seatsBookedInClass = (int) em.createQuery("SELECT COUNT(e) FROM ETicket e WHERE e.flight = :flight AND e.travelClass = :travelClass").setParameter("flight", bookingClass.getFlight()).setParameter("travelClass", bookingClass.getTravelClass()).getSingleResult();
            int seatsLeftInClass = totalSeatsInClass - seatsBookedInClass;
            if (seatsLeftInClass < passengerNames.size()) {
                em.getTransaction().rollback();
                throw new BookingException();
            }
        }
        em.getTransaction().commit();

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

        // Set passengers
        pnr.setPassengers(passengerNames);

        // Issue eTicket for each flight
        for (BookingClass bookingClass : bookingClasses) {
            for (String passengerName : passengerNames) {
                ETicket eTicket = new ETicket();
                eTicket.setBookingClass(bookingClass);
                eTicket.setFlight(bookingClass.getFlight());
                eTicket.setPassengerName(passengerName);
                em.persist(eTicket);
                em.flush();
                try {
                    SpecialServiceRequest ssr = new SpecialServiceRequest();
                    ssr.setPassengerNumber(pnrBean.getPassengerNumber(pnr, passengerName));
                    ssr.setItineraryNumber(pnrBean.getItineraryNumber(pnr, bookingClass.getFlight()));
                    ssr.setActionCode("TKNA");
                    ssr.setValue(eTicket.getId().toString());
                    SSRs.add(ssr);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        pnr.setCreated(new Date());
        pnr.setItineraries(itineraries);
        pnr.setSpecialServiceRequests(SSRs);
        em.persist(pnr);
        em.flush();
        return pnr;
    }

}
