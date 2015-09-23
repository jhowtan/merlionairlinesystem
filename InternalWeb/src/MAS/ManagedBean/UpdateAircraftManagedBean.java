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

    private String tailNum;
    private Date manDate;
    private Aircraft aircraft;
    private AircraftType acType;
    private AircraftSeatConfig seatConfig;
    private List<AircraftSeatConfig> updatedSeatConfig;
    private Map<String,String> params;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long acId = Long.parseLong(params.get("acId"));
        getAircraft(acId);
    }

    private void getAircraft(long id) {
        try {
            aircraft = fleetBean.getAircraft(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        tailNum = aircraft.getTailNumber();
        manDate = aircraft.getManufacturedDate();
        seatConfig = aircraft.getSeatConfig();
        acType = seatConfig.getAircraftType();
    }

    public void save() throws NotFoundException {
        try {
            fleetBean.changeAircraftConfig(aircraft.getId(), seatConfig.getId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated aircraft information: " + tailNum, "update_aircraft");

        init();

        FacesMessage m = new FacesMessage("Aircraft configurations are updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public List<AircraftSeatConfig> getAllSeatConfig() {
        return fleetBean.getAllAircraftSeatConfigs();
    }

    public void updateSeatConfigOnTypeSelect(long id) {
        updatedSeatConfig = fleetBean.findSeatConfigByType(id);
    }

    public List<AircraftType> getAllAircraftTypes() {
        return fleetBean.getAllAircraftTypes();
    }

    public String getTailNum() {
        return tailNum;
    }

    public void setTailNum(String tailNum) {
        this.tailNum = tailNum;
    }

    public Date getManDate() {
        return manDate;
    }

    public void setManDate(Date manDate) {
        this.manDate = manDate;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public AircraftType getAcType() {
        return acType;
    }

    public void setAcType(AircraftType acType) {
        this.acType = acType;
    }

    public AircraftSeatConfig getSeatConfig() {
        return seatConfig;
    }

    public void setSeatConfig(AircraftSeatConfig seatConfig) {
        this.seatConfig = seatConfig;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
