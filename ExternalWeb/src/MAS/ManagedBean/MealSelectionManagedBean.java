package MAS.ManagedBean;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.MealSelectionBean;
import MAS.Common.Cabin;
import MAS.Common.SeatConfigObject;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
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

    public String save() throws NotFoundException {
        boolean error = false;
        // @TODO
        if (!error) {
            return "manage?bookingReference=" + eTicket.getPnr().getBookingReference() + "&passengerLastName=" + eTicket.getPassengerName().split("/")[0].toUpperCase() + "&faces-redirect=true";
        }
        return "";
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
