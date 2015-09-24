package MAS.ManagedBean;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Entity.AircraftMaintenanceSlot;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class MaintenanceSlotManagedBean {
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<AircraftMaintenanceSlot> getAllSlots() {
        return aircraftMaintenanceSlotBean.getAllSlots();
    }

    public void delete(long id) {
        try {
            AircraftMaintenanceSlot slot = aircraftMaintenanceSlotBean.getSlot(id);
            String acTailNum = slot.getAircraft().getTailNumber();
            String airport = slot.getAirport().getName();
            aircraftMaintenanceSlotBean.removeSlot(id);
            authManagedBean.createAuditLog("Deleted maintenance slot: " + acTailNum + " @ " + airport, "delete_maintenance_slot");
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The maintenance slot cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
