package MAS.ManagedBean.CrewOperations;

import MAS.Bean.FleetBean;
import MAS.Bean.FlightBidBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.UserBean;
import MAS.Common.Utils;
import MAS.Entity.*;
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
public class FlightBiddingManagedBean {
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

    public void getFlightsForBid() {
        //Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            ArrayList<CalendarEntry> calendarEntries = new ArrayList<>();
            ArrayList<CalendarResource> calendarResources = new ArrayList<>();
            Date startOfMonth = Utils.currentMonthStart();
            Date endOfMonth = Utils.currentMonthEnd();
            User loggedIn = userBean.getUser(authManagedBean.getUserId());

            List<Flight> resultFlights = flightScheduleBean.getFlightWithinDate(startOfMonth, endOfMonth);

            for (Flight f : resultFlights) {
                CalendarEntry c = new CalendarEntry();
                c.title = f.getAircraftAssignment().getRoute().getOrigin().getId() + " - " +
                        f.getAircraftAssignment().getRoute().getDestination().getId();
                c.start = f.getDepartureTime();
                c.end = f.getArrivalTime();
                c.className = new ArrayList<>();
                c.className.add("calendar-blue-event");
                c.info = f.getCode();
                c.aircraftId = String.valueOf(f.getAircraftAssignment().getAircraft().getId());
                calendarEntries.add(c);

                CalendarResource cr = new CalendarResource();
                cr.id = String.valueOf(f.getAircraftAssignment().getAircraft().getId());
                cr.tailNumber = f.getAircraftAssignment().getAircraft().getTailNumber();
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
            //e.printStackTrace();
        }
    }

    public void testButton() {
        flightBidBean.spamFlightBids();
    }

    public void testedButton() {

    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
