package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.List;

@ManagedBean
@ViewScoped
public class SeasonalityReportManagedBean {
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    RouteBean routeBean;

    private String airportId;
    private List<Airport> airports;
    private int[] flightCountByMonth;
    private double[] flightUtilByMonth;

    @PostConstruct
    private void init() {
        airports = routeBean.getAllAirports();
    }

    public void airportChangeListener(AjaxBehaviorEvent event) {
        try {
            flightCountByMonth = flightScheduleBean.getNumFlightsByMonthForDestination(airportId);
            flightUtilByMonth = flightScheduleBean.getFlightUtilisationByMonthForDestination(airportId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBaselineAggregate(String airportId) {
        try {
            return flightScheduleBean.getAggregateFlightSalesForDestination(airportId);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int[] getFlightCountByMonth() {
        return flightCountByMonth;
    }

    public void setFlightCountByMonth(int[] flightCountByMonth) {
        this.flightCountByMonth = flightCountByMonth;
    }

    public double[] getFlightUtilByMonth() {
        return flightUtilByMonth;
    }

    public void setFlightUtilByMonth(double[] flightUtilByMonth) {
        this.flightUtilByMonth = flightUtilByMonth;
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
