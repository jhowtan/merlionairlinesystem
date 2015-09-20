package MAS.Entity;

import javax.persistence.*;

@Entity
public class AircraftSeatConfig {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String seatConfig;

    @Basic
    public String getSeatConfig() {
        return seatConfig;
    }

    public void setSeatConfig(String seatConfig) {
        this.seatConfig = seatConfig;
    }

    private int weight;

    @Basic
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private AircraftType aircraftType;

    @ManyToOne
    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }
}
