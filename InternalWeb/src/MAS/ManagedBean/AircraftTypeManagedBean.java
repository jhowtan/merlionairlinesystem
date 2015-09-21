package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

    public Long getAircraftCount(long typeId) {
        return fleetBean.getAircraftCountByType(typeId);
    }

    public void delete(long typeId) {
        try {
            String aircraftTypename = fleetBean.getAircraftType(typeId).getName();
            fleetBean.removeAircraftType(typeId);
            authManagedBean.createAuditLog("Deleted aircraft type: " + aircraftTypename, "delete_aircraft_type");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

}
