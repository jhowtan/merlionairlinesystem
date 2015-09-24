package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Entity.AircraftType;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class AircraftTypeManagedBean {
    @EJB
    FleetBean fleetBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<AircraftType> getAllAircraftTypes() {
        return fleetBean.getAllAircraftTypes();
    }

    public long getAircraftCount(long typeId) {
        return fleetBean.getAircraftCountByType(typeId);
    }

    public void delete(long typeId) {
        try {
            String aircraftTypename = fleetBean.getAircraftType(typeId).getName();
            fleetBean.removeAircraftType(typeId);
            authManagedBean.createAuditLog("Deleted aircraft type: " + aircraftTypename, "delete_aircraft_type");
            FacesMessage m = new FacesMessage("Successfully deleted aircraft type: " + aircraftTypename);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Unable to delete aircraft type, please check if there are any existing seat " +
                    "configurations or aircraft created for this aircraft type.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

}
