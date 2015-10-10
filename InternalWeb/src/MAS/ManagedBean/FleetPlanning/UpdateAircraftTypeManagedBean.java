package MAS.ManagedBean.FleetPlanning;

import MAS.Bean.FleetBean;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
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
public class UpdateAircraftTypeManagedBean {
    @EJB
    private FleetBean fleetBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private AircraftType type;
    private String typeName;
    private int fuelCapacity;
    private int cabinReq;
    private int cockpitReq;
    private int weight;
    private double fuelEfficiency;
    private double speed;
    private Map<String,String> params;
    private List<AircraftSeatConfig> seatConfigs;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long typeId = Long.parseLong(params.get("typeId"));
        getAircraftType(typeId);
        populateSeatConfig(typeId);
    }

    private void getAircraftType(long id) {
        try {
            type = fleetBean.getAircraftType(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        typeName = type.getName();
        fuelCapacity = type.getFuelCapacity();
        cabinReq = type.getCabinCrewReq();
        cockpitReq = type.getCockpitCrewReq();
        weight = type.getWeight();
        fuelEfficiency = type.getFuelEfficiency();
        speed = type.getSpeed();
    }

    private void populateSeatConfig(long typeId) {
        seatConfigs = fleetBean.findSeatConfigByType(typeId);
    }

    public void save() throws NotFoundException {
        try {
            fleetBean.changeAircraftTypeName(type.getId(), typeName);
            fleetBean.changeAircraftTypeFuelCap(type.getId(), fuelCapacity);
            fleetBean.changeAircraftTypeCabinReq(type.getId(), cabinReq);
            fleetBean.changeAircraftTypeCockpitReq(type.getId(), cockpitReq);
            fleetBean.changeAircraftTypeFuelEff(type.getId(), fuelEfficiency);
            fleetBean.changeAircraftTypeSpeed(type.getId(), speed);
            fleetBean.changeAircraftTypeWeight(type.getId(), weight);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated aircraft type information: " + typeName, "update_aircraft_type");

        init();

        FacesMessage m = new FacesMessage("Aircraft type details updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void deleteSeatConfig(long id) throws NotFoundException{
        fleetBean.removeAircraftSeatConfig(id);
        authManagedBean.createAuditLog("Deleted Aircraft Seat Configuration: " + id, "delete_seat_config");
        populateSeatConfig(type.getId());

        FacesMessage m = new FacesMessage("The aircraft seat configuration has been deleted successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public AircraftType getType() {
        return type;
    }

    public void setType(AircraftType type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public List<AircraftSeatConfig> getSeatConfigs() {
        return seatConfigs;
    }

    public void setSeatConfigs(List<AircraftSeatConfig> seatConfigs) {
        this.seatConfigs = seatConfigs;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public int getCabinReq() {
        return cabinReq;
    }

    public void setCabinReq(int cabinReq) {
        this.cabinReq = cabinReq;
    }

    public int getCockpitReq() {
        return cockpitReq;
    }

    public void setCockpitReq(int cockpitReq) {
        this.cockpitReq = cockpitReq;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
