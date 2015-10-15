package MAS.Entity;

import javax.persistence.*;

@Entity
public class Route {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Airport origin;

    @ManyToOne
    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    private Airport destination;

    @ManyToOne
    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    private double distance;

    @Basic
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isSame(Route route) {
        if (route.getOrigin() == origin) {
            if (route.getDestination() == destination)
                return true;
        }
        return false;
    }
}
