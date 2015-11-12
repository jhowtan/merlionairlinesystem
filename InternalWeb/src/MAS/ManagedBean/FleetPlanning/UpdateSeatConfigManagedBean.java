package MAS.ManagedBean.FleetPlanning;

import MAS.Bean.FleetBean;
import MAS.Common.Cabin;
import MAS.Common.SeatConfigObject;
import MAS.Entity.AircraftSeatConfig;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Map;

@ManagedBean
//@ViewScoped
public class UpdateSeatConfigManagedBean {
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private AircraftSeatConfig  aircraftSeatConfig;
    private String configName;
    private int weight;
    private long acTypeId;
    private String acTypeName;
    private String seatConfigString;

    private SeatConfigObject seatConfObj;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long seatConfigId = Long.parseLong(params.get("seatConfigId"));
        getSeatConfig(seatConfigId);
    }

    private void getSeatConfig(long id) {
        try {
            setAircraftSeatConfig(fleetBean.getAircraftSeatConfig(id));
            configName = getAircraftSeatConfig().getName();
            weight = getAircraftSeatConfig().getWeight();
            seatConfigString = getAircraftSeatConfig().getSeatConfig();
            acTypeId = getAircraftSeatConfig().getAircraftType().getId();
            setAcTypeName(getAircraftSeatConfig().getAircraftType().getName());
            seatConfObj = new SeatConfigObject();
            seatConfObj.parse(seatConfigString);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateConfig() {
        try {
            fleetBean.changeSeatConfigWeight(aircraftSeatConfig.getId(), weight);
            fleetBean.changeName(aircraftSeatConfig.getId(), configName);

            FacesMessage m = new FacesMessage("Seat Configuration updated successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
            authManagedBean.createAuditLog("Updated Seat Configuration: " + configName, "update_seat_config");
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Seat Configuration could not be updated.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getAcTypeId() {
        return acTypeId;
    }

    public void setAcTypeId(long acTypeId) {
        this.acTypeId = acTypeId;
    }

    public void addSeat(int cabin) {
        if (seatConfObj.selectCabin(cabin))
            seatConfObj.addSeat();
    }

    public void addCorridor(int cabin) {
        if (seatConfObj.selectCabin(cabin))
            seatConfObj.addCorridor();
    }

    public void removeSeat(int cabin) {
        if (seatConfObj.selectCabin(cabin))
            seatConfObj.removeLast();
    }

    public ArrayList<Cabin> getCabins() {
        return seatConfObj.getCabins();
    }

    public void addCabin() {
        seatConfObj.addCabin();
    }

    public int getIndexOf(Cabin cabin) {
        return seatConfObj.getCabins().indexOf(cabin);
    }

    public String getSeatConfigString() {
        return seatConfigString;
    }

    public void setSeatConfigString(String seatConfigString) {
        this.seatConfigString = seatConfigString;
    }

    public String getAcTypeName() {
        return acTypeName;
    }

    public void setAcTypeName(String acTypeName) {
        this.acTypeName = acTypeName;
    }

    public AircraftSeatConfig getAircraftSeatConfig() {
        return aircraftSeatConfig;
    }

    public void setAircraftSeatConfig(AircraftSeatConfig aircraftSeatConfig) {
        this.aircraftSeatConfig = aircraftSeatConfig;
    }

    public int seatsInClass(int travelClass) {
        return seatConfObj.getSeatsInClass(travelClass);
    }
}

