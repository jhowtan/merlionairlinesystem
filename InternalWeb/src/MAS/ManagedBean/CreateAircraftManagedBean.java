package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Entity.AircraftSeatConfig;
import MAS.Entity.AircraftType;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;

@ManagedBean
public class CreateAircraftManagedBean {
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String tailNum;
    private Date manDate;
    private AircraftType acType;
    private AircraftSeatConfig seatConfig;
    private List<AircraftSeatConfig> updatedSeatConfig;

    public void createAircraft() {
        long aircraftId = fleetBean.createAircraft(tailNum, manDate);
        try {
            fleetBean.changeAircraftConfig(aircraftId, seatConfig.getId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        authManagedBean.createAuditLog("Created new aircraft: " + tailNum, "create_aircraft");
        setTailNum(null);
        setManDate(null);
        FacesMessage m = new FacesMessage("Aircraft created successfully.");
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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
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
}
