package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FleetBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Common.Utils;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
public class FlightTimetableManagedBean {
    @EJB
    FleetBean fleetBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<Aircraft> aircrafts;
    private long aircraftId;
    private Aircraft aircraft;

    @PostConstruct
    private void init() {
        aircrafts = fleetBean.getAllAircraft();
    }

    public long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public void selectAircraft() {
        try {
            aircraft = fleetBean.getAircraft(aircraftId);
        } catch (NotFoundException e) {
            //No aircraft
        }
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    private class SearchResult {
        public String value;
        public String label;
    }

    private class CalendarEntry {
        public String title;
        public Date start;
        public Date end;
        public ArrayList<String> className;
        public String info;
        public String color;
    }

    public void getAircraftTimetable() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            long aircraftId = Long.parseLong(params.get("aircraftId"));
            List<Flight> resultFlights = flightScheduleBean.getFlightOfAc(aircraftId);
            List<AircraftMaintenanceSlot> resultMaint = aircraftMaintenanceSlotBean.findSlotByAircraft(aircraftId);

            ArrayList<CalendarEntry> calendarEntries = new ArrayList<>();

            for (Flight f : resultFlights) {
                CalendarEntry c = new CalendarEntry();
                c.title = f.getAircraftAssignment().getRoute().getOrigin().getId() + " - " +
                        f.getAircraftAssignment().getRoute().getDestination().getId();
                c.start = f.getDepartureTime();
                c.end = f.getArrivalTime();
                //c.color = "#378006";
                c.className = new ArrayList<>();
                //c.className.add("b-success");
                //c.className.add("b-info");
                c.className.add("calendar-blue-event");
                c.info = f.getCode();
                calendarEntries.add(c);
            }

            for (AircraftMaintenanceSlot m : resultMaint) {
                CalendarEntry c = new CalendarEntry();
                c.title = m.getAirport().getId();
                c.start = m.getStartTime();
                c.end = Utils.minutesLater(m.getStartTime(), (int) m.getDuration());
                c.className = new ArrayList<>();
                //c.className.add("b-warning");
                //c.className.add("b-info");
                c.className.add("calendar-red-event");
                calendarEntries.add(c);
            }

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            String json = gson.toJson(calendarEntries);

            if(!authManagedBean.isAuthenticated()) {
                json = "[]";
            }

            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setContentLength(json.length());
            response.setContentType("application/json");

            try {
                response.getOutputStream().write(json.getBytes());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ctx.responseComplete();

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }
}
