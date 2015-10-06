package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Common.Utils;
import MAS.Entity.AircraftAssignment;
import MAS.Exception.NoItemsCreatedException;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private String departureTime;
    private int flightDuration;
    private Date recurringStartDate;
    private Date recurringEndDate;
    private int[] recurringDays = {};
    private int[] recurringDaysItems = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY};

    @PostConstruct
    public void init() {
        setAircraftAssignments(routeBean.getAllAircraftAssignments());
    }

    public void createFlight() throws NotFoundException {
        Date departureDateTime = addTimeToDate(departureDate, departureTime);
        flightScheduleBean.createFlight(code,
                addTimeToDate(departureDate, departureTime),
                Utils.minutesLater(departureDateTime, flightDuration), aaId);
        authManagedBean.createAuditLog("Created new flight: " + code, "create_flight");
        setCode(null);
        setAaId(0);
        setDepartureTime(null);
        setDepartureDate(null);
        setFlightDuration(0);
        FacesMessage m = new FacesMessage("Flight created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void createRecurringFlight() {
        try {
            flightScheduleBean.createRecurringFlight(code, aaId, departureTime, flightDuration, recurringStartDate, recurringEndDate, recurringDays);

            authManagedBean.createAuditLog("Created new recurring flight: " + code, "create_recurring_flight");
            setCode(null);
            setAaId(0);
            setRecurringDays(new int[]{});
            setRecurringEndDate(null);
            setDepartureTime(null);
            setDepartureDate(null);
            setFlightDuration(0);
            FacesMessage m = new FacesMessage("Recurring flight created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (NoItemsCreatedException e) {
            // This is triggered when no flights are created
            // @TODO: Show some error message
            e.printStackTrace();
        }
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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Date getRecurringStartDate() {
        return recurringStartDate;
    }

    public void setRecurringStartDate(Date recurringStartDate) {
        this.recurringStartDate = recurringStartDate;
    }

    public Date getRecurringEndDate() {
        return recurringEndDate;
    }

    public void setRecurringEndDate(Date recurringEndDate) {
        this.recurringEndDate = recurringEndDate;
    }

    public int[] getRecurringDays() {
        return recurringDays;
    }

    public void setRecurringDays(int[] recurringDays) {
        this.recurringDays = recurringDays;
    }

    public int[] getRecurringDaysItems() {
        return recurringDaysItems;
    }

    public void setRecurringDaysItems(int[] recurringDaysItems) {
        this.recurringDaysItems = recurringDaysItems;
    }
}
