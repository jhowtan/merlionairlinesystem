package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Flight {
    public static final int NO_STATUS = 0;
    public static final int GATE_OPEN = 1;
    public static final int BOARDING = 2;
    public static final int GATE_CLOSING = 3;
    public static final int LAST_CALL = 4;
    public static final int GATE_CLOSED = 5;
    public static final int DEPARTED = 6;

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

    private FlightGroup flightGroup;

    @ManyToOne
    public FlightGroup getFlightGroup() {
        return flightGroup;
    }

    public void setFlightGroup(FlightGroup flightGroup) {
        this.flightGroup = flightGroup;
    }

    private Date actualDepartureTime;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getActualDepartureTime() {
        return actualDepartureTime;
    }

    public void setActualDepartureTime(Date actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }

    private String gateNumber;

    @Basic
    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    private int status;

    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
