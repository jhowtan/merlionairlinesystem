package MAS.ManagedBean;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.PNRBean;
import MAS.Common.Cabin;
import MAS.Common.Constants;
import MAS.Common.SeatConfigObject;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.CancelException;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
@ViewScoped
public class ManageBookingManagedBean {

    private String bookingReference;
    private String passengerLastName;
    private PNR pnr;

    @EJB
    PNRBean pnrBean;
    @EJB
    FlightScheduleBean flightScheduleBean;

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

    public String getSeatNumber(ETicket eTicket) {
        SeatConfigObject seatConfigObject = SeatConfigObject.getInstance(eTicket.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        try {
            return seatConfigObject.convertIntToString(eTicket.getSeatNumber());
        } catch (NotFoundException e) {
            return "N/A";
        }
    }

    public void cancel() {
        try {
            pnrBean.cancel(pnr.getId());
            pnr = null;
            bookingReference = null;
            passengerLastName = null;
            FacesMessage m = new FacesMessage("Booking successfully cancelled.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (CancelException e) {
            FacesMessage m = new FacesMessage("Unable to cancel your booking. Please contact customer support to make changes to your booking!");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public boolean canCancel() {
        Date cancel = Utils.hoursFromNow(24 * 2);
        for (Itinerary itinerary : pnr.getItineraries()) {
            if (itinerary.getDepartureDate().before(cancel)) {
                return false;
            }
        }
        return true;
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

    private class FlightComparator implements Comparator<Flight> {
        @Override
        public int compare(Flight o1, Flight o2) {
            return o1.getDepartureTime().compareTo(o2.getDepartureTime());
        }
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    public PNR getPnr() {
        return pnr;
    }

    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }
}
