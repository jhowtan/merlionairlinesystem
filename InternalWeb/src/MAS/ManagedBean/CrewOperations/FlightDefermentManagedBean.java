package MAS.ManagedBean.CrewOperations;

import MAS.Bean.FlightDefermentBean;
import MAS.Bean.FlightRosterBean;
import MAS.Common.Permissions;
import MAS.Common.Utils;
import MAS.Entity.FlightDeferment;
import MAS.Entity.FlightRoster;
import MAS.Entity.User;
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
import java.util.Date;
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
    private List<FlightDeferment> flightDeferments;
    private String reason;
    private FlightDeferment flightDeferment;
    private List<User> availableReplacements;

    @PostConstruct
    private void init() {
        try {
            if (!isCrewManager()) {
                flightRosters = flightRosterBean.getFlightRostersOfUser(authManagedBean.getUserId());
                for (int i = 0; i < flightRosters.size(); i++) {
                    if (flightRosters.get(i).getFlight().getDepartureTime().compareTo(Utils.addTimeToDate(new Date(), "24:00")) >= 1)
                    {
                        flightRosters.remove(i);
                        i--;
                    }
                }
            }
            else
                flightDeferments = flightDefermentBean.getUnresolvedDeferments();
        } catch (Exception e) {
            flightRosters = new ArrayList<>();
        }
    }

    public void resolve(FlightDeferment flightDeferment) {
        this.flightDeferment = flightDeferment;
        if (flightDeferment != null) {//get available replacements
            availableReplacements = flightRosterBean.getReplacement(flightDeferment.getFlightRoster().getFlight(), flightDeferment.getDeferrer().getJob());
        }
    }

    public void replaceFlightCrew(User replacement) {

    }

    public boolean hasFlightDeferment() {
        return flightDeferment != null;
    }

    public boolean isCrewManager() {
        return authManagedBean.hasPermission(Permissions.MANAGE_FLIGHT_BID);
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

    public List<FlightDeferment> getFlightDeferments() {
        return flightDeferments;
    }

    public void setFlightDeferments(List<FlightDeferment> flightDeferments) {
        this.flightDeferments = flightDeferments;
    }

    public FlightDeferment getFlightDeferment() {
        return flightDeferment;
    }

    public void setFlightDeferment(FlightDeferment flightDeferment) {
        this.flightDeferment = flightDeferment;
    }

    public List<User> getAvailableReplacements() {
        return availableReplacements;
    }

    public void setAvailableReplacements(List<User> availableReplacements) {
        this.availableReplacements = availableReplacements;
    }
}
