package MAS.Entity;

import MAS.Common.Utils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@TableGenerator(name = "bookingNumberTable", initialValue=234500000, allocationSize = 5)
public class PNR {

    private Date createdTimestamp;

    private long id;

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "bookingNumberTable")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String bookingReference;

    @Transient
    public String getBookingReference() {
        return Utils.convertBookingReference(id);
    }

    public void setBookingReference(String bookingReference) {
        this.id = Utils.convertBookingReference(bookingReference);
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    private List<PNRItem> PNRItems;

    @OneToMany
    public List<PNRItem> getPNRItems() {
        return PNRItems;
    }

    public void setPNRItems(List<PNRItem> PNRItems) {
        this.PNRItems = PNRItems;
    }

    private String travelAgent;

    @Basic
    public String getTravelAgent() {
        return travelAgent;
    }

    public void setTravelAgent(String travelAgent) {
        this.travelAgent = travelAgent;
    }
}
