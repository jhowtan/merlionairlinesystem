package MAS.Bean;

import MAS.Entity.BookingClass;
import MAS.Entity.FareRule;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "BookingClassEJB")
public class BookingClassBean {
    @PersistenceContext
    EntityManager em;

    public BookingClassBean() {
    }

    //-----------------Booking classes---------------------------
    public long createBookingClass(String name, int allocation, int travelClass, long fareRuleId, long flightId) throws NotFoundException {
        FareRule fareRule = em.find(FareRule.class, fareRuleId);
        Flight flight = em.find(Flight.class, flightId);
        if (fareRule == null || flight == null) throw new NotFoundException();
        BookingClass bookingClass = new BookingClass();
        bookingClass.setName(name);
        bookingClass.setTravelClass(travelClass);
        bookingClass.setAllocation(allocation);
        bookingClass.setOccupied(0);
        bookingClass.setOpen(false);
        bookingClass.setFareRule(fareRule);
        bookingClass.setFlight(flight);
        em.persist(bookingClass);
        em.flush();
        return bookingClass.getId();
    }

    public void removeBookingClass(long id) throws NotFoundException {
        BookingClass bookingClass = em.find(BookingClass.class, id);
        if (bookingClass == null) throw new NotFoundException();
        em.remove(bookingClass);
    }

    public BookingClass getBookingClass(long id) throws NotFoundException {
        BookingClass bookingClass = em.find(BookingClass.class, id);
        if (bookingClass == null) throw new NotFoundException();
        return bookingClass;
    }

    public List<BookingClass> getAllBookingClasses() {
        return em.createQuery("SELECT b from BookingClass b", BookingClass.class).getResultList();
    }

    public List<BookingClass> findBookingClassByFlight(long flightId) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        return em.createQuery("SELECT b from BookingClass b WHERE b.flight = :flight", BookingClass.class).setParameter("flight", flight).getResultList();
    }
}
