package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Entity.Aircraft;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UpdateAircraftManagedBean {
    @EJB
    private FleetBean fleetBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private Aircraft aircraft;
    private long seatConfig;
    private List<AircraftSeatConfig> seatConfigList;

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
        seatConfigList = fleetBean.findSeatConfigByType(aircraft.getSeatConfig().getAircraftType().getId());
    }

    public void save() throws NotFoundException {
        try {
            fleetBean.changeAircraftConfig(aircraft.getId(), seatConfig);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated aircraft information: " + aircraft.getTailNumber(), "update_aircraft");

        FacesMessage m = new FacesMessage("Aircraft has been successfully updated.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
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
}
