package MAS.ManagedBean.DepartureControl;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean
public class GateCheckManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    UserBean userBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    CustomerBean customerBean;
    @EJB
    FareRuleBean fareRuleBean;
    @EJB
    FFPBean ffpBean;
    @EJB
    CustomerLogBean customerLogBean;
    @EJB
    PartnerMilesBean partnerMilesBean;

    private Flight flight;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            flight = flightScheduleBean.getFlight(Long.parseLong(params.get("flight")));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void changeGateStatus(Flight flight, int status) {
        try {
            if (status == Flight.DEPARTED) {
                departFlight(flight);
            }
            flight.setStatus(status);
            flightScheduleBean.updateSingleFlight(flight);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        authManagedBean.createAuditLog("Gate status " + flight.getCode() + "-" +
                flight.getGateNumber() + ": " + showStatusName(flight), "gate_check");
    }

    public void departFlight(Flight flight) {
        flight.setActualDepartureTime(new Date());
        int milesFlown = new Double(flight.getAircraftAssignment().getRoute().getDistance()).intValue();
        List<ETicket> etickets = flightScheduleBean.getETicketsForFlight(flight);
        for (ETicket eticket : etickets) {
            String ffp = eticket.getFfpNumber();
            if (ffp == null) continue;
            String[] parts = ffp.split("/");

            if (parts.length != 2) continue;
            if (!Arrays.asList(Constants.FFP_ALLIANCE_LIST_CODE).contains(parts[0])) continue;
            if (!eticket.isGateChecked()) continue;

            try {
                FareRule fareRule = eticket.getBookingClass().getFareRule();

                int miles = (milesFlown * fareRule.getMilesAccrual()) / 100;
                miles = (miles * travelClassMultiplier(eticket)) / 100;

                int eliteMiles = milesFlown;
                eliteMiles = (eliteMiles * travelClassMultiplier(eticket)) / 100;

                if (parts[0].equals("MA")) {
                    Customer customer = customerBean.getCustomer(Long.parseLong(parts[1]));
                    ffpBean.creditEliteMiles(customer.getId(), eliteMiles);
                    ffpBean.creditMiles(customer.getId(), miles);

                    customerLogBean.createCustomerLog(customer.getId(),
                            miles + " Miles earned for flight " + flight.getCode() +
                                    " from " + flight.getAircraftAssignment().getRoute().getOrigin() +
                                    " to " + flight.getAircraftAssignment().getRoute().getDestination(), "miles");
                    customerLogBean.createCustomerLog(customer.getId(),
                            eliteMiles + " Elite Miles earned for flight " + flight.getCode() +
                                    " from " + flight.getAircraftAssignment().getRoute().getOrigin() +
                                    " to " + flight.getAircraftAssignment().getRoute().getDestination(), "elite_miles");
                } else {
                    partnerMilesBean.awardMiles(ffp, miles);
                }
            } catch (NotFoundException e) {
                continue;
            }
        }
    }

    public int travelClassMultiplier(ETicket eticket) {
        switch (eticket.getBookingClass().getTravelClass()) {
            case 0:
                return 150;
            case 1:
                return 125;
            default:
                return 100;
        }
    }

    public String showStatusName(Flight flight) {
        switch (flight.getStatus()) {
            case Flight.NO_STATUS:
                return "NOT AVAILABLE";
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

    public Airport retrieveBaseAirport() {
        try {
            return userBean.getUser(authManagedBean.getUserId()).getBaseAirport();
        } catch (NotFoundException e) {
            e.getMessage();
        }
        return null;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public void setFlightScheduleBean(FlightScheduleBean flightScheduleBean) {
        this.flightScheduleBean = flightScheduleBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
