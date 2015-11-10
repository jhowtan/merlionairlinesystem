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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
