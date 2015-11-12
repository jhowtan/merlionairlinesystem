package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.FlightScheduleBean;
import MAS.Bean.MealSelectionBean;
import MAS.Bean.PNRBean;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;
import MAS.Entity.Itinerary;
import MAS.Entity.PNR;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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

    private PNR pnr;
    private String specialServiceRequest;

    private String flightCode;
    private String passengerName;
    private boolean validSelection;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String bookingReference = params.get("bookingReference");
        try {
            pnr = pnrBean.getPNR(Utils.convertBookingReference(bookingReference));
        } catch (Exception e) {
            pnr = null;
        }
    }

    public void changeListener(AjaxBehaviorEvent event) {
        validSelection = false;
        int passengerNum = -1;
        int itineraryNum = -1;
        try {
            passengerNum = pnrBean.getPassengerNumber(pnr, passengerName);
            itineraryNum = pnrBean.getItineraryNumber(pnr, flightCode);
            validSelection = true;
            specialServiceRequest = pnrBean.getPassengerSpecialServiceRequest(pnr, passengerNum, itineraryNum, Constants.SSR_ACTION_CODE_REQUEST).getValue();
        } catch (NotFoundException e) {
            specialServiceRequest = "";
        }
    }

    public void save() {
        try {
            specialServiceRequest = specialServiceRequest.replaceAll("\\s+", " ").trim();
            int passengerNum = pnrBean.getPassengerNumber(pnr, passengerName);
            int itineraryNum = pnrBean.getItineraryNumber(pnr, flightCode);
            pnrBean.setSpecialServiceRequest(pnr, passengerNum, Constants.SSR_ACTION_CODE_REQUEST, specialServiceRequest, itineraryNum);
            pnrBean.updatePNR(pnr);
            FacesMessage m = new FacesMessage("Special service request for the customer has been saved successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e){
            FacesMessage m = new FacesMessage("There was an error saving the special service request.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
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


    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public boolean isValidSelection() {
        return validSelection;
    }

    public void setValidSelection(boolean validSelection) {
        this.validSelection = validSelection;
    }
}
