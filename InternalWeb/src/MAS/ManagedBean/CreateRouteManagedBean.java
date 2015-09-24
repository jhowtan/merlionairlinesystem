package MAS.ManagedBean;

import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.Entity.City;
import MAS.Entity.Country;
import MAS.Exception.NotFoundException;

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

    private long originId;
    private long destinationId;

    @PostConstruct
    public void init() {
        setAirports(routeBean.getAllAirports());
    }

    public void createRoute() throws NotFoundException {
        routeBean.createRoute(originId, destinationId);
        authManagedBean.createAuditLog("Created new route: " + routeBean.getAirport(originId).getName() + " - " +
                routeBean.getAirport(destinationId).getName(), "create_route");
        setOriginId(0);
        setDestinationId(0);
        FacesMessage m = new FacesMessage("Route created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
