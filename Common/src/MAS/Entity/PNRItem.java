package MAS.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class PNRItem {

    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String passengerName;

    @Basic
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    private String frequentFlyerNumber;

    @Basic
    public String getFrequentFlyerNumber() {
        return frequentFlyerNumber;
    }

    public void setFrequentFlyerNumber(String frequentFlyerNumber) {
        this.frequentFlyerNumber = frequentFlyerNumber;
    }

    private String frequentFlyerProgram;

    @Basic
    public String getFrequentFlyerProgram() {
        return frequentFlyerProgram;
    }

    public void setFrequentFlyerProgram(String frequentFlyerProgram) {
        this.frequentFlyerProgram = frequentFlyerProgram;
    }

    private List<FlightItem> flightItems;

    @OneToMany
    public List<FlightItem> getFlightItems() {
        return flightItems;
    }

    public void setFlightItems(List<FlightItem> flightItems) {
        this.flightItems = flightItems;
    }

    private String specialRequest;

    @Basic
    @Lob
    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }
}
