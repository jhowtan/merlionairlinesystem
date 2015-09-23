package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Bean.UserBean;
import MAS.Entity.AircraftType;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
public class CreateSeatConfigManagedBean {

    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String configName;
    private String weight;
    private String seatConfig;

    private List<AircraftType> aircraftTypes;
    private Map<Long, Boolean> aircraftTypesMap;

    @PostConstruct
    public void init() {
        populateAcType();
    }

    private void populateAcType() {
        aircraftTypes = fleetBean.getAllAircraftTypes();
        aircraftTypesMap = new HashMap<>();
        for (AircraftType acType : aircraftTypes) {
            aircraftTypesMap.put(acType.getId(), Boolean.FALSE);
        }
    }

    public void createConfig() throws NotFoundException {
        ArrayList<Long> acTypeIds = new ArrayList<>();
        for (Object o : aircraftTypesMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if ((Boolean) pair.getValue()) {
                acTypeIds.add((Long) pair.getKey());
            }
        }
        // Decide on whether to have multiple aircraft types with the same seat configuration
        fleetBean.createAircraftSeatConfig(seatConfig, configName, Integer.parseInt(weight), acTypeIds);

        populateAcType();
        setConfigName(null);
        setWeight(null);
        setSeatConfig(null);
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

    public String getSeatConfig() {
        return seatConfig;
    }

    public void setSeatConfig(String seatConfig) {
        this.seatConfig = seatConfig;
    }

    public List<AircraftType> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftType> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public Map<Long, Boolean> getAircraftTypesMap() {
        return aircraftTypesMap;
    }

    public void setAircraftTypesMap(Map<Long, Boolean> aircraftTypesMap) {
        this.aircraftTypesMap = aircraftTypesMap;
    }
}

