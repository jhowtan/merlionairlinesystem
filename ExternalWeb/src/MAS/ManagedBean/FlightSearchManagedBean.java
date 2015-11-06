package MAS.ManagedBean;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Common.FlightSearchItem;
import MAS.Common.FlightSearchResult;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.*;

@ManagedBean
@ViewScoped
public class FlightSearchManagedBean {

    @EJB
    RouteBean routeBean;
    @EJB
    FlightSearchBean flightSearchBean;
    @EJB
    BookFlightBean bookFlightBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    PNRBean pnrBean;
    @EJB
    FFPBean ffpBean;
    @EJB
    CustomerLogBean customerLogBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private int step = 1;

    private List<Airport> airports;
    private String origin;
    private String destination;

    private Date departureDate;
    private Date returnDate;
    private int passengers = 1;
    private int travelClass = 3;
    private boolean roundTrip = true;

    // Step 2
    private List<FlightSearchResult> outboundSearchResult;
    private List<FlightSearchResult> returnSearchResult;
    private FlightSearchResult selectedOutboundSearchResult;
    private FlightSearchResult selectedReturnSearchResult;

    // Step 3
    private List<FlightSearchItem> selectedFlightSearchItems;
    private HashMap<Integer, BookingClass> selectedBookingClasses;

    // Step 4
    private double totalPricePerPerson;
    private double totalPrice;
    private List<PassengerDetails> passengersDetails;
    private String paymentName;
    private String paymentCard;
    private int milesRedeemed;

    // Step 5
    private PNR pnr;

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
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

    @PostConstruct
    public void init() {
        setAirports(routeBean.getAllAirports());
    }

    public boolean canRedeemMiles() {
        return authManagedBean.isAuthenticated();
    }

    public double getTotalPayable() {
        return Math.max(0, (totalPrice * 100 - milesRedeemed * Constants.MILES_TO_CENTS) / 100);
    }

    public int maxMilesRedeemable() {
        return Math.min(authManagedBean.retrieveCustomer().getMiles(), (int) Math.ceil(totalPrice * 100 / Constants.MILES_TO_CENTS));
    }

    public LinkedHashMap<String, String> getFFPAllianceList() {
        LinkedHashMap<String, String> ffpAllianceList = new LinkedHashMap<>();
        for (int i = 0; i < Constants.FFP_ALLIANCE_LIST_CODE.length; i++) {
            ffpAllianceList.put(Constants.FFP_ALLIANCE_LIST_NAME[i], Constants.FFP_ALLIANCE_LIST_CODE[i]);
        }
        return ffpAllianceList;
    }

    public List<Airport> getOrigins() {
        ArrayList<Airport> origins = new ArrayList<>();
        List<Route> routes = routeBean.getAllRoutes();
        for (Route r : routes) {
            if (!origins.contains(r.getOrigin()))
               origins.add(r.getOrigin());
        }
        Collections.sort(origins, new AirportComparator());
        return origins;
    }

    public List<FlightSearchResult> getOutboundResults() {
        int travelDuration = 0;
        if (roundTrip) {
            travelDuration = (int) ((returnDate.getTime() - departureDate.getTime()) / 1000 / 60 / 60 / 24);
        }
        return flightSearchBean.searchAvailableFlights(origin, destination, departureDate, passengers, travelClass, travelDuration);
    }

    public List<FlightSearchResult> getReturnResults() {
        int travelDuration = 0;
        if (roundTrip) {
            travelDuration = (int) ((returnDate.getTime() - departureDate.getTime()) / 1000 / 60 / 60 / 24);
        }
        return flightSearchBean.searchAvailableFlights(destination, origin, returnDate, passengers, travelClass, travelDuration);
    }

    public void selectOutboundSearchResult(int item) {
        if (item == -1) {
            selectedOutboundSearchResult = null;
            return;
        }
        selectedOutboundSearchResult = outboundSearchResult.get(item);
    }

