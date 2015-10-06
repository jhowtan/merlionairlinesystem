package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Flight;
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
import java.util.Map;

@ManagedBean
public class UpdateFlightManagedBean {
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    private Map<String,String> params;

    private Flight flight;

    private String code;
    private long aaId;
    private Date departureDate;
    private Date arrivalDate;
    private String departureTime;
    private String arrivalTime;
    private Long flightGroup;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long flId = Long.parseLong(params.get("flId"));
        getFlight(flId);
    }

    private void getFlight(long id) {
        try {
            flight = flightScheduleBean.getFlight(id);
        } catch (NotFoundException e) {
            e.getMessage();
        }
        code = flight.getCode();
        aaId = flight.getAircraftAssignment().getId();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        departureDate = flight.getDepartureTime();
        departureTime = timeFormat.format(flight.getDepartureTime());
        arrivalDate = flight.getArrivalTime();
        arrivalTime = timeFormat.format(flight.getArrivalTime());
        if (flight.getFlightGroup() != null) {
            flightGroup = flight.getFlightGroup().getId();
        }
    }

    public void save() throws NotFoundException {
        System.out.println(addTimeToDate(departureDate, departureTime));
        flightScheduleBean.changeFlightCode(flight.getId(), code);
        flightScheduleBean.changeFlightTimings(flight.getId(),
                addTimeToDate(departureDate, departureTime),
                addTimeToDate(arrivalDate, arrivalTime));
        flightScheduleBean.removeFlightFromFlightGroup(flight.getId());
        flightGroup = null;
        authManagedBean.createAuditLog("Updated flight: " + flight.getCode(), "update_flight");
        FacesMessage m = new FacesMessage("Flight updated successfully.");
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

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public long getFlightGroup() {
        return flightGroup;
    }

    public void setFlightGroup(long flightGroup) {
        this.flightGroup = flightGroup;
    }
}
