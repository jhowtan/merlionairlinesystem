package MAS.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class FlightItem {

    private Long id;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    private Flight flight;

    @ManyToOne
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    private boolean boarded;

    @Basic
    public boolean isBoarded() {
        return boarded;
    }

    public void setBoarded(boolean boarded) {
        this.boarded = boarded;
    }

    private List<String> mealPreferences;

    @ElementCollection
    public List<String> getMealPreferences() {
        return mealPreferences;
    }

    public void setMealPreferences(List<String> mealPreferences) {
        this.mealPreferences = mealPreferences;
    }

    private String allocatedSeat;

    @Basic
    public String getAllocatedSeat() {
        return allocatedSeat;
    }

    public void setAllocatedSeat(String allocatedSeat) {
        this.allocatedSeat = allocatedSeat;
    }

    private String seatPreference;

    @Basic
    public String getSeatPreference() {
        return seatPreference;
    }

    public void setSeatPreference(String seatPreference) {
        this.seatPreference = seatPreference;
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


    private List<Baggage> baggages;

    @OneToMany(mappedBy = "flightItem")
    public List<Baggage> getBaggages() {
        return baggages;
    }

    public void setBaggages(List<Baggage> baggages) {
        this.baggages = baggages;
    }
}
