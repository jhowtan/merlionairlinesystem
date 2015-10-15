package MAS.ManagedBean.DepartureControl;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.PNRBean;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.Baggage;
import MAS.Entity.ETicket;
import MAS.Entity.Itinerary;
import MAS.Entity.PNR;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
public class CheckInManagedBean {

    @ManagedProperty(value = "#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private ETicket primaryETicket;
    private List<ETicket> relatedPassengers;
    private HashMap<Long, Boolean> relatedPassengersCheck;
    private HashMap<Long, Boolean> relatedPassengersCheckDisable;

    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    PNRBean pnrBean;

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            primaryETicket = flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
            relatedPassengers = flightScheduleBean.getRelatedETickets(primaryETicket);
            relatedPassengersCheck = new HashMap<>();
            relatedPassengersCheckDisable = new HashMap<>();
            for (ETicket eTicket : relatedPassengers) {
                relatedPassengersCheck.put(eTicket.getId(), eTicket.getId().equals(primaryETicket.getId()));
                relatedPassengersCheckDisable.put(eTicket.getId(), eTicket.getId().equals(primaryETicket.getId()) || eTicket.isCheckedIn());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBaggageToETicket(double weight) {
        Baggage baggage = new Baggage();
        baggage.setWeight(weight);
        List<Baggage> baggageList = primaryETicket.getBaggages();
        baggageList.add(baggage);
    }

    public ETicket getPrimaryETicket() {
        return primaryETicket;
    }

    public HashMap<Long, Boolean> getRelatedPassengersCheck() {
        return relatedPassengersCheck;
    }

    public void setRelatedPassengersCheck(HashMap<Long, Boolean> relatedPassengersCheck) {
        this.relatedPassengersCheck = relatedPassengersCheck;
    }

    public List<ETicket> getRelatedPassengers() {
        return relatedPassengers;
    }

    public void setRelatedPassengers(List<ETicket> relatedPassengers) {
        this.relatedPassengers = relatedPassengers;
    }

    public HashMap<Long, Boolean> getRelatedPassengersCheckDisable() {
        return relatedPassengersCheckDisable;
    }

    public void setRelatedPassengersCheckDisable(HashMap<Long, Boolean> relatedPassengersCheckDisable) {
        this.relatedPassengersCheckDisable = relatedPassengersCheckDisable;
    }

    public void setPrimaryETicket(ETicket primaryETicket) {
        this.primaryETicket = primaryETicket;
    }

    public List<ETicket> getAllDestinations() {
        return getPossibleConnections(primaryETicket.getPnr(), primaryETicket);
    }

    public List<ETicket> getPossibleConnections(PNR pnr, ETicket eTicket) {
        return getPossibleConnections(pnr, eTicket, new ArrayList<ETicket>());
    }

    public List<ETicket> getPossibleConnections(PNR pnr, ETicket eTicket, List<ETicket> connections) {
        Date arrivalTime = eTicket.getFlight().getArrivalTime();
        String destination = eTicket.getFlight().getAircraftAssignment().getRoute().getDestination().getId();
        List<Itinerary> itineraries = pnr.getItineraries();

        for (Itinerary itinerary : itineraries) {
            if (itinerary.getDepartureDate().after(arrivalTime) && itinerary.getDepartureDate().before(Utils.minutesLater(arrivalTime, Constants.MAX_CONNECTION_TIME_MINUTES)) && itinerary.getOrigin().equals(destination)) {
                try {
                    int itineraryNumber = pnrBean.getItineraryNumber(pnr, itinerary.getFlightCode());
                    int passengerNumber = pnrBean.getPassengerNumber(pnr, eTicket.getPassengerName());
                    String eTicketNumber = pnrBean.getPassengerSpecialServiceRequest(pnr, passengerNumber, itineraryNumber ,Constants.SSR_ACTION_CODE_TICKET_NUMBER).getValue();
                    ETicket connection = flightScheduleBean.getETicket(Long.parseLong(eTicketNumber));
                    connections.add(connection);
                    connections.addAll(getPossibleConnections(pnr, connection, connections));
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return connections;
    }
}
