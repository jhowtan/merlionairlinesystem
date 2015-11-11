package MAS.ManagedBean.PriceManagement;

import MAS.Bean.BookingClassBean;
import MAS.Bean.CostsBean;
import MAS.Bean.FareRuleBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Common.Cabin;
import MAS.Common.Constants;
import MAS.Common.Utils;
import MAS.Entity.FareRule;
import MAS.Entity.Flight;
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
import java.util.Arrays;
import java.util.List;

@ManagedBean
@ViewScoped
public class CreateBookingClassManagedBean {
    @EJB
    BookingClassBean bookingClassBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    FareRuleBean fareRuleBean;
    @EJB
    CostsBean costsBean;

    private List<Flight> flights;
    private List<FareRule> fareRules;
    private FareRule fareRule;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String[] travelClasses = Cabin.TRAVEL_CLASSES;
    private String name;
    private long fareRuleId;
    private long flightId;
    private int occupied;
    private int allocation;
    private int travelClass;
    private boolean openStatus;
    private double price;

    private double costPerSeat;
    private double fareMul;
    private double classMul;

    public CreateBookingClassManagedBean() {
        resetFields();
    }

    @PostConstruct
    private void init() {
        flights = flightScheduleBean.getAllFlights();
        fareRules = fareRuleBean.getAllFareRules();
    }

    public void resetFields() {
        name = "";
        fareRuleId = 0;
        flightId = 0;
        allocation = 0;
    }

    public void createBookingClass() {
        try {
            bookingClassBean.createBookingClass(name, allocation, getTravelClass(), fareRuleId, flightId, getPrice());

            getAuthManagedBean().createAuditLog("Created new booking class: " + getName(), "create_booking_class");

            FacesMessage m = new FacesMessage("Booking class created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);

            resetFields();
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Booking class could not be created.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void fareRuleChangeListener(AjaxBehaviorEvent event) {
        try {
            fareRule = fareRuleBean.getFareRule(fareRuleId);
            fareMul = fareRule.getPriceMul();
            recalculateSuggPrice();
        } catch (NotFoundException e) {
            fareRule = null;
        }
    }

    public void flightCodeChangeListener(AjaxBehaviorEvent event) {
        try {
            costPerSeat = costsBean.calculateCostPerSeat(flightId);
            recalculateSuggPrice();
        } catch (NotFoundException e) {
            //Cannot find flight
        }
    }

    public void travelClassChangeListener(AjaxBehaviorEvent event) {
        recalculateSuggPrice();
    }

    public void priceChangeListener(AjaxBehaviorEvent event) {
        double baseprice = costPerSeat * classMul;
        try {
            allocation = costsBean.getSeatAllocation(flightId, travelClass, baseprice, price);
        } catch (NotFoundException e) {
            //e.printStackTrace();
            allocation = 0;
        }
    }

    public void recalculateSuggPrice() {
        classMul = Constants.TRAVEL_CLASS_PRICE_MULTIPLIER[travelClass];
        price = Utils.makeNiceMoney(costPerSeat * classMul * fareMul);
        if (price < 0) price = 0;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public List<FareRule> getFareRules() {
        return fareRules;
    }

    public void setFareRules(List<FareRule> fareRules) {
        this.fareRules = fareRules;
    }

    public AuthManagedBean getAuthManagedBean() {
        return authManagedBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFareRuleId() {
        return fareRuleId;
    }

    public void setFareRuleId(long fareRuleId) {
        this.fareRuleId = fareRuleId;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public boolean isOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(boolean openStatus) {
        this.openStatus = openStatus;
    }

    public int getAllocation() {
        return allocation;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    public int getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(int travelClass) {
        this.travelClass = travelClass;
    }

    public int getClassIndex(String travelClass) {
        return Arrays.asList(Cabin.TRAVEL_CLASSES).indexOf(travelClass);
    }

    public String[] getTravelClasses() {
        return travelClasses;
    }

    public void setTravelClasses(String[] travelClasses) {
        this.travelClasses = travelClasses;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public FareRule getFareRule() {
        return fareRule;
    }
}
