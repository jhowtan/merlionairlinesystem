package MAS.ManagedBean;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.PNRBean;
import MAS.Common.Cabin;
import MAS.Common.SeatConfigObject;
import MAS.Common.Utils;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Entity.PNR;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
public class SeatSelectionManagedBean {

    private ETicket eTicket;

    @EJB
    FlightScheduleBean flightScheduleBean;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            eTicket = flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
        } catch (Exception e) {
            eTicket = null;
        }
    }

    public LinkedHashMap<String, Integer> availableSeatsName() {
        SeatConfigObject seatConfigObject = SeatConfigObject.getInstance(eTicket.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        System.out.println(eTicket.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        seatConfigObject.addTakenSeats(flightScheduleBean.getSeatsTakenForFlight(eTicket.getFlight()));
        return seatConfigObject.getAvailableSeatsNameForTravelClass(eTicket.getTravelClass());
    }

    public String getCabinConfig() {
        SeatConfigObject seatConfigObject = SeatConfigObject.getInstance(eTicket.getFlight().getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        Cabin cabin = null;
        for (Cabin c : seatConfigObject.getCabins()) {
            if (c.getTravelClass() == eTicket.getTravelClass()) {
                cabin = c;
                break;
            }
        }
        return cabin.getRepresentation();
    }

    public ETicket geteTicket() {
        return eTicket;
    }

    public void seteTicket(ETicket eTicket) {
        this.eTicket = eTicket;
    }
}
