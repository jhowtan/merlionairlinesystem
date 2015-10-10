package MAS.ManagedBean.FleetPlanning;

import MAS.Bean.FleetBean;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
public class CreateAircraftTypeManagedBean {
    @EJB
    FleetBean fleetBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String typeName;
    private int fuelCapacity;
    private int cabinReq;
    private int cockpitReq;
    private int weight;
    private double fuelEfficiency;
    private double speed;

    public void createType() {
        fleetBean.createAircraftType(typeName, fuelCapacity, getCabinReq(), getCockpitReq(), getFuelEfficiency(), getSpeed(), getWeight());
        authManagedBean.createAuditLog("Created new aircraft type: " + typeName, "create_aircraft_type");
        setTypeName(null);
        setFuelCapacity(0);
        FacesMessage m = new FacesMessage("Aircraft Type created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
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
