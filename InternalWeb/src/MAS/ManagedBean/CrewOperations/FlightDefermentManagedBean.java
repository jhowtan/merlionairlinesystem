package MAS.ManagedBean.CrewOperations;

import MAS.Bean.FlightDefermentBean;
import MAS.Bean.FlightRosterBean;
import MAS.Entity.FlightRoster;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
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

    @PostConstruct
    private void init() {
        try {
            flightRosters = flightRosterBean.getFlightRostersOfUser(authManagedBean.getUserId());
        } catch (Exception e) {
            flightRosters = new ArrayList<>();
        }
    }

    public void flightRosterChangeListener(AjaxBehaviorEvent event) {

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
}
