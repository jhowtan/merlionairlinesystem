package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.MealSelectionBean;
import MAS.Bean.PNRBean;
import MAS.Common.Utils;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Entity.PNR;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.*;

@ManagedBean
@ViewScoped
public class SpecialRequestsManagedBean {
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    @EJB
    private PNRBean pnrBean;
    @EJB
    private MealSelectionBean mealSelectionBean;
    @EJB
    FlightScheduleBean flightScheduleBean;

    private String bookingReference;
    private String passengerLastName;
    private PNR pnr;
    private List<ETicket> eTickets;
    private String specialServiceRequest;
    private String eticketId;
    private String flightId;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        bookingReference = params.get("bookingReference");
        passengerLastName = params.get("passengerLastName");
        retrievePNR();
    }

    public void retrievePNR() {
        try {
            pnr = pnrBean.getPNR(Utils.convertBookingReference(bookingReference), passengerLastName);
        } catch (Exception e) {
            pnr = null;
        }
    }

    public void changeListener(AjaxBehaviorEvent event) {
        try {
            eTickets = pnrBean.getETicketsByPNR(pnr);
            Flight fl = flightScheduleBean.getFlight(Long.parseLong(flightId));
            for (ETicket et : eTickets) {
                if (!et.getFlight().equals(fl)) {
                    eTickets.remove(et);
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        //TODO: Save SSR request
    }

    public List<FlightTicketCollection> getFlightTicketCollections(PNR pnr) {
        List<ETicket> eTickets = pnrBean.getETicketsByPNR(pnr);
        HashMap<Flight, List<ETicket>> eTicketHashMap = new HashMap<>();
        for (ETicket eTicket : eTickets) {
            Flight flight = eTicket.getFlight();
            if (!eTicketHashMap.containsKey(flight)) {
                eTicketHashMap.put(flight, new ArrayList<>());
            }
            eTicketHashMap.get(flight).add(eTicket);
        }
        List<Flight> flights = new ArrayList<>(eTicketHashMap.keySet());
        Collections.sort(flights, new FlightComparator());
        List<FlightTicketCollection> flightTicketCollections = new ArrayList<>();
        for (Flight flight : flights) {
            FlightTicketCollection flightTicketCollection = new FlightTicketCollection();
            flightTicketCollection.setFlight(flight);
            flightTicketCollection.seteTickets(eTicketHashMap.get(flight));
            flightTicketCollections.add(flightTicketCollection);
        }
        return flightTicketCollections;
    }

    public String getEticketId() {
        return eticketId;
    }

    public void setEticketId(String eticketId) {
        this.eticketId = eticketId;
    }

    private class FlightComparator implements Comparator<Flight> {
        @Override
        public int compare(Flight o1, Flight o2) {
            return o1.getDepartureTime().compareTo(o2.getDepartureTime());
        }
    }

    public class FlightTicketCollection {
        private Flight flight;
        private List<ETicket> eTickets;

        public Flight getFlight() {
            return flight;
        }

        public void setFlight(Flight flight) {
            this.flight = flight;
        }

        public List<ETicket> geteTickets() {
            return eTickets;
        }

        public void seteTickets(List<ETicket> eTickets) {
            this.eTickets = eTickets;
        }
    }


    public PNR getPnr() {
        return pnr;
    }

    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }
    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getSpecialServiceRequest() {
        return specialServiceRequest;
    }

    public void setSpecialServiceRequest(String specialServiceRequest) {
        this.specialServiceRequest = specialServiceRequest;
    }

    public List<ETicket> geteTickets() {
        return eTickets;
    }

    public void seteTickets(List<ETicket> eTickets) {
        this.eTickets = eTickets;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
}
