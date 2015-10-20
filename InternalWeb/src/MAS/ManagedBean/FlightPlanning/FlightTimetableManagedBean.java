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
    private List<String> aircraftIdInputs;
    private String aircraftIds;
    private Aircraft aircraft;

    @PostConstruct
    private void init() {
        aircrafts = fleetBean.getAllAircraft();
    }

    public void saveAircraft() {
        aircraftIds = "";
        for (int i = 0; i < aircraftIdInputs.size(); i++) {
            aircraftIds = aircraftIds.concat(aircraftIdInputs.get(i)).concat("-");
        }
    }

    public String getAircraftIds() {
        return aircraftIds;
    }

    public void setAircraftIds(String aircraftIds) {
        this.aircraftIds = aircraftIds;
    }

    public class CalendarData {
        public List<CalendarEntry> entries;
        public List<CalendarResource> resources;
    }

    private class CalendarEntry {
        public String title;
        public Date start;
        public Date end;
        public ArrayList<String> className;
        public String info;
        public String color;
        public String aircraftId;
    }

    private class CalendarResource {
        public String id;
        public String tailNumber;
    }

    public void getAircraftTimetable() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            String[] aircraftIdsString = params.get("aircraftIds").split("-");

            ArrayList<CalendarEntry> calendarEntries = new ArrayList<>();
            ArrayList<CalendarResource> calendarResources = new ArrayList<>();
            for (String aircraftIdString : aircraftIdsString) {
                long aircraftId = Long.parseLong(aircraftIdString);

                Aircraft ac = fleetBean.getAircraft(aircraftId);
                CalendarResource cr = new CalendarResource();
                cr.id = aircraftIdString;
                cr.tailNumber = ac.getTailNumber();
                calendarResources.add(cr);

                List<Flight> resultFlights = flightScheduleBean.getFlightOfAc(aircraftId);
                List<AircraftMaintenanceSlot> resultMaint = aircraftMaintenanceSlotBean.findSlotByAircraft(aircraftId);

                for (Flight f : resultFlights) {
                    CalendarEntry c = new CalendarEntry();
                    c.title = f.getAircraftAssignment().getRoute().getOrigin().getId() + " - " +
                            f.getAircraftAssignment().getRoute().getDestination().getId();
                    c.start = f.getDepartureTime();
                    c.end = f.getArrivalTime();
                    c.className = new ArrayList<>();
                    c.className.add("calendar-blue-event");
                    c.info = f.getCode();
                    c.aircraftId = String.valueOf(aircraftId);
                    calendarEntries.add(c);
                }

                for (AircraftMaintenanceSlot m : resultMaint) {
                    CalendarEntry c = new CalendarEntry();
                    c.title = m.getAirport().getId();
                    c.start = m.getStartTime();
                    c.end = Utils.minutesLater(m.getStartTime(), (int) m.getDuration());
                    c.className = new ArrayList<>();
                    c.className.add("calendar-red-event");
                    c.aircraftId = String.valueOf(aircraftId);
                    c.info = "Maintenance";
                    calendarEntries.add(c);
                }
            }

            CalendarData cd = new CalendarData();
            cd.entries = calendarEntries;
            cd.resources = calendarResources;

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            String json = gson.toJson(cd);

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

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public List<String> getAircraftIdInputs() {
        return aircraftIdInputs;
    }

    public void setAircraftIdInputs(List<String> aircraftIdInputs) {
        this.aircraftIdInputs = aircraftIdInputs;
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
