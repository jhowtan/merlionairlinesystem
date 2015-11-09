package MAS.Bean;

import MAS.Common.Constants;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Entity.PNR;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "MealSelectionEJB")
public class MealSelectionBean {
    public MealSelectionBean() {
    }

    @EJB
    PNRBean pnrBean;

    public int getMealsCount(Flight flight) {
        long flightTime = (flight.getArrivalTime().getTime() - flight.getDepartureTime().getTime()) / 1000 / 60;
        // One meal every 4 hours with a minimum of 1 meal
        return Math.max(1, (int) (flightTime / (4 * 60)));
    }

    public List<String> getMealSelections(ETicket eTicket) throws NotFoundException {
        PNR pnr = eTicket.getPnr();
        Flight flight = eTicket.getFlight();
        int passengerNum = pnrBean.getPassengerNumber(pnr, eTicket.getPassengerName());
        int itineraryNum = pnrBean.getItineraryNumber(pnr, flight);
        ArrayList<String> mealSelection = new ArrayList<>();
        for (int i = 0; i < getMealsCount(flight); i++) {
            try {
                mealSelection.add(pnrBean.getPassengerSpecialServiceRequest(pnr, passengerNum, itineraryNum, Constants.SSR_ACTION_CODE_MEAL + (i + 1)).getValue());
            } catch (NotFoundException e) {
                mealSelection.add("");
            }
        }
        return mealSelection;
    }

    public void setMealSelections(ETicket eTicket, List<String> mealSelection) throws NotFoundException {
        PNR pnr = eTicket.getPnr();
        Flight flight = eTicket.getFlight();
        int passengerNum = pnrBean.getPassengerNumber(pnr, eTicket.getPassengerName());
        int itineraryNum = pnrBean.getItineraryNumber(pnr, flight);
        for (int i = 0; i < mealSelection.size(); i++) {
            pnrBean.setSpecialServiceRequest(pnr, passengerNum, Constants.SSR_ACTION_CODE_MEAL + (i+1), mealSelection.get(i), itineraryNum);
        }
        pnrBean.updatePNR(pnr);
    }

}
