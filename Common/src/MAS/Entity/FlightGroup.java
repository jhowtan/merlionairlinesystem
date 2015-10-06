package MAS.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class FlightGroup {
    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private List<Flight> flights;

    @OneToMany(mappedBy = "flightGroup", cascade = CascadeType.ALL)
    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
