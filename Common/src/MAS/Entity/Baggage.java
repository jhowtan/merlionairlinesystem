package MAS.Entity;

import javax.persistence.*;

@Entity
public class Baggage {

    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Double weight;

    @Basic
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    private FlightItem flightItem;

    @ManyToOne
    public FlightItem getFlightItem() {
        return flightItem;
    }

    public void setFlightItem(FlightItem flightItem) {
        this.flightItem = flightItem;
    }
}
