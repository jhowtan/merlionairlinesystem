package MAS.Entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Daryl Ho on 7/11/2015.
 */
@Entity
public class RosterSignInOut {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private List<User> signedIn;

    @ManyToMany
    public List<User> getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(List<User> signedIn) {
        this.signedIn = signedIn;
    }

    private List<User> signedOut;

    @ManyToMany
    public List<User> getSignedOut() {
        return signedOut;
    }

    public void setSignedOut(List<User> signedOut) {
        this.signedOut = signedOut;
    }

    private FlightRoster flightRoster;

    @OneToOne
    public FlightRoster getFlightRoster() {
        return flightRoster;
    }

    public void setFlightRoster(FlightRoster flightRoster) {
        this.flightRoster = flightRoster;
    }
}
