package MAS.WebServiceHelpers;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement
public class WSFlightResult {
    public ArrayList<WSBookingClass> bookingClasses;
    public long id;
    public String flightNumber;
    public Date depatureTime;
    public Date arrivalTIme;
    public String orign;
    public String destination;
}
