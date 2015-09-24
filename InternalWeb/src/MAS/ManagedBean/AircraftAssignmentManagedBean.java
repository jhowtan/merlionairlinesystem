package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.AircraftAssignment;
import MAS.Entity.City;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
            e.printStackTrace();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
