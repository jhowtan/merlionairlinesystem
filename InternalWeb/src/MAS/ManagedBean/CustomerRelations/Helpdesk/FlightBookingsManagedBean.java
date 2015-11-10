package MAS.ManagedBean.CustomerRelations.Helpdesk;

import MAS.Bean.CustomerBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Entity.Customer;
import MAS.Entity.ETicket;
import MAS.Entity.Flight;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
public class FlightBookingsManagedBean implements Serializable {
    @EJB
    CustomerBean customerBean;
    @EJB
    FlightScheduleBean flightScheduleBean;

    private Customer customer;

    @PostConstruct
    public void init() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            customer = customerBean.getCustomer(Long.parseLong(params.get("customerId")));
        } catch (Exception e) {
            // Cannot find customer!
        }
    }

    public List<ETicket> getCustomerEtickets() {
        List<ETicket> allETickets = flightScheduleBean.getCustomerEtickets(customer.getId());
        List<ETicket> upcomingFlights = new ArrayList<>();
        for (ETicket eticket : allETickets) {
            if (eticket.getFlight().getStatus() != Flight.DEPARTED) {
                upcomingFlights.add(eticket);
            }
        }
        return upcomingFlights;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
