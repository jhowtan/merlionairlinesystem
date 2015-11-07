package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Daryl on 7/11/2015.
 */
@Entity
public class FlightDeferment {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String reason;

    @Basic
    @Lob
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private int status;

    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private Date createDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    private FlightRoster flightRoster;

    @ManyToOne
    public FlightRoster getFlightRoster() {
        return flightRoster;
    }

    public void setFlightRoster(FlightRoster flightRoster) {
        this.flightRoster = flightRoster;
    }

    private User deferrer;

    @OneToOne
    public User getDeferrer() {
        return deferrer;
    }

    public void setDeferrer(User deferrer) {
        this.deferrer = deferrer;
    }

    private User replacement;

    @OneToOne
    public User getReplacement() {
        return replacement;
    }

    public void setReplacement(User replacement) {
        this.replacement = replacement;
    }
}
