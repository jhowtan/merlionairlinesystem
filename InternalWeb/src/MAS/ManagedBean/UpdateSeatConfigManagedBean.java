package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Common.Cabin;
import MAS.Common.SeatConfigObject;
import MAS.Entity.AircraftSeatConfig;
import MAS.Exception.NotFoundException;

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
        long scId = Long.parseLong(params.get("seatConfigId"));
        getSeatConfig(scId);
        seatConfObj = new SeatConfigObject();
    }

    private void getSeatConfig(long id) {
        try {
            aircraftSeatConfig = fleetBean.getAircraftSeatConfig(id);
            configName = aircraftSeatConfig.getName();
            weight = aircraftSeatConfig.getWeight();
            seatConfigString = aircraftSeatConfig.getSeatConfig();
            System.out.println(seatConfigString);
            acTypeId = aircraftSeatConfig.getAircraftType().getId();
            setAcTypeName(aircraftSeatConfig.getAircraftType().getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateConfig() throws NotFoundException {
        // Decide on whether to have multiple aircraft types with the same seat configuration
        System.out.println(seatConfObj.toString());
        fleetBean.createAircraftSeatConfig(seatConfObj.toString(), configName, weight, acTypeId);

        setConfigName(null);
        setWeight(0);
        seatConfObj = new SeatConfigObject();
        FacesMessage m = new FacesMessage("Seat Configuration created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
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
}

