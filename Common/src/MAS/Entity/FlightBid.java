package MAS.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class FlightBid {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private User bidder;

    @ManyToOne
    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    private Date bidDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    private List<Flight> flights;

    @ManyToMany
    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
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
