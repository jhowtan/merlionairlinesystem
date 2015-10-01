package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.AircraftAssignment;
import MAS.Exception.IntegrityConstraintViolationException;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class AircraftAssignmentManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<AircraftAssignment> getAllAircraftAssignments() {
        return routeBean.getAllAircraftAssignments();
    }

    public void delete(long id) {
        try {
            String tailNum = routeBean.getAircraftAssignment(id).getAircraft().getTailNumber();
            String origin = routeBean.getAircraftAssignment(id).getRoute().getOrigin().getName();
            String destination = routeBean.getAircraftAssignment(id).getRoute().getDestination().getName();
            routeBean.removeAircraftAssignment(id);
            authManagedBean.createAuditLog("Deleted aircraft assignment: " + tailNum
                    + " : " + origin + " - " + destination, "delete_aircraft_assignment");
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("The aircraft assignment cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (IntegrityConstraintViolationException e) {
            FacesMessage m = new FacesMessage("The aircraft cannot be unassigned because a flight already exists.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
