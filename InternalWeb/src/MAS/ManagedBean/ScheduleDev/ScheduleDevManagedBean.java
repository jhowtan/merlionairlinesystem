package MAS.ManagedBean.ScheduleDev;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Bean.ScheduleDevelopmentBean;
import MAS.Common.Utils;
import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class ScheduleDevManagedBean {
    @EJB
    ScheduleDevelopmentBean scheduleDevelopmentBean;
    @EJB
    FleetBean fleetBean;
    @EJB
    RouteBean routeBean;

    private List<Airport> allAirports;
    private List<String> selectAirportsId;
    private List<Airport> selectAirports;
    private List<String> hubAirportsId;
    private List<Airport> hubAirports;
    private List<Double> hubStrengths;
    private String[] hubStrInputs;

    private List<Aircraft> allAircrafts;
    private List<String> selectAircraftsId;
    private List<Aircraft> selectAircrafts;
    private String[] acLocInputs;
    private List<Airport> acLocations;

    private List<Route> allRoutes;
    private List<String> selectRoutesId;

    private String customRouteOriginId;
    private String customRouteDestinationId;

    private int duration;
    private Date startDate;
    private String startTime;
    private Date start;
    private int step = 0;

    private int flightsCreated;
    private List<String[]> routeOutputTable;

    private List<String> breadcrumbs;

    @PostConstruct
    private void init() {
        setAllAirports(routeBean.getAllAirports());
        setAllAircrafts(fleetBean.getAllAircraft());
        selectAirportsId = new ArrayList<>();
        selectAircraftsId = new ArrayList<>();
        breadcrumbs = new ArrayList<>();
        breadcrumbs.add("Select Airports");
    }

    public void selectAirportAjaxListener(AjaxBehaviorEvent event) {
        selectAirports = new ArrayList<>();
        hubAirports = new ArrayList<>();
        for (int i = 0; i < selectAirportsId.size(); i++) {
            try {
                selectAirports.add(routeBean.getAirport(selectAirportsId.get(i)));
            } catch (NotFoundException e) {
                //Airport not found
            }
        }
    }
    public void hubAirportAjaxListener(AjaxBehaviorEvent event) {
        hubAirports = new ArrayList<>();
        for (int i = 0; i < hubAirportsId.size(); i++) {
            try {
                hubAirports.add(routeBean.getAirport(hubAirportsId.get(i)));
            } catch (NotFoundException e) {
                //Airport not found
            }
        }
        hubStrInputs = new String[hubAirports.size()];
        for (int i = 0; i < hubStrInputs.length; i++)
            hubStrInputs[i] = "0.5";
    }

    public boolean showApButtons() {
        if (selectAirports == null)
            return false;
        else if (selectAirports.size() >= 2)
            return true;
        return false;
    }

    public boolean displayAp(){
        if (step == 0) return true;
        return false;
    }

    public void saveAirports() {
        try {
            hubStrengths = new ArrayList<>();
            for (int i = 0; i < hubStrInputs.length; i++) {
                hubStrengths.add(Double.parseDouble(hubStrInputs[i]));
            }

            List<String> apIds = new ArrayList<>();
            for (int i = 0; i < selectAirports.size(); i++) {
                apIds.add(selectAirports.get(i).getId());
            }
            List<String> hubIds = new ArrayList<>();
            for (int i = 0; i < hubAirports.size(); i++) {
                hubIds.add(hubAirports.get(i).getId());
            }
            scheduleDevelopmentBean.addAirports(apIds);
            scheduleDevelopmentBean.addHubs(hubIds, hubStrengths);
            step = 1;
            breadcrumbs.add("Aircraft Entry");
        } catch (NullPointerException e) {
            FacesMessage m = new FacesMessage("Flights between spoke airports require a hub airport to be present, please choose a hub airport if any.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("The airports you have chosen are no longer found on the system.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            FacesMessage m = new FacesMessage("There are airport data dependencies that exist in the system that restrict you from proceeding.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public boolean displayAc(){
        if (step == 1) return true;
        return false;
    }

    public void selectAircraftsAjaxListener(AjaxBehaviorEvent event) {
        selectAircrafts = new ArrayList<>();
        for (int i = 0; i < selectAircraftsId.size(); i++) {
            try {
                selectAircrafts.add(fleetBean.getAircraft(Long.parseLong(selectAircraftsId.get(i))));
            } catch (NotFoundException e) {
                //Aircraft not found
            }
        }
        acLocInputs = new String[selectAircrafts.size()];
        for (int i = 0; i < acLocInputs.length; i++) {
            if (selectAircrafts.get(i).getCurrentLocation() != null)
                acLocInputs[i] = selectAircrafts.get(i).getCurrentLocation().getId();
        }
    }
    public boolean showAcButtons() {
        if (selectAircrafts == null)
            return false;
        else if (selectAircrafts.size() >= 1)
            return true;
        return false;
    }

    public void saveAircraft() {
        acLocations = new ArrayList<>();
        for (int i = 0; i < acLocInputs.length; i++) {
            try {
                Airport ap = routeBean.getAirport(acLocInputs[i]);
                acLocations.add(ap);
            } catch (NotFoundException e) {
                //Cannot find airport using the code
            }
        }

        try {
            List<Long> acIds = new ArrayList<>();
            List<String> startingApIds = new ArrayList<>();
            for (int i = 0; i < selectAircrafts.size(); i++) {
                acIds.add(selectAircrafts.get(i).getId());
                startingApIds.add(acLocations.get(i).getId());
            }
            scheduleDevelopmentBean.addAircrafts(acIds, startingApIds);
            allRoutes = scheduleDevelopmentBean.processRoutes();
            routeOutputTable = scheduleDevelopmentBean.getRouteOutputTable();
            selectRoutesId = new ArrayList<>();
            for (int i = 0; i < allRoutes.size(); i++) {
                selectRoutesId.add(((Long)allRoutes.get(i).getId()).toString());
            }
            step = 2;
            breadcrumbs.add("Route Selection");

        } catch (NullPointerException e) {
            FacesMessage m = new FacesMessage("Please enter the location of these aircraft you have chosen.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("The aircraft you have chosen are no longer found on the system.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            FacesMessage m = new FacesMessage("There are aircraft data dependencies that exist in the system that restrict you from proceeding.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void addCustomRoute() {
        Route route = new Route();
        try {
            route.setOrigin(routeBean.getAirport(customRouteOriginId));
            route.setDestination(routeBean.getAirport(customRouteDestinationId));
            double distance = Utils.calculateDistance(route.getOrigin().getLatitude(), route.getOrigin().getLongitude(), route.getDestination().getLatitude(), route.getDestination().getLongitude());
            route.setDistance(distance);
            for (int i = 0; i < allRoutes.size(); i++) {
                if (allRoutes.get(i).equals(route) || allRoutes.get(i).equalsReversed(route)) {
                    return;
                }
            }
            allRoutes.add(route);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveRoutes() {
        try {
            List<Route> acceptedRoutes = new ArrayList<>();
            for (int i = 0; i < selectRoutesId.size(); i++) {
                String originId = selectRoutesId.get(i).split("\\s+")[0];
                String destinationId = selectRoutesId.get(i).split("\\s+")[1];
                for (int j = 0; j < allRoutes.size(); j++) {
                    if (allRoutes.get(j).getOrigin().getId().equals(originId))
                        if (allRoutes.get(j).getDestination().getId().equals(destinationId))
                            acceptedRoutes.add(allRoutes.get(j));
                }
            }
            scheduleDevelopmentBean.updateRoutes(acceptedRoutes);
            step = 3;
            breadcrumbs.add("Start Time Entry");
        } catch (NullPointerException e) {
            FacesMessage m = new FacesMessage("Please select viable routes from the list displayed.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            FacesMessage m = new FacesMessage("There are route data dependencies that exist in the system that restrict you from proceeding.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public boolean displayRt() {
        if (step == 2) return true;
        return false;
    }

    public boolean showRtButtons() {
        return true;
    }

    public void saveDateTime() {
        start = Utils.addTimeToDate(startDate, startTime);
        try {
            flightsCreated = scheduleDevelopmentBean.processFlights(start, duration * 60 * 24);
            step = 4;
            breadcrumbs.add("End");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean displayDate(){
        if (step == 3) return true;
        return false;
    }

    public boolean displayEnd() {
        if (step == 4) return true;
        return false;
    }

    public List<Airport> getAllAirports() {
        return allAirports;
    }

    public void setAllAirports(List<Airport> allAirports) {
        this.allAirports = allAirports;
    }

    public List<Airport> getSelectAirports() {
        return selectAirports;
    }

    public void setSelectAirports(List<Airport> selectAirports) {
        this.selectAirports = selectAirports;
    }

    public List<Airport> getHubAirports() {
        return hubAirports;
    }

    public void setHubAirports(List<Airport> hubAirports) {
        this.hubAirports = hubAirports;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<String> getSelectAirportsId() {
        return selectAirportsId;
    }

    public void setSelectAirportsId(List<String> selectAirportsId) {
        this.selectAirportsId = selectAirportsId;
    }

    public List<String> getHubAirportsId() {
        return hubAirportsId;
    }

    public void setHubAirportsId(List<String> hubAirportsId) {
        this.hubAirportsId = hubAirportsId;
    }

    public List<Aircraft> getAllAircrafts() {
        return allAircrafts;
    }

    public void setAllAircrafts(List<Aircraft> allAircrafts) {
        this.allAircrafts = allAircrafts;
    }

    public List<String> getSelectAircraftsId() {
        return selectAircraftsId;
    }

    public void setSelectAircraftsId(List<String> selectAircraftsId) {
        this.selectAircraftsId = selectAircraftsId;
    }

    public List<Aircraft> getSelectAircrafts() {
        return selectAircrafts;
    }

    public void setSelectAircrafts(List<Aircraft> selectAircrafts) {
        this.selectAircrafts = selectAircrafts;
    }

    public String[] getHubStrInputs() {
        return hubStrInputs;
    }

    public void setHubStrInputs(String[] hubStrInputs) {
        this.hubStrInputs = hubStrInputs;
    }

    public String[] getAcLocInputs() {
        return acLocInputs;
    }

    public void setAcLocInputs(String[] acLocInputs) {
        this.acLocInputs = acLocInputs;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<Route> getAllRoutes() {
        return allRoutes;
    }

    public void setAllRoutes(List<Route> allRoutes) {
        this.allRoutes = allRoutes;
    }

    public List<String> getSelectRoutesId() {
        return selectRoutesId;
    }

    public void setSelectRoutesId(List<String> selectRoutesId) {
        this.selectRoutesId = selectRoutesId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFlightsCreated() {
        return flightsCreated;
    }

    public void setFlightsCreated(int flightsCreated) {
        this.flightsCreated = flightsCreated;
    }

    public String getCustomRouteOriginId() {
        return customRouteOriginId;
    }

    public void setCustomRouteOriginId(String customRouteOriginId) {
        this.customRouteOriginId = customRouteOriginId;
    }

    public String getCustomRouteDestinationId() {
        return customRouteDestinationId;
    }

    public void setCustomRouteDestinationId(String customRouteDestinationId) {
        this.customRouteDestinationId = customRouteDestinationId;
    }

    public List<String[]> getRouteOutputTable() {
        return routeOutputTable;
    }

    public void setRouteOutputTable(List<String[]> routeOutputTable) {
        this.routeOutputTable = routeOutputTable;
    }

    public List<String> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<String> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }
}
