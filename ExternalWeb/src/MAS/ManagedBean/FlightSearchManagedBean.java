package MAS.ManagedBean;

import MAS.Bean.FlightSearchBean;
import MAS.Bean.RouteBean;
import MAS.Common.FlightSearchResult;
import MAS.Entity.Airport;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.*;

@ManagedBean
@ViewScoped
public class FlightSearchManagedBean {

    @EJB
    RouteBean routeBean;
    @EJB
    FlightSearchBean flightSearchBean;

    private int step = 1;

    private List<Airport> airports;
    private String origin;
    private String destination;

    private Date departureDate;
    private Date returnDate;
    private int passengers = 1;
    private int travelClass = 3;

    private boolean roundTrip = true;

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

    public void nextStep() {
        step++;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
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

    @PostConstruct
    public void init() {
        setAirports(routeBean.getAllAirports());
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
}
