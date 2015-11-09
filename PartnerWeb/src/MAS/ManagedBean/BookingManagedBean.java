package MAS.ManagedBean;

import MAS.WebService.DirectDistributionSystem.DirectDistributionSystem;
import MAS.WebService.DirectDistributionSystem.DirectDistributionSystemService;
import MAS.WebService.DirectDistributionSystem.WsFlightResultArray;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@ManagedBean
@ViewScoped
public class BookingManagedBean {

    DirectDistributionSystem dds;

    private String origin;
    private String destination;
    private Date departureDate;
    private Date returnDate;
    private int passengers = 1;
    private int travelClass = 3;

    private int step = 1;

    // Step 2
    List<WsFlightResultArray> outboundSearchResult;
    List<WsFlightResultArray> returnSearchResult;

    public void nextStep() throws DatatypeConfigurationException {
        step++;
        if (dds == null) {
            dds = new DirectDistributionSystemService().getDirectDistributionSystemPort();
        }
        switch (step) {
            case 2:
                int travelDuration = 0;
                if (returnDate != null) {
                    travelDuration = (int) ((returnDate.getTime() - departureDate.getTime()) / 1000 / 60 / 60 / 24);
                    GregorianCalendar returnDateG = new GregorianCalendar();
                    returnDateG.setTime(returnDate);
                    XMLGregorianCalendar returnDateXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(returnDateG );
                    returnSearchResult = dds.searchAvailableFlights(destination, origin, returnDateXML, passengers, travelClass, travelDuration);
                }
                GregorianCalendar departureDateG = new GregorianCalendar();
                departureDateG.setTime(departureDate);
                XMLGregorianCalendar departureDateXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(departureDateG);
                outboundSearchResult = dds.searchAvailableFlights(origin, destination, departureDateXML, passengers, travelClass, travelDuration);
                break;
        }
    }

    public void prevStep() {
        step--;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<WsFlightResultArray> getOutboundSearchResult() {
        return outboundSearchResult;
    }

    public void setOutboundSearchResult(List<WsFlightResultArray> outboundSearchResult) {
        this.outboundSearchResult = outboundSearchResult;
    }

    public List<WsFlightResultArray> getReturnSearchResult() {
        return returnSearchResult;
    }

    public void setReturnSearchResult(List<WsFlightResultArray> returnSearchResult) {
        this.returnSearchResult = returnSearchResult;
    }
}
