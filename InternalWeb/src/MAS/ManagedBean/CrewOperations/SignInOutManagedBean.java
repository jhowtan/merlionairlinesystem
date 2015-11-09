package MAS.ManagedBean.CrewOperations;

import MAS.Bean.FlightRosterBean;
import MAS.Bean.UserBean;
import MAS.Common.Utils;
import MAS.Entity.FlightRoster;
import MAS.Entity.User;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
public class SignInOutManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    FlightRosterBean flightRosterBean;
    @EJB
    UserBean userBean;

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
                List<User> signedOut = flightRosters.get(i).getSignedOut();
                for (User user : signedOut) {
                    if (user.equals(userBean.getUser(authManagedBean.getUserId()))) {
                        flightRosters.remove(i);
                        i--;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            flightRosters = new ArrayList<>();
        }
    }

    public void signIn() {
        try {
            flightRosterBean.signInFR(flightRosterId, authManagedBean.getUserId());
            FacesMessage m = new FacesMessage("Signed in for flight " + flightRosterBean.getFlightRoster(flightRosterId).getFlight().getCode());
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Could not sign in to flight.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void signOut() {
        try {
            flightRosterBean.signOutFR(flightRoster.getId(), authManagedBean.getUserId());
            FacesMessage m = new FacesMessage("Signed out for flight " + flightRoster.getFlight().getCode());
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Could not sign out from flight.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public String signOutProblem() {
        signOut();
        return "flightReports&faces-redirect=true";
    }

    public String getSignOutText() {
        try {
            String result = "You are currently signed in to flight ";
            result = result.concat(flightRoster.getFlight().getCode() + ", but have not yet signed out.");
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public boolean crewSignedIn() {
        try {
            for (int i = 0; i < flightRosters.size(); i++) {
                User user = userBean.getUser(authManagedBean.getUserId());
                List<User> signedIn = flightRosters.get(i).getSignedIn();
                for (int j = 0; j < signedIn.size(); j++) {
                    if (signedIn.get(j).equals(user)) {
                        List<User> signedOut = flightRosters.get(i).getSignedOut();
                        boolean isOut = false;
                        for (int k = 0; k < signedOut.size(); k++) {
                            if (signedOut.get(k).equals(user)) {
                                isOut = true;
                                break;
                            }
                        }
                        if (!isOut) {
                            flightRoster = flightRosters.get(i);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
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
