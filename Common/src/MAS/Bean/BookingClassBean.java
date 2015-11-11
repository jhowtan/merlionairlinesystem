package MAS.Bean;

import MAS.Entity.BookingClass;
import MAS.Entity.FareRule;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "BookingClassEJB")
@LocalBean
public class BookingClassBean {
    @PersistenceContext
    EntityManager em;

    public BookingClassBean() {
    }

    //-----------------Booking classes---------------------------
    public long createBookingClass(String name, int allocation, int travelClass, long fareRuleId, long flightId, double price) throws NotFoundException {
        FareRule fareRule = em.find(FareRule.class, fareRuleId);
        Flight flight = em.find(Flight.class, flightId);
        if (fareRule == null || flight == null) throw new NotFoundException();
        return createBookingClass(name, allocation, travelClass, fareRule, flight, price, true);
    }

    public long createBookingClass(String name, int allocation, int travelClass, FareRule fareRule, Flight flight, double price, boolean shouldFlush) {
        BookingClass bookingClass = new BookingClass();
        bookingClass.setName(name);
        bookingClass.setTravelClass(travelClass);
        bookingClass.setAllocation(allocation);
        bookingClass.setOccupied(0);
        bookingClass.setOpen(true);
        bookingClass.setFareRule(fareRule);
        bookingClass.setFlight(flight);
        bookingClass.setPrice(price);
        em.persist(bookingClass);
        if (shouldFlush) {
            em.flush();
            return bookingClass.getId();
        } else {
            return -1;
        }
    }

    public void changeName(long id, String name) throws NotFoundException {
        BookingClass bookingClass = em.find(BookingClass.class, id);
        if (bookingClass == null) throw new NotFoundException();
        bookingClass.setName(name);
        em.persist(bookingClass);
    }

    public void changeOpenStatus(long id, boolean status) throws NotFoundException {
        BookingClass bookingClass = em.find(BookingClass.class, id);
        if (bookingClass == null) throw new NotFoundException();
        bookingClass.setOpen(status);
        em.persist(bookingClass);
    }
    public void changeAllocation(long id, int allocation) throws NotFoundException {
        BookingClass bookingClass = em.find(BookingClass.class, id);
        if (bookingClass == null) throw new NotFoundException();
        bookingClass.setAllocation(allocation);
        em.persist(bookingClass);
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

    public List<BookingClass> findBookingClassByFlightAndClass(long flightId, int travelClass) throws NotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight == null) throw new NotFoundException();
        return em.createQuery("SELECT b from BookingClass b WHERE b.flight = :flight AND b.travelClass = :travelClass", BookingClass.class)
                .setParameter("flight", flight)
                .setParameter("travelClass", travelClass)
                .getResultList();
    }
}
