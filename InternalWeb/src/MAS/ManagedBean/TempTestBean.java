package MAS.ManagedBean;

import MAS.Bean.BookFlightBean;
import MAS.Bean.BookingClassBean;
import MAS.Entity.BookingClass;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;

@ManagedBean
public class TempTestBean {

    @EJB
    BookFlightBean bookFlightBean;
    @EJB
    BookingClassBean bookingClassBean;

    public void book() throws Exception {
        ArrayList<BookingClass> b = new ArrayList<>();
        ArrayList<String> p = new ArrayList<>();
        b.add(bookingClassBean.getBookingClass(55));
        p.add("LAU/WEIJIEJONATHAN");
        p.add("HO/ZEWEIDARYL");
        p.add("TAN/JYEHOWJONATHAN");
        bookFlightBean.bookFlights(b, p);
    }

}
