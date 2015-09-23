package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

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
    }

    private void populateSeatConfig(long typeId) {
        seatConfigs = fleetBean.findSeatConfigByType(typeId);
    }

    public void save() throws NotFoundException {
        try {
            fleetBean.changeAircraftTypeName(type.getId(), typeName);
            fleetBean.changeFuelCapacity(type.getId(), fuelCapacity);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Updated aircraft type information: " + typeName, "update_aircraft_type");

        init();

        FacesMessage m = new FacesMessage("Aircraft type details updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void deleteSeatConfig(long id, String name) throws NotFoundException{
        try {
            fleetBean.removeAircraftSeatConfig(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        authManagedBean.createAuditLog("Deleted Aircraft Seat Configuration: " + name, "delete_seat_config");

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
}
