package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class AirportsManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Airport> getAllAirports() {
        return routeBean.getAllAirports();
    }

    public void delete(String id) {
        try {
            routeBean.removeAirport(id);
            authManagedBean.createAuditLog("Deleted airport: " + routeBean.getAirport(id).getName(), "delete_airport");
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("Unable to delete the airport. There is an existing route that utilizes this airport.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The airport cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
