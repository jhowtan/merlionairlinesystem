package MAS.ManagedBean.FleetPlanning;

import MAS.Bean.FleetBean;
import MAS.Entity.Aircraft;
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
public class AircraftManagedBean {
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Aircraft> getAllAircraft() {
        return fleetBean.getAllAircraft();
    }

    public void delete(long id) {
        try {
            String tailNumber = fleetBean.getAircraft(id).getTailNumber();
            fleetBean.removeAircraft(id);
            authManagedBean.createAuditLog("Deleted aircraft: " + tailNumber, "delete_aircraft");
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("Unable to delete aircraft, please check if there are existing assignments made for this aircraft.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The aircraft cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
