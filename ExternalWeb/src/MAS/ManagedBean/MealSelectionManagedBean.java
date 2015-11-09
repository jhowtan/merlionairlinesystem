package MAS.ManagedBean;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.MealSelectionBean;
import MAS.Common.Cabin;
import MAS.Common.Constants;
import MAS.Common.SeatConfigObject;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;
import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
@ViewScoped
public class MealSelectionManagedBean {

    private ETicket eTicket;
    private String[] mealSelection;

    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    MealSelectionBean mealSelectionBean;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            eTicket = flightScheduleBean.getETicket(Long.parseLong(params.get("eticket")));
            if (eTicket.getFlight().getStatus() != Flight.NO_STATUS) {
                throw new Exception();
            }
            List<String> mealSelectionList = mealSelectionBean.getMealSelections(eTicket);
            mealSelection = new String[mealSelectionList.size()];
            for (int i = 0; i < mealSelectionList.size(); i++) {
                mealSelection[i] = mealSelectionList.get(i);
            }
        } catch (Exception e) {
            eTicket = null;
        }
    }

    public List<String> getAvailableMeals() {
        ArrayList<String> meals = new ArrayList<>();
        meals.add("Normal Meal");
        meals.add("Vegetarian Meal");
        meals.add("Indian Meal");
        if (eTicket.getTravelClass() != Constants.ECONOMY_CLASS) {
            meals.add("Chicken Rice");
            meals.add("Pork Chop");
            meals.add("Singapore Laksa");
            meals.add("Toast with Scrambled Eggs");
        }
        if (eTicket.getTravelClass() == Constants.BUSINESS_CLASS || eTicket.getTravelClass() == Constants.FIRST_CLASS) {
            meals.add("Mushroom Ravioli");
            meals.add("Ceasar Salad with Prawns");
            meals.add("Seared Lamb Loin in Sage Sauce");
            meals.add("Char Siew Dumpling Noodle Soup");
            meals.add("Fillet Steak in Green Peppercorn Sauce");
        }
        if (eTicket.getTravelClass() == Constants.FIRST_CLASS) {
            meals.add("U.S. Grilled Prime Beef Fillet");
            meals.add("NY Sirloin Steak");
            meals.add("Pecan Crusted Veal");
            meals.add("Braised Short-Ribs");
            meals.add("Lobster Thermidor with Saffron Rice");
        }
        return meals;
    }

    public String save() throws NotFoundException {
        mealSelectionBean.setMealSelections(eTicket, new ArrayList<>(Arrays.asList(mealSelection)));
        return "manage?bookingReference=" + eTicket.getPnr().getBookingReference() + "&passengerLastName=" + eTicket.getPassengerName().split("/")[0].toUpperCase() + "&faces-redirect=true";
    }

    public ETicket geteTicket() {
        return eTicket;
    }

    public void seteTicket(ETicket eTicket) {
        this.eTicket = eTicket;
    }

    public String[] getMealSelection() {
        return mealSelection;
    }

    public void setMealSelection(String[] mealSelection) {
        this.mealSelection = mealSelection;
    }
}
