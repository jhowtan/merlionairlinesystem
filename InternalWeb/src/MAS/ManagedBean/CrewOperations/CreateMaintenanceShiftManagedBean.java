package MAS.ManagedBean.CrewOperations;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Entity.Airport;
import MAS.Entity.MaintenanceShift;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;
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
public class CreateMaintenanceShiftManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    UserBean userBean;
    @EJB
    AircraftMaintenanceSlotBean aircraftMaintenanceSlotBean;
    @EJB
    MaintenanceShiftBean maintenanceShiftBean;
    @EJB
    RouteBean routeBean;

    private List<User> crewMembers;
    private List<User> allCrewMembers;
    private List<Airport> airports;
    private String airport;

    @PostConstruct
    public void init() {
        airports = new ArrayList<>();
        crewMembers = new ArrayList<>();
        airports = routeBean.getAllAirports();
        try {
            allCrewMembers = userBean.getUsersAtAirportWithJob(airports.get(0).getId(), Constants.maintenanceCrewJobId);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void airportChangeListener(AjaxBehaviorEvent event) {
        try {
            allCrewMembers = userBean.getUsersAtAirportWithJob(airport, Constants.maintenanceCrewJobId);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Cannot find users for chosen airport in system, airport may not exist.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void createShift() {
        MaintenanceShift maintenanceShift = new MaintenanceShift();
        maintenanceShift.setCrew(crewMembers);
        try {
            maintenanceShift.setAirport(routeBean.getAirport(airport));
            maintenanceShiftBean.createMaintenanceShift(maintenanceShift);
            FacesMessage m = new FacesMessage("Created maintenance shift for chosen users successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Unable to create maintenance shift for chosen users.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);        }
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

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }
}
