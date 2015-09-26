package MAS.ManagedBean;

import MAS.Bean.BookingClassBean;
import MAS.Common.Cabin;
import MAS.Entity.BookingClass;
import MAS.Entity.FareRule;
import MAS.Entity.Flight;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean
public class UpdateBookingClassManagedBean {

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    private Map<String,String> params;

    @EJB
    private BookingClassBean bookingClassBean;

    private String[] travelClasses = Cabin.TRAVEL_CLASSES;
    private BookingClass bookingClass;
    private String name;
    private int allocation;
    private Flight flight;
    private FareRule fareRule;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        long bcId = Long.parseLong(params.get("bcId"));
        getBookingClass(bcId);
    }

    private void getBookingClass(long id) {
        try {
            bookingClass = bookingClassBean.getBookingClass(id);
        } catch (NotFoundException e) {
            e.getMessage();
        }
        name = bookingClass.getName();
        allocation = bookingClass.getAllocation();
        flight = bookingClass.getFlight();
        fareRule = bookingClass.getFareRule();
    }

    public void save() throws NotFoundException {
        bookingClassBean.changeName(bookingClass.getId(), name);
        bookingClassBean.changeAllocation(bookingClass.getId(), allocation);
        authManagedBean.createAuditLog("Updated booking class: " + bookingClass.getName(), "update_booking_class");
        FacesMessage m = new FacesMessage("Booking class updated successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public BookingClass getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(BookingClass bookingClass) {
        this.bookingClass = bookingClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAllocation() {
        return allocation;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public FareRule getFareRule() {
        return fareRule;
    }

    public void setFareRule(FareRule fareRule) {
        this.fareRule = fareRule;
    }

    public String[] getTravelClasses() {
        return travelClasses;
    }
}
