package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class CreateRouteManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<Airport> airports;

    private String originId;
    private String destinationId;

    @PostConstruct
    public void init() {
        setAirports(routeBean.getAllAirports());
    }

    public void createRoute() {
        if (originId == destinationId) {
            FacesMessage m = new FacesMessage("Origin and destination of the route must be different.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
        try {
            routeBean.createRoute(originId, destinationId);
            authManagedBean.createAuditLog("Created new route: " + routeBean.getAirport(originId).getName() + " - " +
                    routeBean.getAirport(destinationId).getName(), "create_route");
            setOriginId(null);
            setDestinationId(null);
            FacesMessage m = new FacesMessage("Route created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Route could not be created.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
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
}
