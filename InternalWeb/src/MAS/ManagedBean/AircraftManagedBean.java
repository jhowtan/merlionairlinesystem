package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Entity.Aircraft;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
