package MAS.ManagedBean.FleetPlanning;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
import MAS.Entity.Airport;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UpdateAircraftManagedBean {
    @EJB
    private FleetBean fleetBean;
    @EJB
    private RouteBean routeBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private Aircraft aircraft;
    private long seatConfig;
    private String currentApId;
    private List<AircraftSeatConfig> seatConfigList;
    private List<Airport> airportList;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long acId = Long.parseLong(params.get("acId"));
        getAircraft(acId);
    }

    private void getAircraft(long id) {
        try {
            aircraft = fleetBean.getAircraft(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        seatConfig = aircraft.getSeatConfig().getId();
        currentApId = aircraft.getCurrentLocation().getId();
        seatConfigList = fleetBean.findSeatConfigByType(aircraft.getSeatConfig().getAircraftType().getId());
        airportList = routeBean.getAllAirports();
    }

    public void save() {
        try {
            fleetBean.changeAircraftConfig(aircraft.getId(), seatConfig);
            fleetBean.changeAircraftLocation(aircraft.getId(), currentApId);

            authManagedBean.createAuditLog("Updated aircraft information: " + aircraft.getTailNumber(), "update_aircraft");

            FacesMessage m = new FacesMessage("Aircraft has been successfully updated.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Aircraft could not be updated.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public List<AircraftSeatConfig> getAllSeatConfig() {
        return fleetBean.getAllAircraftSeatConfigs();
    }

    public List<AircraftType> getAllAircraftTypes() {
        return fleetBean.getAllAircraftTypes();
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public long getSeatConfig() {
        return seatConfig;
    }

    public void setSeatConfig(long seatConfig) {
        this.seatConfig = seatConfig;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<AircraftSeatConfig> getSeatConfigList() {
        return seatConfigList;
    }

    public void setSeatConfigList(List<AircraftSeatConfig> seatConfigList) {
        this.seatConfigList = seatConfigList;
    }

    public String getCurrentApId() {
        return currentApId;
    }

    public void setCurrentApId(String currentApId) {
        this.currentApId = currentApId;
    }

    public List<Airport> getAirportList() {
        return airportList;
    }

    public void setAirportList(List<Airport> airportList) {
        this.airportList = airportList;
    }
}
