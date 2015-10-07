package MAS.Entity;

import javax.persistence.*;

@Entity
public class CostAAOccurence { //AKA Cost per flight
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private double amount;

    @Basic
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private String comments;

    @Basic
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private AircraftAssignment aircraftAssignment;

    @ManyToOne
    public AircraftAssignment getAircraftAssignment() {
        return aircraftAssignment;
    }

    public void setAircraftAssignment(AircraftAssignment aircraftAssignment) {
        this.aircraftAssignment = aircraftAssignment;
    }
}
