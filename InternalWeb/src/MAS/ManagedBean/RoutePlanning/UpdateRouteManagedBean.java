package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.Airport;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UpdateRouteManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<Airport> airports;
    private Map<String,String> params;
    private boolean editable;

    private Route route;

    private String originId;
    private String destinationId;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long rtId = Long.parseLong(params.get("rtId"));
        getRoute(rtId);
    }

    private void getRoute(long id) {
        try {
            route = routeBean.getRoute(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        originId = route.getOrigin().getId();
        destinationId = route.getDestination().getId();
        airports = routeBean.getAllAirports();
    }

    public void save() throws NotFoundException {
        if (originId == destinationId) {
            FacesMessage m = new FacesMessage("Origin and destination cannot be the same.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
            return;
        }
        try {
            List<AircraftAssignment> aircraftAssignments = routeBean.findAAByRoute(route.getId());
            if (aircraftAssignments.size() > 0) {
                editable = false;
                FacesMessage m = new FacesMessage("Route cannot be edited due to assigned flights to this route.");
                m.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage("status", m);
            }
        }
        catch (Exception e) {
            editable = true;
        }
        routeBean.updateRoute(route.getId(), originId, destinationId);
        authManagedBean.createAuditLog("Updated route: " + routeBean.getAirport(originId).getName() + " - " +
                routeBean.getAirport(destinationId).getName(), "update_route");
        FacesMessage m = new FacesMessage("Route updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
