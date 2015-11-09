package MAS.WebServiceHelpers;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class WSFlightResult {
    public List<WSBookingClass> bookingClasses;
    public String flightNumber;
}
