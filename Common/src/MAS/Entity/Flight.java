package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Flight {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String code;

    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private Date departureTime;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    private AircraftAssignment aircraftAssignment;

    @ManyToOne
    public AircraftAssignment getAircraftAssignment() {
        return aircraftAssignment;
    }

    public void setAircraftAssignment(AircraftAssignment aircraftAssignment) {
        this.aircraftAssignment = aircraftAssignment;
    }

    private Date arrivalTime;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
