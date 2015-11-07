package MAS.ManagedBean.CrewOperations;

import MAS.Bean.*;
import MAS.Common.Permissions;
import MAS.Common.Utils;
import MAS.Entity.Flight;
import MAS.Entity.FlightRoster;
import MAS.Entity.User;
import MAS.ManagedBean.Auth.AuthManagedBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
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
                flightRosters = flightRosterBean.getAllFlightRosters();

            for (FlightRoster fr : flightRosters) {
                CalendarEntry c = new CalendarEntry();
                Flight f = fr.getFlight();
                c.title = f.getAircraftAssignment().getRoute().getOrigin().getId() + " - " +
                        f.getAircraftAssignment().getRoute().getDestination().getId();
                c.start = f.getDepartureTime();
                c.end = f.getArrivalTime();
                c.className = new ArrayList<>();
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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
