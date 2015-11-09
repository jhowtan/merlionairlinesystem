package MAS.ManagedBean;

import MAS.WebService.DirectDistributionSystem.DirectDistributionSystem;
import MAS.WebService.DirectDistributionSystem.DirectDistributionSystemService;
import MAS.WebService.DirectDistributionSystem.WsFlightResultArray;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

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
    List<Long> selectedBookingClasses;

    // Step 3
    private List<PassengerDetails> passengersDetails;

    public void nextStep() throws DatatypeConfigurationException {
        step++;
        if (dds == null) {
            dds = new DirectDistributionSystemService().getDirectDistributionSystemPort();
        }
        switch (step) {
            case 2:
                passengersDetails = new ArrayList<>();
                for (int i = 0; i < passengers; i++) {
                    passengersDetails.add(new PassengerDetails());
                }
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
                selectedBookingClasses = new ArrayList<>();
                break;
        }
    }

    public void toggleSelectBookingClass(long id) {
        if (selectedBookingClasses.contains(id)) {
            selectedBookingClasses.remove(id);
        } else {
            selectedBookingClasses.add(id);
        }
    }

    public boolean isBookingClassSelected(long id) {
        return selectedBookingClasses.contains(id);
    }

    public LinkedHashMap<String, String> getFFPAllianceList() {
        String[] FFP_ALLIANCE_LIST_CODE = {"MA", "A3", "AC", "CA", "AI", "NZ", "NH", "OZ", "AV", "O6", "CM", "MS", "ET", "BR", "B6", "LH", "SK", "SQ", "SA", "JJ", "TP", "TG", "TK", "UA", "VX", "VS", "VA", "UK"};
        String[] FFP_ALLIANCE_LIST_NAME = {"Merlion Airlines - MerlionFlyer Elite", "Aegean Airlines - Miles&Bonus", "Air Canada - Aeroplan", "Air China / Shenzhen Airlines - PhoenixMiles", "Air India - Flying Returns", "Air New Zealand - AirPoints", "ANA - ANA Mileage Club", "Asiana Airlines - Asiana Club", "Avianca - LifeMiles", "Avianca Brazil - Amigo", "Copa Airlines - ConnectMiles", "EgyptAir - EgyptAir Plus", "Ethiopian Airlines - ShebaMiles", "EVA Air - Infinity MileageLands", "JetBlue - TrueBlue", "Lufthansa - Miles & More", "Scandinavian Airlines - EuroBonus", "Singapore Airlines - KrisFlyer", "South African Airways - Voyager", "TAM Airlines - Fidelidade", "TAP Portugal - Victoria", "THAI - Royal Orchid Plus", "Turkish Airlines - Miles&Smiles", "United - MileagePlus", "Virgin America - Elevate", "Virgin Atlantic - Flying Club", "Virgin Australia - Velocity", "Vistara - Club Vistara"};
        LinkedHashMap<String, String> ffpAllianceList = new LinkedHashMap<>();
        for (int i = 0; i < FFP_ALLIANCE_LIST_CODE.length; i++) {
            ffpAllianceList.put(FFP_ALLIANCE_LIST_NAME[i], FFP_ALLIANCE_LIST_CODE[i]);
        }
        return ffpAllianceList;
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

    public List<Long> getSelectedBookingClasses() {
        return selectedBookingClasses;
    }

    public void setSelectedBookingClasses(List<Long> selectedBookingClasses) {
        this.selectedBookingClasses = selectedBookingClasses;
    }

    public List<PassengerDetails> getPassengersDetails() {
        return passengersDetails;
    }

    public void setPassengersDetails(List<PassengerDetails> passengersDetails) {
        this.passengersDetails = passengersDetails;
    }

    public class PassengerDetails {
        private String firstName;
        private String lastName;
        private String ffpProgram;
        private String ffpNumber;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFfpProgram() {
            return ffpProgram;
        }

        public void setFfpProgram(String ffpProgram) {
            this.ffpProgram = ffpProgram;
        }

        public String getFfpNumber() {
            return ffpNumber;
        }

        public void setFfpNumber(String ffpNumber) {
            this.ffpNumber = ffpNumber;
        }
    }
}
