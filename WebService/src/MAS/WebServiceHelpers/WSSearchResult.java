package MAS.WebServiceHelpers;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class WSSearchResult {
    @XmlElementWrapper(name="flight_results")
    public List<WSFlightResult> flightResults;
}
