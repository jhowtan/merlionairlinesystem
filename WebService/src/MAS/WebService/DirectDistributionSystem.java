package MAS.WebService;
import MAS.Bean.FlightSearchBean;
import MAS.Bean.UserBean;
import MAS.Common.FlightSearchItem;
import MAS.Common.FlightSearchResult;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.WebServiceHelpers.WSBookingClass;
import MAS.WebServiceHelpers.WSFlightResult;
import MAS.WebServiceHelpers.WSSearchResult;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.Endpoint;
import java.io.Serializable;
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
    public ArrayList<WSSearchResult> searchAvailableFlights(String origin, String destination, Date date, int passengerCount, int travelClass, int travelDuration) {
        List<FlightSearchResult> flightSearchResults = flightSearchBean.searchAvailableFlights(origin, destination, date, passengerCount, travelClass, travelDuration);

        ArrayList<WSSearchResult> searchResults = new ArrayList<>();

        WSSearchResult searchResult = new WSSearchResult();

        WSFlightResult flightResult = new WSFlightResult();
        flightResult.flightNumber = "MA123T";
        searchResult.flightResults = new ArrayList<>();
        searchResult.flightResults.add(flightResult);
        searchResult.flightResults.add(flightResult);

        searchResult.flightResults = new ArrayList<>();
        searchResult.flightResults.add(flightResult);

        searchResults.add(searchResult);

        return searchResults;

//        ArrayList<ArrayList<WSFlightResult>> searchResults = new ArrayList<>();
//
//        for (FlightSearchResult flightSearchResult : flightSearchResults) {
//            ArrayList<WSFlightResult> searchResult = new ArrayList<>();
//            for (FlightSearchItem flightSearchItem : flightSearchResult.getFlightSearchItems()) {
//                WSFlightResult wsFlightResult = new WSFlightResult();
//                wsFlightResult.flightNumber = flightSearchItem.getFlight().getCode();
////                webServiceFlight.departureTime = flightSearchItem.getFlight().getDepartureTime();
////                webServiceFlight.arrivalTime = flightSearchItem.getFlight().getArrivalTime();
//                // Add booking classes
//                searchResult.add(wsFlightResult);
//            }
//            searchResults.add(searchResult);
//        }
        //return searchResults;
    }

    public static void main(String[] argv) {
        Object implementor = new DirectDistributionSystem ();
        String address = "http://localhost:9000/DirectDistributionSystem";
        Endpoint.publish(address, implementor);
    }
}