package MAS.ManagedBean.DepartureControl;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.MealSelectionBean;
import MAS.Common.SeatConfigObject;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@ManagedBean
public class PassengerServiceManagedBean {

    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    MealSelectionBean mealSelectionBean;

    private Flight flight;
    private SeatConfigObject seatConfigObject;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            flight = flightScheduleBean.getFlight(Long.parseLong(params.get("flight")));
            seatConfigObject = SeatConfigObject.getInstance(flight.getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ETicket> getPassengers() {
        List<ETicket> eTickets = flightScheduleBean.getETicketsForFlight(flight);
        Collections.sort(eTickets, new Comparator<ETicket>() {
            @Override
            public int compare(ETicket o1, ETicket o2) {
                if (o1.getTravelClass() == o2.getTravelClass()) {
                    if (o1.getSeatNumber() == o2.getSeatNumber()) {
                        return o1.getPassengerName().compareTo(o2.getPassengerName());
                    }
                    return o1.getSeatNumber() < o2.getSeatNumber() ? -1 : 1;
                }
                return o1.getTravelClass() < o2.getTravelClass() ? -1 : 1;
            }
        });
        return eTickets;
    }

    public List<String> getMeals(ETicket eTicket) throws NotFoundException {
        return mealSelectionBean.getMealSelections(eTicket);
    }

    public String getSeatName(int seat) {
        try {
            return seatConfigObject.convertIntToString(seat);
        } catch (NotFoundException e) {
            return null;
        }
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
