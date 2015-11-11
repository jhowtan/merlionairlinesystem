package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.CommonManagedBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@ManagedBean
public class ReportingDataManagedBean {
    @EJB
    private BookingClassBean bookingClassBean;
    @EJB
    private FleetBean fleetBean;
    @EJB
    private RouteBean routeBean;
    @EJB
    private CostsBean costsBean;
    @EJB
    private FlightScheduleBean flightScheduleBean;
    @EJB
    private AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @EJB
    private UserBean userBean;
    @EJB
    private FlightRosterBean flightRosterBean;
    @EJB
    private CampaignBean campaignBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @ManagedProperty(value="#{profitabilityReportManagedBean}")
    private ProfitabilityReportManagedBean profitabilityReportManagedBean;

    private class ReportItem {
        public String name;
        public Double value;
    }

    private class MonthItem {
        public String month;
        public String value;
    }

    private class AircraftCalendarData {
        public List<AircraftCalendarEntry> entries;
        public List<AircraftCalendarResource> resources;
    }

    private class AircraftCalendarEntry {
        public String title;
        public Date start;
        public Date end;
        public ArrayList<String> className;
        public String info;
        public String color;
        public String aircraftId;
    }

    private class AircraftCalendarResource {
        public String id;
        public String tailNumber;
    }

    private class CrewCalendarData {
        public List<CrewCalendarEntry> entries;
        public List<CrewCalendarResource> resources;
    }

    private class CrewCalendarEntry {
        public String title;
        public Date start;
        public Date end;
        public String info;
        public String color;
        public String userId;
        public ArrayList<String> className;
    }

    private class CrewCalendarResource {
        public String id;
        public String fullName;
        public String job;
    }

    private class CampaignCalendarData {
        public List<CampaignCalendarEntry> entries;
        public List<CampaignCalendarResource> resources;
    }

    private class CampaignCalendarEntry {
        public String title;
        public Date start;
        public Date end;
        public String info;
        public String color;
        public String campaignId;
        public ArrayList<String> className;
    }

    private class CampaignCalendarResource {
        public String id;
        public String name;
    }

    private class CustomerItem {
        public String revenuePerMile;
        public String flightCount;
    }

    public void search() throws NotFoundException {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String query = params.get("q");
        String id = params.get("id");
        String aircraftIds = params.get("aircraftIds");
        String jobIds = params.get("jobIds");
        String campaignIds = params.get("campaignIds");

        switch (query) {
            case "customerSegment":
                showCustomerSegmentation();
                return;
            case "campaignTimetable":
                showCampaignTimetable(campaignIds);
                return;
            case "aircraftTimetable":
                showAircraftTimetable(aircraftIds);
                return;
            case "crewTimetable":
                showCrewTimetable(jobIds);
                return;
            case "topPerformingFlights":
                showTopPerformingFlights();
                return;
            case "worstPerformingFlights":
                showWorstPerformingFlights();
                return;
            case "flightCountByMonth":
                showFlightCountByMonth(id);
                return;
            case "flightUtilByMonth":
                showFlightUtilByMonth(id);
                return;
            case "flightSalesByMonth":
                showFlightSalesByMonth(id);
                return;
            case "salesVarianceByMonth":
                showSalesVarianceByMonth(id);
                return;
            default:
                outputJSON("[]");
                return;
        }
    }

    private void showCustomerSegmentation() {

    }