    public void selectReturnSearchResult(int item) {
        if (item == -1) {
            selectedReturnSearchResult = null;
            return;
        }
        selectedReturnSearchResult = returnSearchResult.get(item);
    }

    public void selectBookingClass(int flightItem, int bookingClassItem) {
        if (bookingClassItem == -1) {
            selectedBookingClasses.remove(flightItem);
            return;
        }
        selectedBookingClasses.put(flightItem, selectedFlightSearchItems.get(flightItem).getBookingClasses().get(bookingClassItem));
    }

    public boolean isStep2Valid() {
        if (selectedOutboundSearchResult == null) return false;
        if (selectedReturnSearchResult == null && roundTrip) return false;
        return true;
    }

    public boolean isStep3Valid() {
        return selectedBookingClasses.size() == selectedFlightSearchItems.size();
    }

    public void nextStep() {
        step++;
        switch (step) {
            case 2:
                selectedOutboundSearchResult = null;
                selectedReturnSearchResult = null;
                outboundSearchResult = getOutboundResults();
                returnSearchResult = null;
                if (roundTrip) {
                    returnSearchResult = getReturnResults();
                }
                passengersDetails = new ArrayList<>();
                for (int i = 0; i < passengers; i++) {
                    if (authManagedBean.isAuthenticated() && i == 0) {
                        PassengerDetails p = new PassengerDetails();
                        p.firstName = authManagedBean.retrieveCustomer().getFirstName();
                        p.lastName = authManagedBean.retrieveCustomer().getLastName();
                        p.ffpNumber = String.valueOf(authManagedBean.getCustomerId());
                        p.ffpProgram = "MA";
                        passengersDetails.add(p);
                    } else {
                        passengersDetails.add(new PassengerDetails());
                    }
                }
                break;
            case 3:
                selectedFlightSearchItems = new ArrayList<>();
                selectedFlightSearchItems.addAll(selectedOutboundSearchResult.getFlightSearchItems());
                if (roundTrip) {
                    selectedFlightSearchItems.addAll(selectedReturnSearchResult.getFlightSearchItems());
                }
                selectedBookingClasses = new HashMap<>();
                break;
            case 4:
                totalPricePerPerson = 0;
                for (BookingClass bookingClass : selectedBookingClasses.values()) {
                    totalPricePerPerson += bookingClass.getPrice();
                }
                totalPrice = totalPricePerPerson * passengers;
                milesRedeemed = 0;
                break;
            case 5:
                try {
                    pnr = null;
                    ArrayList<BookingClass> b = new ArrayList<>(selectedBookingClasses.values());
                    ArrayList<String> p = new ArrayList<>();
                    for (PassengerDetails passengerDetails : passengersDetails) {
                        p.add(passengerDetails.lastName.toUpperCase() + "/" + passengerDetails.firstName.toUpperCase());
                    }
                    pnr = bookFlightBean.bookFlights(b, p);
                    for (PassengerDetails passengerDetails : passengersDetails) {
                        if (passengerDetails.ffpNumber.equals("")) continue;
                        pnrBean.setSpecialServiceRequest(pnr, pnrBean.getPassengerNumber(pnr, passengerDetails.lastName.toUpperCase() + "/" + passengerDetails.firstName.toUpperCase()), Constants.SSR_ACTION_CODE_FFP, passengerDetails.ffpProgram + "/" + passengerDetails.ffpNumber);
                    }
                    pnrBean.updatePNR(pnr);
                    for (String passenger : pnr.getPassengers()) {
                        int passengerNum = pnrBean.getPassengerNumber(pnr, passenger);
                        String ffpNumber = null;
                        for (PassengerDetails passengerDetails : passengersDetails) {
                            if (passenger.toUpperCase().equals(passengerDetails.lastName.toUpperCase() + "/" + passengerDetails.firstName.toUpperCase())) {
                                if (passengerDetails.ffpNumber != null && !passengerDetails.ffpNumber.equals("")) {
                                    ffpNumber = passengerDetails.getFfpProgram() + "/" + passengerDetails.ffpNumber;
                                }
                                break;
                            }
                        }
                        if (ffpNumber != null) {
                            for (SpecialServiceRequest ssr : pnrBean.getPassengerSpecialServiceRequests(pnr, passengerNum)) {
                                if (ssr.getActionCode().equals(Constants.SSR_ACTION_CODE_TICKET_NUMBER)) {
                                    ETicket eTicket = flightScheduleBean.getETicket(Long.parseLong(ssr.getValue()));
                                    eTicket.setFfpNumber(ffpNumber);
                                    flightScheduleBean.updateETicket(eTicket);
                                }
                            }
                        }
                    if (milesRedeemed > 0) {
                        ffpBean.redeemMiles(authManagedBean.getCustomerId(), milesRedeemed);
                        customerLogBean.createCustomerLog(authManagedBean.getCustomerId(), "Redeemed miles for booking " + pnr.getBookingReference(), "redeem_miles");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public void prevStep() {
        step--;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<FlightSearchResult> getOutboundSearchResult() {
        return outboundSearchResult;
    }

    public void setOutboundSearchResult(List<FlightSearchResult> outboundSearchResult) {
        this.outboundSearchResult = outboundSearchResult;
    }

    public List<FlightSearchResult> getReturnSearchResult() {
        return returnSearchResult;
    }

    public void setReturnSearchResult(List<FlightSearchResult> returnSearchResult) {
        this.returnSearchResult = returnSearchResult;
    }

    public FlightSearchResult getSelectedOutboundSearchResult() {
        return selectedOutboundSearchResult;
    }

    public void setSelectedOutboundSearchResult(FlightSearchResult selectedOutboundSearchResult) {
        this.selectedOutboundSearchResult = selectedOutboundSearchResult;
    }

    public FlightSearchResult getSelectedReturnSearchResult() {
        return selectedReturnSearchResult;
    }

    public void setSelectedReturnSearchResult(FlightSearchResult selectedReturnSearchResult) {
        this.selectedReturnSearchResult = selectedReturnSearchResult;
    }

    public HashMap<Integer, BookingClass> getSelectedBookingClasses() {
        return selectedBookingClasses;
    }

    public void setSelectedBookingClasses(HashMap<Integer, BookingClass> selectedBookingClasses) {
        this.selectedBookingClasses = selectedBookingClasses;
    }

    public double getTotalPricePerPerson() {
        return totalPricePerPerson;
    }

    public void setTotalPricePerPerson(double totalPricePerPerson) {
        this.totalPricePerPerson = totalPricePerPerson;
    }

    private class AirportComparator implements Comparator<Airport> {
        @Override
        public int compare(Airport o1, Airport o2) {
            return o1.getCity().getName().compareTo(o2.getCity().getName());
        }
    }

    public List<Airport> getDestinationsByOrigin() {
        try {
            List<Airport> destinations = routeBean.getDestinationByOrigin(routeBean.getAirport(origin));
            Collections.sort(destinations, new AirportComparator());
            return destinations;
        } catch (NotFoundException e) {
            return new ArrayList<>();
        }
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }

    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        this.roundTrip = roundTrip;
    }

    public List<FlightSearchItem> getSelectedFlightSearchItems() {
        return selectedFlightSearchItems;
    }

    public void setSelectedFlightSearchItems(List<FlightSearchItem> selectedFlightSearchItems) {
        this.selectedFlightSearchItems = selectedFlightSearchItems;
    }

    public List<PassengerDetails> getPassengersDetails() {
        return passengersDetails;
    }

    public void setPassengersDetails(List<PassengerDetails> passengersDetails) {
        this.passengersDetails = passengersDetails;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(String paymentCard) {
        this.paymentCard = paymentCard;
    }

    public PNR getPnr() {
        return pnr;
    }

    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getMilesRedeemed() {
        return milesRedeemed;
    }

    public void setMilesRedeemed(int milesRedeemed) {
        this.milesRedeemed = milesRedeemed;
    }
}
