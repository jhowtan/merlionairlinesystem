package MAS.ManagedBean.CrewOperations;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Common.Permissions;
import MAS.Common.Utils;
import MAS.Entity.Flight;
import MAS.Entity.FlightRoster;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class FlightRosterManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    @EJB
    FleetBean fleetBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    FlightBidBean flightBidBean;
    @EJB
    UserBean userBean;
    @EJB
    FlightRosterBean flightRosterBean;

    private List<User> crew;
    private List<Flight> flights;
    private long flightId;
    private Long userId;
    private FlightRoster flightRoster;

    @PostConstruct
    private void init() {
        load();
    }

    private void load() {
        Date start = new Date();
        Date end = Utils.monthEnd(2);
        flights = flightScheduleBean.getFlightWithinDate(start, end);
        crew = new ArrayList<>();
        flightRoster = null;
        flightId = 0;
    }

    public void flightCodeChangeListener(AjaxBehaviorEvent event) {
        try {
            flightRoster = flightRosterBean.getFlightRosterForFlight(flightId);
            crew = flightRoster.getMembers();
        } catch (NotFoundException e) {
            //No flight roster for this flight
            flightRoster = null;
            crew = new ArrayList<>();
        }
    }

    public void addCrew() {
        try {
            User user = userBean.getUser(userId);
            boolean exists = false;
            for (int i = 0; i < crew.size(); i++) {
                if (crew.get(i).equals(user))
                    exists = true;
            }
            if (!exists)
                crew.add(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userId = null;
    }

    public void removeCrew(User member) {
        if (crew.indexOf(member) != -1)
            crew.remove(member);
    }

    public void saveFlightRoster() {
        try {
            if (flightRoster == null) {
                List<Long> crewIds = new ArrayList<>();
                for (int i = 0; i < crew.size(); i++)
                    crewIds.add(crew.get(i).getId());
                flightRosterBean.createFlightRoster(flightId, crewIds);
            } else {
                flightRosterBean.changeFlightRosterMembers(flightRoster.getId(), crew);
            }
            FacesMessage m = new FacesMessage("Flight rosters saved.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Unable to save flight rosters.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
        load();
    }

    public boolean hasFlight() {
        try {
            flightScheduleBean.getFlight(flightId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getFulfillmentString() {
        try {
            Flight flight = flightScheduleBean.getFlight(flightId);
            int cabinReq = flight.getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCabinCrewReq();
            int cockpitReq = flight.getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getCockpitCrewReq();
            int inCabin = 0;
            int inCockpit = 0;
            for (int i = 0; i < crew.size(); i++) {
                if (crew.get(i).getJob() == Constants.cabinCrewJobId)
                    inCabin++;
                else if (crew.get(i).getJob() == Constants.cockpitCrewJobId)
                    inCockpit++;
            }
            return "This flight has " + inCabin + " cabin crew out of the required " + cabinReq + ", and " + inCockpit + " pilots out of the required " + cockpitReq + ".";
        } catch (Exception e) {
            return "This flight is unfulfilled";
        }
    }

    public boolean isCrewManager() {
        return authManagedBean.hasPermission(Permissions.MANAGE_FLIGHT_BID);
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
        public String crewMembers;
        public String flightId;
        public String flightRosterId;
    }

    private class CalendarResource {
        public String id;
        public String crewName;
    }

    public void getFlightRosters() {
        //Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            //String userIdString = params.get("userId");
            ArrayList<CalendarEntry> calendarEntries = new ArrayList<>();
            ArrayList<CalendarResource> calendarResources = new ArrayList<>();
            User user = userBean.getUser(authManagedBean.getUserId());
            List<FlightRoster> flightRosters;

            if (!isCrewManager())
                flightRosters = flightRosterBean.getFlightRostersOfUser(user.getId());
            else
                flightRosters = flightRosterBean.getAllFutureFlightRosters();

            for (FlightRoster fr : flightRosters) {
                CalendarEntry c = new CalendarEntry();
                Flight f = fr.getFlight();
                c.title = f.getAircraftAssignment().getRoute().getOrigin().getId() + " - " +
                        f.getAircraftAssignment().getRoute().getDestination().getId();
                c.start = f.getDepartureTime();
                c.end = f.getArrivalTime();
                c.className = new ArrayList<>();
                c.flightId = String.valueOf(f.getId());
                c.flightRosterId = String.valueOf(fr.getId());
                if (fr.isComplete())
                    c.className.add("calendar-blue-event");
                else
                    c.className.add("calendar-red-event");
                String cMembers = "";
                for (int i = 0; i < fr.getMembers().size(); i++) {
                    cMembers = cMembers.concat(fr.getMembers().get(i).getFirstName() + " " + fr.getMembers().get(i).getLastName() + ", ");
                }
                if (cMembers.length() > 2)
                    c.crewMembers = cMembers.substring(0, cMembers.length() - 2);
                else
                    c.crewMembers = "Nobody";
                c.info = f.getCode() + ": " + c.crewMembers;
                calendarEntries.add(c);

                CalendarResource cr = new CalendarResource();
                cr.id = String.valueOf(f.getAircraftAssignment().getAircraft().getId());
                if (!isCrewManager())
                    cr.crewName = user.getFirstName() + " " + user.getLastName();
                calendarResources.add(cr);
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
            e.printStackTrace();
        }
    }

    private class SearchResult {
        public String value;
        public String label;
    }

    public void crewSearch() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");

        List<User> users = userBean.searchForFlightCrew(query);

        ArrayList<SearchResult> searchResults = new ArrayList<>();

        for (User user : users) {
            SearchResult r = new SearchResult();
            r.label = user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")";
            r.value = String.valueOf(user.getId());
            searchResults.add(r);
        }

        Gson gson = new Gson();
        String json = gson.toJson(searchResults);

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
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<User> getCrew() {
        return crew;
    }

    public void setCrew(List<User> crew) {
        this.crew = crew;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
