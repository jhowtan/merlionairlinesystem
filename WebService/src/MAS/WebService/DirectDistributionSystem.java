package MAS.WebService;
import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Common.FlightSearchItem;
import MAS.Common.FlightSearchResult;
import MAS.Entity.BookingClass;
import MAS.Entity.ETicket;
import MAS.Entity.PNR;
import MAS.Entity.SpecialServiceRequest;
import MAS.Exception.BookingException;
import MAS.Exception.NotFoundException;
import MAS.WebServiceHelpers.WSBookingClass;
import MAS.WebServiceHelpers.WSFlightResult;
import MAS.WebServiceHelpers.WSPassengerDetails;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebService()
public class DirectDistributionSystem {

    FlightSearchBean flightSearchBean;
    PNRBean pnrBean;
    BookingClassBean bookingClassBean;
    BookFlightBean bookFlightBean;
    FlightScheduleBean flightScheduleBean;

    public DirectDistributionSystem() {
        try {
            flightSearchBean = (FlightSearchBean) new InitialContext().lookup("java:global/WebService_war_exploded/FlightSearchEJB");
            pnrBean = (PNRBean) new InitialContext().lookup("java:global/WebService_war_exploded/PNREJB");
            bookingClassBean = (BookingClassBean) new InitialContext().lookup("java:global/WebService_war_exploded/BookingClassEJB");
            bookFlightBean = (BookFlightBean) new InitialContext().lookup("java:global/WebService_war_exploded/BookFlightEJB");
            flightScheduleBean = (FlightScheduleBean) new InitialContext().lookup("java:global/WebService_war_exploded/FlightScheduleEJB");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public WSFlightResult[][] searchAvailableFlights(@WebParam(name="origin") String origin, @WebParam(name="destination") String destination, @WebParam(name="date") Date date, @WebParam(name="passengerCount") int passengerCount, @WebParam(name="travelClass") int travelClass, @WebParam(name="travelDuration") int travelDuration) {
        List<FlightSearchResult> flightSearchResults = flightSearchBean.searchAvailableFlights(origin, destination, date, passengerCount, travelClass, travelDuration);
        ArrayList<ArrayList<WSFlightResult>> searchResults = new ArrayList<>();
        for (FlightSearchResult flightSearchResult : flightSearchResults) {
            ArrayList<WSFlightResult> searchResult = new ArrayList<>();
            for (FlightSearchItem flightSearchItem : flightSearchResult.getFlightSearchItems()) {
                WSFlightResult flightResult = new WSFlightResult();
                flightResult.id = flightSearchItem.getFlight().getId();
                flightResult.flightNumber = flightSearchItem.getFlight().getCode();
                flightResult.departureTime = flightSearchItem.getFlight().getDepartureTime();
                flightResult.arrivalTime = flightSearchItem.getFlight().getArrivalTime();
                flightResult.origin = flightSearchItem.getFlight().getAircraftAssignment().getRoute().getOrigin().getId();
                flightResult.destination = flightSearchItem.getFlight().getAircraftAssignment().getRoute().getDestination().getId();
                flightResult.bookingClasses = new ArrayList<>();
                for (BookingClass bookingClass : flightSearchItem.getBookingClasses()) {
                    WSBookingClass wsBookingClass = new WSBookingClass();
                    wsBookingClass.id = bookingClass.getId();
                    wsBookingClass.name = bookingClass.getName();
                    wsBookingClass.price = bookingClass.getPrice();
                    wsBookingClass.fareRule = bookingClass.getFareRule().getName();
                    flightResult.bookingClasses.add(wsBookingClass);
                }
                searchResult.add(flightResult);
            }
            searchResults.add(searchResult);
        }
        WSFlightResult[][] searchResultsArray = new WSFlightResult[searchResults.size()][];
        for (int i = 0; i < searchResults.size(); i++) {
            WSFlightResult[] searchResult = searchResults.get(i).toArray(new WSFlightResult[searchResults.get(i).size()]);
            searchResultsArray[i] = searchResult;
        }
        return searchResultsArray;
    }

    @WebMethod
    public String book(@WebParam(name="bookingClasses") long[] bookingClasses, @WebParam(name="passengersDetails") WSPassengerDetails[] passengersDetails, @WebParam(name="partnerId") String partnerId) throws BookingException {
        PNR pnr = null;
        ArrayList<BookingClass> b = new ArrayList<>();
        for (long bookingClass : bookingClasses) {
            try {
                b.add(bookingClassBean.getBookingClass(bookingClass));
            } catch (NotFoundException e) {
                throw new BookingException();
            }
        }
        ArrayList<String> p = new ArrayList<>();
        for (WSPassengerDetails passengerDetails : passengersDetails) {
            p.add(passengerDetails.lastName.toUpperCase() + "/" + passengerDetails.firstName.toUpperCase());
        }
        pnr = bookFlightBean.bookFlights(b, p);
        for (WSPassengerDetails passengerDetails : passengersDetails) {
            if (passengerDetails.ffpNumber.equals("")) continue;
            try {
                pnrBean.setSpecialServiceRequest(pnr, pnrBean.getPassengerNumber(pnr, passengerDetails.lastName.toUpperCase() + "/" + passengerDetails.firstName.toUpperCase()), Constants.SSR_ACTION_CODE_FFP, passengerDetails.ffpProgram + "/" + passengerDetails.ffpNumber);
            } catch (NotFoundException e) {
                throw new BookingException();
            }
        }
        try {
            pnrBean.updatePNR(pnr);
            for (String passenger : pnr.getPassengers()) {
                int passengerNum = pnrBean.getPassengerNumber(pnr, passenger);
                String ffpNumber = null;
                for (WSPassengerDetails passengerDetails : passengersDetails) {
                    if (passenger.toUpperCase().equals(passengerDetails.lastName.toUpperCase() + "/" + passengerDetails.firstName.toUpperCase())) {
                        if (passengerDetails.ffpNumber != null && !passengerDetails.ffpNumber.equals("")) {
                            ffpNumber = passengerDetails.ffpProgram + "/" + passengerDetails.ffpNumber;
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
            }
        } catch (NotFoundException e) {
            throw new BookingException();
        }
        return pnr.getBookingReference();
    }

    public static void main(String[] argv) {
        Object implementor = new DirectDistributionSystem ();
        String address = "http://localhost:9000/DirectDistributionSystem";
        Endpoint.publish(address, implementor);
    }
}