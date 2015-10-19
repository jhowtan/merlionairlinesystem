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

    public void changeGateStatus(Flight flight, int status) {
        try {
            flight.setStatus(status);
            flightScheduleBean.updateSingleFlight(flight);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        authManagedBean.createAuditLog("Gate status " + flight.getCode() + "-" +
                flight.getGateNumber() + ": " + showStatusName(flight), "gate_check");
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
            case Flight.NO_STATUS:
                return "NO STATUS";
            case Flight.GATE_OPEN:
                return "GATE OPEN";
            case Flight.BOARDING:
                return "BOARDING";
            case Flight.GATE_CLOSING:
                return "GATE CLOSING";
            case Flight.LAST_CALL:
                return "LAST CALL";
            case Flight.GATE_CLOSED:
                return "GATE CLOSED";
            case Flight.DEPARTED:
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

    public double countPercentageBoarded() {
        if (countCheckedInPassengers() != 0) {
            return (double) countBoardedPassengers() / countCheckedInPassengers() * 100;
        }
        return 0;
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
