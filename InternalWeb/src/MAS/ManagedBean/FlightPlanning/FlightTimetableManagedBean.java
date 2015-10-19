package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FleetBean;
import MAS.Bean.FlightScheduleBean;
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
    }

    public void getAircraftTimetable() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            long aircraftId = Long.parseLong(params.get("aircraftId"));

            // @TODO: do stuff

            List<Flight> resultFlights = flightScheduleBean.getAllFlights();
            List<AircraftMaintenanceSlot> resultMaint;

            ArrayList<CalendarEntry> calendarEntries = new ArrayList<>();

            for (Flight f : resultFlights) {
                CalendarEntry c = new CalendarEntry();
                c.title = f.getCode();
                c.start = f.getDepartureTime();
                c.end = f.getArrivalTime();
                c.className = new ArrayList<>();
                c.className.add("b-success");
                // @TODO
                c.className.add("another-class-here-replace-this");
                c.info = "origin dest replace this";
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
            e.printStackTrace();
        }

//        List<User> users = userBean.searchForUser(query);
//        List<Workgroup> workgroups = workgroupBean.searchForWorkgroup(query);
//
//        ArrayList<SearchResult> searchResults = new ArrayList<>();
//
//        for (User user : users) {
//            SearchResult r = new SearchResult();
//            r.label = user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")";
//            r.value = "user:" + user.getId() + ":" + r.label;
//            searchResults.add(r);
//        }
//
//        for (Workgroup workgroup : workgroups) {
//            SearchResult r = new SearchResult();
//            r.label = workgroup.getName();
//            r.value = "workgroup:" + workgroup.getId() + ":" + r.label;
//            searchResults.add(r);
//        }
        //{title:'Family', start: new Date(y, m, 9, 19, 30), end: new Date(y, m, 9, 20, 30), className: ['b-l b-2x b-success'], info:'Family party'}

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
