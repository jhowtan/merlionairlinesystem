package MAS.ManagedBean.DepartureControl;

import MAS.Bean.FlightScheduleBean;
import MAS.Entity.ETicket;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
public class CheckInManagedBean {

    @ManagedProperty(value = "#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<ETicket> relatedPassengers;
    private HashMap<Long, Boolean> relatedPassengersCheck;
    private HashMap<Long, Boolean> relatedPassengersCheckDisable;

    @EJB
    FlightScheduleBean flightScheduleBean;

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    private ETicket primaryETicket;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            primaryETicket = flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
            relatedPassengers = flightScheduleBean.getRelatedETickets(primaryETicket);
            relatedPassengersCheck = new HashMap<>();
            relatedPassengersCheckDisable = new HashMap<>();
            for (ETicket eTicket : relatedPassengers) {
                relatedPassengersCheck.put(eTicket.getId(), eTicket.getId().equals(primaryETicket.getId()));
                relatedPassengersCheckDisable.put(eTicket.getId(), eTicket.getId().equals(primaryETicket.getId()) || eTicket.isCheckedIn());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ETicket getPrimaryETicket() {
        return primaryETicket;
    }

    public HashMap<Long, Boolean> getRelatedPassengersCheck() {
        return relatedPassengersCheck;
    }

    public void setRelatedPassengersCheck(HashMap<Long, Boolean> relatedPassengersCheck) {
        this.relatedPassengersCheck = relatedPassengersCheck;
    }

    public List<ETicket> getRelatedPassengers() {
        return relatedPassengers;
    }

    public void setRelatedPassengers(List<ETicket> relatedPassengers) {
        this.relatedPassengers = relatedPassengers;
    }

    public HashMap<Long, Boolean> getRelatedPassengersCheckDisable() {
        return relatedPassengersCheckDisable;
    }

    public void setRelatedPassengersCheckDisable(HashMap<Long, Boolean> relatedPassengersCheckDisable) {
        this.relatedPassengersCheckDisable = relatedPassengersCheckDisable;
    }
}
