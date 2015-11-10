package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Airport;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
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
    private List<String> flightUtilArray;
    private List<String> flightCountArray;

    @PostConstruct
    private void init() {
        airports = routeBean.getAllAirports();
    }

    public void airportChangeListener(AjaxBehaviorEvent event) {
        try {
            flightCountByMonth = flightScheduleBean.getNumFlightsByMonthForDestination(airportId);
            flightUtilByMonth = flightScheduleBean.getFlightUtilisationByMonthForDestination(airportId);

            flightUtilArray = new ArrayList<>();
            for (int i = 0; i < flightUtilByMonth.length; i++) {
                flightUtilArray.add(String.valueOf(flightUtilByMonth[i]));
            }
            flightCountArray = new ArrayList<>();
            for (int i = 0; i < flightCountByMonth.length; i++) {
                flightCountArray.add(String.valueOf(flightCountByMonth[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getFlightCountArray() {
        return flightCountArray;
    }

    public void setFlightCountArray(List<String> flightCountArray) {
        this.flightCountArray = flightCountArray;
    }

    public List<String> getFlightUtilArray() {
        return flightUtilArray;
    }

    public void setFlightUtilArray(List<String> flightUtilArray) {
        this.flightUtilArray = flightUtilArray;
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
