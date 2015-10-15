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

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<ETicket> retrievePassengersFromFlight() {
        return flightScheduleBean.getETicketsForFlight(flight);
    }

    public List<Flight> retrieveDepartingFlightsFromBaseAirport() {
        return flightScheduleBean.findDepartingFlightsByAirport(retrieveBaseAirport());
    }

    public void updatePassengerCheckIn(ETicket eTicket) {
        eTicket.setCheckedIn(true);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public Flight getFlight() {
        return flight;
    }
}
