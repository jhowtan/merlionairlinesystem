package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class CreateAircraftAssignmentManagedBean {
    @EJB
    RouteBean routeBean;
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<Route> routes;
    private List<Aircraft> aircrafts;

    private long aircraftId;
    private long routeId;

    @PostConstruct
    public void init() {
        setAircrafts(fleetBean.getAllAircraft());
        setRoutes(routeBean.getAllRoutes());
    }

    public void createAircraftAssignment() throws NotFoundException {
        routeBean.createAircraftAssignment(aircraftId, routeId);
        authManagedBean.createAuditLog("Created new aircraft assignment: " + fleetBean.getAircraft(aircraftId).getTailNumber() + " : " +
                routeBean.getRoute(routeId).getOrigin().getName() + " - " + routeBean.getRoute(routeId).getDestination().getName(), "create_aircraft_assignment");
        setAircraftId(0);
        setRouteId(0);
        FacesMessage m = new FacesMessage("Route created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public String formatRoute(Route route) {
        return route.getOrigin().getName() + " - " + route.getDestination().getName();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }
}
