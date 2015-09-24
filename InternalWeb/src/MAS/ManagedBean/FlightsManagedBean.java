package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

@ManagedBean
public class FlightsManagedBean {
    @EJB
    FlightScheduleBean flightScheduleBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Flight> getAllFlights() {
        return flightScheduleBean.getAllFlights();
    }

    public void delete(long id) {
        try {
            String code = flightScheduleBean.getFlight(id).getCode();
            flightScheduleBean.removeFlight(id);
            authManagedBean.createAuditLog("Deleted flight: " + code, "delete_flight");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
