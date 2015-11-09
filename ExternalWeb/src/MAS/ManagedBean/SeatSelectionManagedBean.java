package MAS.ManagedBean;

import MAS.Bean.FlightScheduleBean;
import MAS.Common.Cabin;
import MAS.Common.SeatConfigObject;
import MAS.Entity.ETicket;
import MAS.Exception.NotFoundException;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class SeatSelectionManagedBean {

    private ETicket eTicket;
    private SeatConfigObject seatConfigObject;
    private String selectedSeat;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            eTicket = flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
            seatConfigObject = SeatConfigObject.getInstance(eTicket.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
            if (eTicket.getSeatNumber() != -1) {
                selectedSeat = seatConfigObject.convertIntToString(eTicket.getSeatNumber());
            }
        } catch (Exception e) {
            eTicket = null;
        }
    }

    public String save() throws NotFoundException {
        Integer seat = seatConfigObject.convertStringToInt(selectedSeat);
        boolean error = false;
        if (seat != eTicket.getSeatNumber()) {
            if (flightScheduleBean.isSeatAvailable(eTicket.getFlight(), seat)) {
                eTicket.setSeatNumber(seat);
                flightScheduleBean.updateETicket(eTicket);
            } else {
                selectedSeat = null;
                FacesMessage m = new FacesMessage("The seat selected on flight " + eTicket.getFlight().getCode() + " is no longer available.");
                m.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage("status", m);
                error = true;
            }
        }
        if (!error) {
            return "manage?bookingReference=" + eTicket.getPnr().getBookingReference() + "&passengerLastName=" + eTicket.getPassengerName().split("/")[0].toUpperCase() + "&faces-redirect=true";
        }
        return "";
    }

    public LinkedHashMap<String, Integer> availableSeatsName() {
        return seatConfigObject.getAvailableSeatsNameForTravelClass(eTicket.getTravelClass());
    }

    public String occupiedSeatsNameJson() throws NotFoundException {
        flightScheduleBean.getSeatsTakenForFlight(eTicket.getFlight());
        List<Integer> seatsTakenForFlight = flightScheduleBean.getSeatsTakenForFlight(eTicket.getFlight());
        List<String> seatsTakenForFlightString = new ArrayList<>();
        for (Integer s : seatsTakenForFlight) {
            seatsTakenForFlightString.add(seatConfigObject.convertIntToString(s));
        }
        Gson gson = new Gson();
        return gson.toJson(seatsTakenForFlightString);
    }

    public String getCabinConfig() {
        Cabin cabin = null;
        for (Cabin c : seatConfigObject.getCabins()) {
            if (c.getTravelClass() == eTicket.getTravelClass()) {
                cabin = c;
                break;
            }
        }
        return cabin.getRepresentation().replace("|", "_");
    }

    public int getCabinRowCount() {
        Cabin cabin = null;
        for (Cabin c : seatConfigObject.getCabins()) {
            if (c.getTravelClass() == eTicket.getTravelClass()) {
                cabin = c;
                break;
            }
        }
        return cabin.getNumRows();
    }

    public int getFirstSeatInCabin() throws NotFoundException {
        return seatConfigObject.getFirstSeatInTravelClass(eTicket.getTravelClass());
    }

    public int getFirstSeatRow() throws NotFoundException {
        return Integer.parseInt(seatConfigObject.convertIntToString(getFirstSeatInCabin()).replaceAll("[^0-9]", ""));
    }

    public ETicket geteTicket() {
        return eTicket;
    }

    public void seteTicket(ETicket eTicket) {
        this.eTicket = eTicket;
    }

    public String getSelectedSeat() {
        return selectedSeat;
    }

    public void setSelectedSeat(String selectedSeat) {
        this.selectedSeat = selectedSeat;
    }
}
