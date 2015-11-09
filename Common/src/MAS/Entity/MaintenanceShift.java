package MAS.Entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Daryl on 9/11/2015.
 */
@Entity
public class MaintenanceShift {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private List<User> crew;

    @ManyToMany
    public List<User> getCrew() {
        return crew;
    }

    public void setCrew(List<User> crew) {
        this.crew = crew;
    }

    private Airport airport;

    @ManyToOne
    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }
}