    private void showCampaignTimetable(String campaignIds) throws NotFoundException {
        String[] campaignIdsString = campaignIds.split("-");
        if (campaignIdsString[0].equals("")) {
            String json = "[]";
            outputJSON(json);
        }
        ArrayList<CampaignCalendarEntry> calendarEntries = new ArrayList<>();
        ArrayList<CampaignCalendarResource> calendarResources = new ArrayList<>();
        for (String campaignIdString : campaignIdsString) {
            if (campaignIdString.equals("")) {
                continue;
            }
            Long id = Long.parseLong(campaignIdString);
            Campaign campaign = campaignBean.getCampaign(id);
            CampaignCalendarResource cr = new CampaignCalendarResource();
            cr.id = campaignIdString;
            cr.name = campaign.getName();
            calendarResources.add(cr);
            CampaignCalendarEntry c = new CampaignCalendarEntry();
            c.campaignId = String.valueOf(campaign.getId());
            c.title = String.valueOf(campaign.getCode()) + ": " + String.valueOf(campaign.getUsageCount());
            c.className = new ArrayList<>();
            c.className.add("calendar-green-event");
            c.start = campaign.getStartDate();
            c.end = campaign.getEndDate();
            c.info = String.valueOf(campaign.getId());
            calendarEntries.add(c);
        }

        CampaignCalendarData cd = new CampaignCalendarData();
        cd.entries = calendarEntries;
        cd.resources = calendarResources;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        String json = gson.toJson(cd);

        outputJSON(json);
    }

    private void showCrewTimetable(String jobIds) throws NotFoundException {
        String[] jobIdsString = jobIds.split("-");
        ArrayList<CrewCalendarEntry> calendarEntries = new ArrayList<>();
        ArrayList<CrewCalendarResource> crewCalendarResources = new ArrayList<>();
        List<User> crewStaff;
        if (jobIdsString[0].equals("")) {
            String json = "[]";
            outputJSON(json);
        }
        System.out.println(jobIdsString.length + " " + jobIdsString[0]);
        for (String jobIdString : jobIdsString) {
            // Deal with users for job
            if (jobIdString.equals("")) {
                continue;
            }
            int jobId = Integer.parseInt(jobIdString);
            crewStaff = userBean.getUsersWithJobs(jobId);
            for (User crew : crewStaff) {
                // Deal with individual user for job
                CrewCalendarResource cr = new CrewCalendarResource();

                // Cockpit Crew / Cabin Crew
                if (jobId == Constants.cockpitCrewJobId || jobId == Constants.cabinCrewJobId) {
                    List<FlightRoster> crewFlightRosters = flightRosterBean.getFlightRostersOfUser(crew.getId());
                    for (FlightRoster fr : crewFlightRosters) {
                        CrewCalendarEntry c = new CrewCalendarEntry();
                        Flight f = fr.getFlight();
                        c.title = f.getAircraftAssignment().getRoute().getOrigin().getId() + " - " +
                                f.getAircraftAssignment().getRoute().getDestination().getId();
                        c.start = f.getDepartureTime();
                        c.end = f.getArrivalTime();
                        c.className = new ArrayList<>();
                        if (jobId == Constants.cockpitCrewJobId) {
                            c.className.add("calendar-green-event");
                        } else {
                            c.className.add("calendar-blue-event");
                        }
                        c.info = f.getCode();
                        c.userId = String.valueOf(crew.getId());
                        calendarEntries.add(c);

                        cr.id = String.valueOf(crew.getId());
                        cr.fullName = crew.getFirstName() + " " + crew.getLastName();
                        cr.fullName = cr.fullName.concat(" (" + ((jobId == Constants.cockpitCrewJobId) ? "Cockpit Crew" : "Cabin Crew") + ")");
                        crewCalendarResources.add(cr);
                    }
                }
                // Maintenance Crew
                if (jobId == Constants.maintenanceCrewJobId) {
                    List<AircraftMaintenanceSlot> resultMaint = aircraftMaintenanceSlotBean.findSlotByAirport(crew.getBaseAirport().getId());
                    for (AircraftMaintenanceSlot m : resultMaint) {
                        CrewCalendarEntry c = new CrewCalendarEntry();
                        c.title = m.getAirport().getId();
                        c.start = m.getStartTime();
                        c.end = Utils.minutesLater(m.getStartTime(), (int) m.getDuration());
                        c.className = new ArrayList<>();
                        c.className.add("calendar-red-event");
                        c.userId = String.valueOf(crew.getId());
                        c.info = "Maintenance";
                        calendarEntries.add(c);

                        cr.id = String.valueOf(crew.getId());
                        cr.fullName = crew.getFirstName() + " " + crew.getLastName();
                        cr.fullName = cr.fullName.concat(" (" + "Maintenance Crew" + ")");
                        crewCalendarResources.add(cr);
                    }
                }

            }
        }
        CrewCalendarData cd = new CrewCalendarData();
        cd.entries = calendarEntries;
        cd.resources = crewCalendarResources;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        String json = gson.toJson(cd);

        outputJSON(json);
    }

