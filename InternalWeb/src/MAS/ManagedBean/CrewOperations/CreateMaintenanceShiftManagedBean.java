package MAS.ManagedBean.CrewOperations;

import MAS.Bean.AircraftMaintenanceSlotBean;
import MAS.Bean.MaintenanceShiftBean;
import MAS.Bean.UserBean;
import MAS.Common.Constants;
import MAS.Entity.Airport;
import MAS.Entity.MaintenanceShift;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.RoutePlanning.AirportsManagedBean;

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
public class CreateMaintenanceShiftManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    UserBean userBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @EJB
    AirportsManagedBean airportsManagedBean;
    @EJB
    MaintenanceShiftBean maintenanceShiftBean;

    private List<User> crewMembers;
    private List<User> allCrewMembers;
    private List<Airport> airports;
    private Airport airport;

    @PostConstruct
    public void init() {
        airports = new ArrayList<>();
        crewMembers = new ArrayList<>();
        allCrewMembers = new ArrayList<>();
        airports = airportsManagedBean.getAllAirports();
    }

    public void airportChangeListener(AjaxBehaviorEvent event) {
        try {
            allCrewMembers = userBean.getUsersAtAirportWithJob(airport.getId(), Constants.maintenanceCrewJobId);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Cannot find users for chosen airport in system, airport may not exist.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void createShift() {
        MaintenanceShift maintenanceShift = new MaintenanceShift();
        maintenanceShift.setCrew(crewMembers);
        maintenanceShift.setAirport(airport);
        maintenanceShiftBean.createMaintenanceShift(maintenanceShift);
        FacesMessage m = new FacesMessage("Created maintenance shift for chosen users successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<User> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<User> crewMembers) {
        this.crewMembers = crewMembers;
    }

    public List<User> getAllCrewMembers() {
        return allCrewMembers;
    }

    public void setAllCrewMembers(List<User> allCrewMembers) {
        this.allCrewMembers = allCrewMembers;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }
}
