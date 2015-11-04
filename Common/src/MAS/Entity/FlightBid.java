package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

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

    private Flight flight;

    @ManyToOne
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
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
}
