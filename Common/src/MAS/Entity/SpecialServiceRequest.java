package MAS.Entity;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

/**
 * Created by Jonathan on 14/10/2015.
 */
@Embeddable
public class SpecialServiceRequest {
    private String actionCode;

    @Basic
    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    private int passengerNumber;

    @Basic
    public int getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(int passengerNumber) {
        this.passengerNumber = passengerNumber;
    }

    private int itineraryNumber;

    @Basic
    public int getItineraryNumber() {
        return itineraryNumber;
    }

    public void setItineraryNumber(int itineraryNumber) {
        this.itineraryNumber = itineraryNumber;
    }

    private String value;

    @Basic
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
