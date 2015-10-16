package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ManagedBean
public class CreateMaintenanceSlotManagedBean {
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @EJB
    RouteBean routeBean;
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<Airport> airports;
    private List<Aircraft> aircrafts;

    private String airportId;
    private long aircraftId;

    private Date startDate;
    private String startTime;
    private double duration;

    @PostConstruct
    public void init() {
        setAircrafts(fleetBean.getAllAircraft());
        setAirports(routeBean.getAllAirports());
    }

    public void createMaintenanceSlot() throws NotFoundException {
        Date timestamp = addTimeToDate(startDate, startTime);
        System.out.println(timestamp);
        Airport airport = routeBean.getAirport(airportId);
        Aircraft aircraft = fleetBean.getAircraft(aircraftId);
        aircraftMaintenanceSlotBean.createSlot(addTimeToDate(startDate, startTime), duration, airportId, aircraftId);
        authManagedBean.createAuditLog("Created new maintenance slot for: " + aircraft.getTailNumber() + " - " + airport.getId() + " @ " + timestamp, "create_maintenance_slot");
        FacesMessage m = new FacesMessage("Maintenance slot for " + aircraft.getTailNumber() + " created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    private Date addTimeToDate(Date date, String time) {
        if (time.length() != 5)
            return null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String dateString = Integer.toString(cal.get(Calendar.YEAR)) + "-" +
                    Integer.toString(cal.get(Calendar.MONTH)+1) + "-" +
                    Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            return new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(dateString + " " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
