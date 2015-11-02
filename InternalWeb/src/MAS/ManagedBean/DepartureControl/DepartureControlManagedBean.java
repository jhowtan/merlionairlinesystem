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

    public List<ETicket> retrievePassengersFromFlight() {
        return flightScheduleBean.getETicketsForFlight(flight);
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

    public List<Flight> retrieveDepartingFlightsFromBaseAirportForCheckIn() {
        return flightScheduleBean.findDepartingFlightsByAirportForCheckIn(retrieveBaseAirport());
    }

    public List<Flight> retrieveDepartingFlightsFromBaseAirportForGateControl() {
        return flightScheduleBean.findDepartingFlightsByAirportForGateControl(retrieveBaseAirport());
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public Flight getFlight() {
        return flight;
    }
}
