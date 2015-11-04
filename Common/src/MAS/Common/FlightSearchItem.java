package MAS.Common;

import MAS.Entity.BookingClass;
import MAS.Entity.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchItem {
    private final Flight flight;
    private final List<BookingClass> bookingClasses;
    private BookingClass cheapestBookingClass;

    public FlightSearchItem(Flight flight) {
        this.flight = flight;
        bookingClasses = new ArrayList<>();
    }

    public void addBookingClass(BookingClass bookingClass) {
        if (cheapestBookingClass == null) {
            cheapestBookingClass = bookingClass;
        } else if (bookingClass.getPrice() < cheapestBookingClass.getPrice()) {
            cheapestBookingClass = bookingClass;
        }
    }

    public Flight getFlight() {
        return flight;
    }

    public BookingClass getCheapestBookingClass() {
        return cheapestBookingClass;
    }

    public List<BookingClass> getBookingClasses() {
        return bookingClasses;
    }
}
