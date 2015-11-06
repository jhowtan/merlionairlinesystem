package MAS.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class FlightRoster {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Flight flight;

    @OneToOne
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    private List<User> members;

    @ManyToMany
    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    private boolean complete;

    @Basic
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
