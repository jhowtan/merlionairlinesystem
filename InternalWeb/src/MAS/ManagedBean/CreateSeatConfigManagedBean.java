package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Common.Cabin;
import MAS.Common.SeatConfigObject;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class CreateSeatConfigManagedBean {

    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String configName;
    private String weight;
    private long acTypeId;

    private SeatConfigObject seatConfObj;
    private List<AircraftType> aircraftTypes;

    @PostConstruct
    public void init() {
        populateAcType();
        seatConfObj = new SeatConfigObject();
    }

    private void populateAcType() {
        aircraftTypes = fleetBean.getAllAircraftTypes();
    }

    public void createConfig() throws NotFoundException {
        // Decide on whether to have multiple aircraft types with the same seat configuration
        System.out.println(seatConfObj.toString());
        fleetBean.createAircraftSeatConfig(seatConfObj.toString(), configName, Integer.parseInt(weight), acTypeId);

        setConfigName(null);
        setWeight(null);
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public long getAcTypeId() {
        return acTypeId;
    }

    public void setAcTypeId(long acTypeId) {
        this.acTypeId = acTypeId;
    }

    public List<AircraftType> getAircraftTypes() {
        return aircraftTypes;
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
}

