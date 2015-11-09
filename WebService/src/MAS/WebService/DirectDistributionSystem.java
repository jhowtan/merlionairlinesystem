package MAS.WebService;
import MAS.Bean.FlightSearchBean;
import MAS.Common.FlightSearchItem;
import MAS.Common.FlightSearchResult;
import MAS.Entity.BookingClass;
import MAS.WebServiceHelpers.WSBookingClass;
import MAS.WebServiceHelpers.WSFlightResult;

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

    public DirectDistributionSystem() {
        try {
            flightSearchBean = (FlightSearchBean) new InitialContext().lookup("java:global/WebService_war_exploded/FlightSearchEJB");
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

    public static void main(String[] argv) {
        Object implementor = new DirectDistributionSystem ();
        String address = "http://localhost:9000/DirectDistributionSystem";
        Endpoint.publish(address, implementor);
    }
}