package MAS.ManagedBean;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
public class TempTestBean {

    @EJB
    BookFlightBean bookFlightBean;
    @EJB
    BookingClassBean bookingClassBean;
    @EJB
    FlightSearchBean flightSearchBean;
    @EJB
    PNRBean pnrBean;

    public void book() throws Exception {
        ArrayList<BookingClass> b = new ArrayList<>();
        ArrayList<String> p = new ArrayList<>();
        b.add(bookingClassBean.getBookingClass(161));
        b.add(bookingClassBean.getBookingClass(202));
        b.add(bookingClassBean.getBookingClass(453));
        b.add(bookingClassBean.getBookingClass(476));
        p.add("TAN/KELLY");
        PNR pnr = bookFlightBean.bookFlights(b, p);
        pnrBean.setSpecialServiceRequest(pnr, pnrBean.getPassengerNumber(pnr, "TAN/KELLY"), Constants.SSR_ACTION_CODE_FFP, "B6/12345655");
        pnrBean.updatePNR(pnr);
    }

    public void searchTest() {
        System.out.println("test!!!");
        List<List<Flight>> flights = flightSearchBean.searchFlights("SIN", "SFO", new Date());
        for (List<Flight> journey : flights) {
            Flight f = journey.get(0);
            System.out.println(f.getId() + " " + f.getAircraftAssignment().getRoute().getOrigin().getId() + " " + f.getAircraftAssignment().getRoute().getDestination().getId());
        }
    }

}
