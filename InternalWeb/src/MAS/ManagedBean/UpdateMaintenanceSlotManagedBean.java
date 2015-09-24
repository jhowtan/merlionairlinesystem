package MAS.ManagedBean;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@ManagedBean
public class UpdateMaintenanceSlotManagedBean {
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @EJB
    RouteBean routeBean;
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private Map<String,String> params;
    private AircraftMaintenanceSlot slot;

    private Airport airport;
    private Aircraft aircraft;

    private Date startDate;
    private String startTime;
    private double duration;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long msId = Long.parseLong(params.get("msId"));
        getMaintenanceSlot(msId);
    }

    private void getMaintenanceSlot(long id) {
        try {
            slot = aircraftMaintenanceSlotBean.getSlot(id);
        } catch (NotFoundException e) {
            e.getMessage();
        }
        airport = slot.getAirport();
        aircraft = slot.getAircraft();
        startDate = slot.getStartTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        startTime = timeFormat.format(slot.getStartTime());
        duration = slot.getDuration();
    }

    public void save() throws NotFoundException {
        Date timestamp = addTimeToDate(startDate, startTime);
        System.out.println(timestamp);
        aircraftMaintenanceSlotBean.changeSlotDuration(slot.getId(), duration);
        aircraftMaintenanceSlotBean.changeSlotStartTime(slot.getId(), timestamp);
        authManagedBean.createAuditLog("Updated maintenance slot for: " + aircraft.getTailNumber() + " - " + airport.getCode() + " @ " + timestamp, "create_maintenance_slot");
        FacesMessage m = new FacesMessage("Maintenance slot updated successfully.");
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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public AircraftMaintenanceSlot getSlot() {
        return slot;
    }

    public void setSlot(AircraftMaintenanceSlot slot) {
        this.slot = slot;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
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
