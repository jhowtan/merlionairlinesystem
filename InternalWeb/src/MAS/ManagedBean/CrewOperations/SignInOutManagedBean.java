package MAS.ManagedBean.CrewOperations;

import MAS.Bean.FlightRosterBean;
import MAS.Common.Utils;
import MAS.Entity.FlightRoster;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
public class SignInOutManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    FlightRosterBean flightRosterBean;

    private FlightRoster flightRoster;
    private long flightRosterId;
    private List<FlightRoster> flightRosters;

    @PostConstruct
    private void init() {
        try {
            flightRosters = flightRosterBean.getFlightRostersOfUser(authManagedBean.getUserId());
            for (int i = 0; i < flightRosters.size(); i++) {
                if (flightRosters.get(i).getFlight().getDepartureTime().compareTo(Utils.addTimeToDate(new Date(), "48:00")) >= 1)
                {
                    flightRosters.remove(i);
                    i--;
                }
            }
        } catch (Exception e) {
            flightRosters = new ArrayList<>();
        }
    }

    public void signIn() {

    }

    public boolean crewSignedIn() {
        return false;
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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
