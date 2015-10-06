package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Flight;
import MAS.Entity.FlightGroup;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UpdateFlightGroupManagedBean {
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value = "#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    private Map<String, String> params;

    private String code;
    private long aaId;
    private String departureTime;
    private long flightGroupId;
    private int flightDuration;
    private List<Flight> flights;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        flightGroupId = Long.parseLong(params.get("flightGroupId"));
        FlightGroup flightGroup = null;
        try {
            flightGroup = flightScheduleBean.getFlightGroup(flightGroupId);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (flightGroup == null || flightGroup.getFlights().size() == 0) {
            // @TODO: Show an error, there are no flights in this FlightGroup or FlightGroup does not exist
        }
        Flight flight = flightGroup.getFlights().get(0);
        code = flight.getCode();
        aaId = flight.getAircraftAssignment().getId();
        departureTime = new SimpleDateFormat("HH:mm").format(flight.getDepartureTime());
        flightDuration = (int) ((flight.getArrivalTime().getTime() - flight.getDepartureTime().getTime()) / 60000);
        flights = flightGroup.getFlights();
    }

    public void save() throws NotFoundException {
        flightScheduleBean.updateRecurringFlight(flightGroupId, code, departureTime, flightDuration);
        authManagedBean.createAuditLog("Updated recurring flight: " + code, "update_recurring_flight");
        FacesMessage m = new FacesMessage("Flight group updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
        init();
    }

    public String formatAA() {
        try {
            AircraftAssignment aa = routeBean.getAircraftAssignment(aaId);
            return aa.getAircraft().getTailNumber() + " : " + aa.getRoute().getOrigin().getName() + " - " + aa.getRoute().getDestination().getName();
        } catch (NotFoundException e) {
            e.getMessage();
        }
        return "";
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getAaId() {
        return aaId;
    }

    public void setAaId(long aaId) {
        this.aaId = aaId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public long getFlightGroupId() {
        return flightGroupId;
    }

    public void setFlightGroupId(long flightGroupId) {
        this.flightGroupId = flightGroupId;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
