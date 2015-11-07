package MAS.ManagedBean.CrewOperations;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
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
    @EJB
    FlightRosterBean flightRosterBean;
    @EJB
    AttributesBean attributesBean;

    private String flightId;
    private List<Flight> flightsBidded;
    private List<Flight> pastFlightsBidded;

    @PostConstruct
    private void init() {
        flightsBidded = new ArrayList<>();
        try {
            FlightBid pastBid = flightBidBean.getFlightBidOfUser(authManagedBean.getUserId());
            pastFlightsBidded = new ArrayList<>(pastBid.getFlights());
        } catch (Exception e) {
            pastFlightsBidded = new ArrayList<>();
        }
    }

    public void addFlight() {
        try {
            //Check for number of flights already bidded for first
            Flight flight = flightScheduleBean.getFlight(Long.parseLong(flightId));
            boolean exists = false;
            for (int i = 0; i < flightsBidded.size(); i++) {
                if (flightsBidded.get(i).getId() == flight.getId())
                    exists = true;
            }
            if (!exists) flightsBidded.add(flight);
            else {
                FacesMessage m = new FacesMessage("Flight has already been selected.");
                m.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage("status", m);
            }
            flightId = "";
        } catch (Exception e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("Unable to add flight.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void sendBid() {
        try {
            List<Long> flightIds = new ArrayList<>();
            for (int i = 0 ; i < flightsBidded.size(); i++)
                flightIds.add(flightsBidded.get(i).getId());
            flightBidBean.createFlightBid(authManagedBean.getUserId(), flightIds);
            pastFlightsBidded = new ArrayList<>(flightsBidded);
            flightId = "";
            flightsBidded = new ArrayList<>();
            FacesMessage m = new FacesMessage("Flight bids sent.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("Unable to send flight bid.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public String getSendButtonText() {
        int left = attributesBean.getIntAttribute(Constants.FLIGHTJOBS_PER_MONTH, 5) - flightsBidded.size();
        if (left > 0) {
            return left + " flights left to choose.";
        }
        return "Send Bid";
    }

    public boolean isSendButtonDisabled() {
        return !(flightsBidded.size() == attributesBean.getIntAttribute(Constants.FLIGHTJOBS_PER_MONTH, 5));
    }

    public void removeFlight(Flight flight) {
        if (flightsBidded.indexOf(flight) != -1)
            flightsBidded.remove(flight);
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

    public void getFlightsForBid() {
        //Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            ArrayList<CalendarEntry> calendarEntries = new ArrayList<>();
            ArrayList<CalendarResource> calendarResources = new ArrayList<>();
            Date startOfMonth = Utils.monthStart(1);
            Date endOfMonth = Utils.monthEnd(1);
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
                c.info = f.getCode() + " (" + f.getId() + ")";
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
        flightRosterBean.allocateFlightJobs();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public List<Flight> getFlightsBidded() {
        return flightsBidded;
    }

    public void setFlightsBidded(List<Flight> flightsBidded) {
        this.flightsBidded = flightsBidded;
    }

    public List<Flight> getPastFlightsBidded() {
        return pastFlightsBidded;
    }

    public void setPastFlightsBidded(List<Flight> pastFlightsBidded) {
        this.pastFlightsBidded = pastFlightsBidded;
    }
}
