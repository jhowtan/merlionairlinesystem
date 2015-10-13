package MAS.Entity;

import MAS.Common.Utils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@TableGenerator(name = "bookingNumberTable", initialValue=234500000, allocationSize = 5)
public class PNR {

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

    private Date created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    private String travelAgent;

    @Basic
    public String getTravelAgent() {
        return travelAgent;
    }

    public void setTravelAgent(String travelAgent) {
        this.travelAgent = travelAgent;
    }

    private List<Itinerary> itineraries;

    @ElementCollection
    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    private List<SpecialServiceRequest> specialServiceRequests;

    @ElementCollection
    public List<SpecialServiceRequest> getSpecialServiceRequests() {
        return specialServiceRequests;
    }

    public void setSpecialServiceRequests(List<SpecialServiceRequest> specialServiceRequests) {
        this.specialServiceRequests = specialServiceRequests;
    }

    private List<String> passengers;

    @ElementCollection
    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }

}
