package MAS.ManagedBean.CrewOperations;

import MAS.Bean.MaintenanceShiftBean;
import MAS.Bean.UserBean;
import MAS.Entity.Airport;
import MAS.Entity.MaintenanceShift;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class MaintenanceShiftManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    @EJB
    UserBean userBean;
    @EJB
    MaintenanceShiftBean maintenanceShiftBean;

    private MaintenanceShift maintenanceShift;

    public List<MaintenanceShift> getAllMaintenanceShifts() {
        try {
            Airport baseAirport = userBean.getUser(authManagedBean.getUserId()).getBaseAirport();
            return maintenanceShiftBean.findMaintenanceShiftsByAirport(baseAirport.getId());
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Cannot find any maintenance shifts because the user or airport is invalid");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
        return null;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public boolean displayDetails() {
        return maintenanceShift != null;
    }

    public void viewMaintenanceShift(long id) {
        try {
            maintenanceShift = maintenanceShiftBean.getMaintenanceShift(id);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Cannot find view this maintenance shift for it may not exist.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void removeShift() {
        try {
            maintenanceShiftBean.deleteMaintenanceShift(maintenanceShift.getId());
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Unable to delete this maintenance shift for it may not exist.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public MaintenanceShift getMaintenanceShift() {
        return maintenanceShift;
    }

    public void setMaintenanceShift(MaintenanceShift maintenanceShift) {
        this.maintenanceShift = maintenanceShift;
    }
}
