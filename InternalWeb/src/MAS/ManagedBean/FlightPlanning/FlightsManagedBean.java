package MAS.ManagedBean.FlightPlanning;

import MAS.Bean.CostsBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Entity.Flight;
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
public class FlightsManagedBean {
    @EJB
    FlightScheduleBean flightScheduleBean;

    @EJB
    CostsBean costsBean;

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
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("Unable to delete flight, please check if there are existing booking classes created for this flight.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The flight cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public double estimatedCost(long flightId) {
        try {
            return costsBean.calculateCostPerFlight(flightId);
        } catch (NotFoundException e) {
            return 0;
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
