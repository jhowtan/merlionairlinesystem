package MAS.Entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AircraftType {

    private String name;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private int fuelCapacity;

    @Basic
    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    private double speed;

    @Basic
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    private double fuelEfficiency;

    @Basic
    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    private int weight;

    @Basic
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int cabinCrewReq;

    @Basic
    public int getCabinCrewReq() {
        return cabinCrewReq;
    }

    public void setCabinCrewReq(int cabinCrewReq) {
        this.cabinCrewReq = cabinCrewReq;
    }

    private int cockpitCrewReq;

    @Basic
    public int getCockpitCrewReq() {
        return cockpitCrewReq;
    }

    public void setCockpitCrewReq(int cockpitCrewReq) {
        this.cockpitCrewReq = cockpitCrewReq;
    }

}
