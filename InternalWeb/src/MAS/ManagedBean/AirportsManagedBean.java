package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

    public void delete(long id) {
        try {
            routeBean.removeAirport(id);
            authManagedBean.createAuditLog("Deleted airport: " + routeBean.getAirport(id).getName(), "delete_airport");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
