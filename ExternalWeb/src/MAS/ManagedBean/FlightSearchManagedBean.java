package MAS.ManagedBean;

import MAS.Bean.RouteBean;
import MAS.Entity.Airport;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
public class FlightSearchManagedBean {

    @EJB
    RouteBean routeBean;

    private List<Airport> airports;

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
}
