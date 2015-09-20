package MAS.Entity;

import javax.persistence.*;

@Entity
public class Aircraft {

    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true)
    private String tailNumber;

    @Basic
    public String getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    private AircraftSeatConfig seatConfig;

    @ManyToOne
    public AircraftSeatConfig getSeatConfig() {
        return seatConfig;
    }

    public void setSeatConfig(AircraftSeatConfig seatConfig) {
        this.seatConfig = seatConfig;
    }
}
