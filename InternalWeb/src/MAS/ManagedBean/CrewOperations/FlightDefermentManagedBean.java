package MAS.ManagedBean.CrewOperations;

import MAS.Bean.FlightDefermentBean;
import MAS.Bean.FlightRosterBean;
import MAS.Entity.FlightRoster;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class FlightDefermentManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    FlightDefermentBean flightDefermentBean;
    @EJB
    FlightRosterBean flightRosterBean;

    private FlightRoster flightRoster;
    private long flightRosterId;
    private List<FlightRoster> flightRosters;
    private String reason;

    @PostConstruct
    private void init() {
        try {
            flightRosters = flightRosterBean.getFlightRostersOfUser(authManagedBean.getUserId());
        } catch (Exception e) {
            flightRosters = new ArrayList<>();
        }
    }

    public void flightRosterChangeListener(AjaxBehaviorEvent event) {
        try {
            flightRoster = flightRosterBean.getFlightRoster(flightRosterId);
        } catch (Exception e) {
            flightRoster = null;
        }
    }

    public void saveFlightDeferment() {
        try {
            flightDefermentBean.createFlightDeferment(authManagedBean.getUserId(), flightRoster.getId(), reason);
            flightRosterId = 0;
            reason = "";
            FacesMessage m = new FacesMessage("Flight deferment submitted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Unable to submit flight deferment.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public boolean hasFlightRoster() {
        return flightRoster != null;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public FlightRoster getFlightRoster() {
        return flightRoster;
    }

    public void setFlightRoster(FlightRoster flightRoster) {
        this.flightRoster = flightRoster;
    }

    public long getFlightRosterId() {
        return flightRosterId;
    }

    public void setFlightRosterId(long flightRosterId) {
        this.flightRosterId = flightRosterId;
    }

    public List<FlightRoster> getFlightRosters() {
        return flightRosters;
    }

    public void setFlightRosters(List<FlightRoster> flightRosters) {
        this.flightRosters = flightRosters;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