    private void showAircraftTimetable(String aircraftIds) {
        try {
            String[] aircraftIdsString = aircraftIds.split("-");

            ArrayList<AircraftCalendarEntry> calendarEntries = new ArrayList<>();
            ArrayList<AircraftCalendarResource> aircraftCalendarResources = new ArrayList<>();
            for (String aircraftIdString : aircraftIdsString) {
                long aircraftId = Long.parseLong(aircraftIdString);

                Aircraft ac = fleetBean.getAircraft(aircraftId);
                AircraftCalendarResource cr = new AircraftCalendarResource();
                cr.id = aircraftIdString;
                cr.tailNumber = ac.getTailNumber();
                aircraftCalendarResources.add(cr);

                List<Flight> resultFlights = flightScheduleBean.getFlightOfAc(aircraftId);
                List<AircraftMaintenanceSlot> resultMaint = aircraftMaintenanceSlotBean.findSlotByAircraft(aircraftId);

                for (Flight f : resultFlights) {
                    AircraftCalendarEntry c = new AircraftCalendarEntry();
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
                    AircraftCalendarEntry c = new AircraftCalendarEntry();
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

            AircraftCalendarData cd = new AircraftCalendarData();
            cd.entries = calendarEntries;
            cd.resources = aircraftCalendarResources;

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            String json = gson.toJson(cd);

            outputJSON(json);

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void showSalesVarianceByMonth(String id) throws NotFoundException{
        ArrayList<MonthItem> monthItems = new ArrayList<>();
        double[] salesVariancePerMonth = flightScheduleBean.getSalesVarianceOfFlightForDestination(id);
        List<String> months = Arrays.asList(Constants.MONTHS_OF_YEAR);
        for (int i = 0; i < months.size(); i++) {
            MonthItem monthItem = new MonthItem();
            monthItem.month = months.get(i);
            monthItem.value = String.valueOf(salesVariancePerMonth[i]);
            monthItems.add(monthItem);
        }
        Gson gson = new Gson();
        String json = gson.toJson(monthItems);

        outputJSON(json);
    }

    private void showFlightSalesByMonth(String id) throws NotFoundException{
        ArrayList<MonthItem> monthItems = new ArrayList<>();
        int[] flightSalesPerMonth = flightScheduleBean.getFlightSalesVolumeByMonthForDestination(id);
        List<String> months = Arrays.asList(Constants.MONTHS_OF_YEAR);
        for (int i = 0; i < months.size(); i++) {
            MonthItem monthItem = new MonthItem();
            monthItem.month = months.get(i);
            monthItem.value = String.valueOf(flightSalesPerMonth[i]);
            monthItems.add(monthItem);
        }

        Gson gson = new Gson();
        String json = gson.toJson(monthItems);

        outputJSON(json);
    }


    public void showTopPerformingFlights() throws NotFoundException {
        ArrayList<ReportItem> reportItems = new ArrayList<>();
        List<Flight> flights = flightScheduleBean.getAllFlights();
        for (Flight flight : flights) {
            ReportItem flightItem = new ReportItem();
            flightItem.name = flight.getCode() + " (" + CommonManagedBean.formatDate("dd MMM yy, HH:mm", flight.getDepartureTime()) + ")";
            flightItem.value = profitabilityReportManagedBean.getProfitabilityByFlight(flight);
            reportItems.add(flightItem);
        }
        Collections.sort(reportItems, new Comparator<ReportItem>() {
            @Override
            public int compare(ReportItem o1, ReportItem o2) {
                if (o1.value < o2.value)
                    return 1;
                else if (o1.value > o2.value)
                    return -1;
                return 0;
            }
        });
        ArrayList<ReportItem> resultItems = new ArrayList<>();
        int size = 5;
        if (reportItems.size() < 5) {
            size = reportItems.size();
        }

        for (int i = 0; i < size; i++) {
            resultItems.add(reportItems.get(i));
        }

        Gson gson = new Gson();
        String json = gson.toJson(resultItems);

        outputJSON(json);
    }

    public void showWorstPerformingFlights() throws NotFoundException {
        ArrayList<ReportItem> reportItems = new ArrayList<>();
        List<Flight> flights = flightScheduleBean.getAllFlights();
        for (Flight flight : flights) {
            ReportItem flightItem = new ReportItem();
            flightItem.name = flight.getCode() + " (" + CommonManagedBean.formatDate("dd MMM yy, HH:mm", flight.getDepartureTime()) + ")";
            flightItem.value = profitabilityReportManagedBean.getProfitabilityByFlight(flight);
            reportItems.add(flightItem);
        }
        Collections.sort(reportItems, new Comparator<ReportItem>() {
            @Override
            public int compare(ReportItem o1, ReportItem o2) {
                if (o1.value > o2.value)
                    return 1;
                else if (o1.value < o2.value)
                    return -1;
                return 0;
            }
        });
        ArrayList<ReportItem> resultItems = new ArrayList<>();
        int size = 5;
        if (reportItems.size() < 5) {
            size = reportItems.size();
        }

        for (int i = 0; i < size; i++) {
            resultItems.add(reportItems.get(i));
        }

        Gson gson = new Gson();
        String json = gson.toJson(resultItems);

        outputJSON(json);
    }

    public void showFlightCountByMonth(String airportId) throws NotFoundException {
        ArrayList<MonthItem> monthItems = new ArrayList<>();
        int[] flightCountByMonth = flightScheduleBean.getNumFlightsByMonthForDestination(airportId);
        List<String> months = Arrays.asList(Constants.MONTHS_OF_YEAR);
        for (int i = 0; i < months.size(); i++) {
            MonthItem monthItem = new MonthItem();
            monthItem.month = months.get(i);
            monthItem.value = String.valueOf(flightCountByMonth[i]);
            monthItems.add(monthItem);
        }

        Gson gson = new Gson();
        String json = gson.toJson(monthItems);

        outputJSON(json);
    }

    public void showFlightUtilByMonth(String airportId) throws NotFoundException {
        ArrayList<MonthItem> monthItems = new ArrayList<>();
        double[] flightUtilByMonth = flightScheduleBean.getFlightUtilisationByMonthForDestination(airportId);
        List<String> months = Arrays.asList(Constants.MONTHS_OF_YEAR);
        for (int i = 0; i < months.size(); i++) {
            MonthItem monthItem = new MonthItem();
            monthItem.month = months.get(i);
            monthItem.value = String.valueOf(flightUtilByMonth[i]);
            monthItems.add(monthItem);
        }

        Gson gson = new Gson();
        String json = gson.toJson(monthItems);

        outputJSON(json);
    }

    public void outputJSON(String json) {
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

    public ProfitabilityReportManagedBean getProfitabilityReportManagedBean() {
        return profitabilityReportManagedBean;
    }

    public void setProfitabilityReportManagedBean(ProfitabilityReportManagedBean profitabilityReportManagedBean) {
        this.profitabilityReportManagedBean = profitabilityReportManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
