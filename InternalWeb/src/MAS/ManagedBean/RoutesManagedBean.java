package MAS.ManagedBean;

import MAS.Bean.RouteBean;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class RoutesManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Route> getAllRoutes() {
        return routeBean.getAllRoutes();
    }

    public void delete(long id) {
        try {
            String originName = routeBean.getRoute(id).getOrigin().getName();
            String destName = routeBean.getRoute(id).getDestination().getName();
            routeBean.removeRoute(id);
            authManagedBean.createAuditLog("Deleted route: " + originName + " - " +
                    destName, "delete_route");
            FacesMessage m = new FacesMessage("Successfully deleted route: " + originName + " - " + destName);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Unable to delete route, please check if there are existing aircraft assignments created for this route.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
