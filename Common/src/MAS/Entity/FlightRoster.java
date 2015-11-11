package MAS.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class FlightRoster implements Comparable<FlightRoster> {
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
    @JoinTable(name="flightroster_members")
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

    private List<User> signedIn;

    @ManyToMany
    @JoinTable(name="flightroster_signedin")
    public List<User> getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(List<User> signedIn) {
        this.signedIn = signedIn;
    }

    private List<User> signedOut;

    @ManyToMany
    @JoinTable(name="flightroster_signedout")
    public List<User> getSignedOut() {
        return signedOut;
    }

    public void setSignedOut(List<User> signedOut) {
        this.signedOut = signedOut;
    }

    public int compareTo(FlightRoster another) {
        return this.getFlight().getDepartureTime().compareTo(another.getFlight().getDepartureTime());
    }
}
