package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Entity.AircraftAssignment;
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
public class CreateFlightManagedBean {
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<AircraftAssignment> aircraftAssignments;

    private String code;
    private long aaId;
    private Date departureDate;
    private Date arrivalDate;
    private String departureTime;
    private String arrivalTime;

    @PostConstruct
    public void init() {
        setAircraftAssignments(routeBean.getAllAircraftAssignments());
    }

    public void createFlight() throws NotFoundException {
        System.out.println(addTimeToDate(departureDate, departureTime));
        flightScheduleBean.createFlight(code,
                addTimeToDate(departureDate, departureTime),
                addTimeToDate(arrivalDate, arrivalTime), aaId);
        authManagedBean.createAuditLog("Created new flight: " + code, "create_flight");
        setCode(null);
        setAaId(0);
        setDepartureTime(null);
        setArrivalTime(null);
        FacesMessage m = new FacesMessage("Flight created successfully.");
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

    public String formatAA(AircraftAssignment aa) {
        return aa.getAircraft().getTailNumber() + " : " + aa.getRoute().getOrigin().getName() + " - " + aa.getRoute().getDestination().getName();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<AircraftAssignment> getAircraftAssignments() {
        return aircraftAssignments;
    }

    public void setAircraftAssignments(List<AircraftAssignment> aircraftAssignments) {
        this.aircraftAssignments = aircraftAssignments;
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
}
