package MAS.ManagedBean.DepartureControl;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.UserBean;
import MAS.Entity.Airport;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

@ManagedBean
public class DepartureControlManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    UserBean userBean;
    @EJB
    FlightScheduleBean flightScheduleBean;

    private Flight flight;

    public Airport retrieveBaseAirport() {
        try {
            return userBean.getUser(authManagedBean.getUserId()).getBaseAirport();
        } catch (NotFoundException e) {
            e.getMessage();
        }
        return null;
    }

    public void openGateForFlight(Flight flight) {
        try {
            flight.setStatus(Flight.GATE_OPEN);
            flightScheduleBean.updateSingleFlight(flight);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        authManagedBean.createAuditLog("Gate opened for " + flight.getCode() + ": " + flight.getGateNumber(), "gate_check");
    }

    public void changeGateNumber(Flight flight) {
        try {
            flightScheduleBean.updateSingleFlight(flight);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        authManagedBean.createAuditLog("Gate number changed for " + flight.getCode() + ": " + flight.getGateNumber(), "gate_check");
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String showStatusName(Flight flight) {
        switch (flight.getStatus()) {
            case 0:
                return "NO STATUS";
            case 1:
                return "GATE OPEN";
            case 2:
                return "BOARDING";
            case 3:
                return "GATE CLOSING";
            case 4:
                return "LAST CALL";
            case 5:
                return "GATE CLOSED";
            case 6:
                return "DEPARTED";
            default:
                return "";
        }
    }

    public List<ETicket> retrievePassengersFromFlight() {
        return flightScheduleBean.getETicketsForFlight(flight);
    }

    public int countCheckedInPassengers() {
        int count = 0;
        for (ETicket passenger : retrievePassengersFromFlight()) {
           if (passenger.isCheckedIn())
               count++;
        }
        return count;
    }

    public int countBoardedPassengers() {
        int count = 0;
        for (ETicket passenger : retrievePassengersFromFlight()) {
            if (passenger.isCheckedIn() && passenger.isGateChecked()) {
                count++;
            }
        }
        return count;
    }

    public void removeBaggageForPassenger(ETicket eticket) {
        //@TODO: Decide how we want to remove baggages
    }

    public List<Flight> retrieveDepartingFlightsFromBaseAirport() {
        return flightScheduleBean.findDepartingFlightsByAirport(retrieveBaseAirport());
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public Flight getFlight() {
        return flight;
    }
}
