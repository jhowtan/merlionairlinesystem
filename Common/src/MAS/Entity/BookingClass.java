package MAS.Entity;

import javax.persistence.*;

@Entity
public class BookingClass {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private FareRule fareRule;

    @ManyToOne
    public FareRule getFareRule() {
        return fareRule;
    }

    public void setFareRule(FareRule fareRule) {
        this.fareRule = fareRule;
    }

    private Flight flight;

    @ManyToOne
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    private int travelClass;

    @Basic
    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }

    private String name;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int allocation;

    @Basic
    public int getAllocation() {
        return allocation;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    private int occupied;

    @Basic
    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    private boolean open;

    @Basic
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    private double price;

    @Basic
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
