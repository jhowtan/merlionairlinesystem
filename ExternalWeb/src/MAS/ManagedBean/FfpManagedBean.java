package MAS.ManagedBean;

import MAS.Bean.CustomerLogBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.PNRBean;
import MAS.Common.Constants;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ManagedBean
public class FfpManagedBean {

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    private Customer customer;
    private List<PNR> customerBookings;
    private List<CustomerLog> customerLogs;

    @EJB
    PNRBean pnrBean;
    @EJB
    FlightScheduleBean flightScheduleBean;
    @EJB
    CustomerLogBean customerLogBean;

    @PostConstruct
    public void init() {
        customer = authManagedBean.retrieveCustomer();
        try {
            customerBookings = pnrBean.getCustomerPNR(customer.getId());
            customerLogs = customerLogBean.getCustomerLogForCustomer(customer.getId());
        } catch (NotFoundException e) {
            e.printStackTrace();
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

    public String getTierName(int tier) {
        HashMap<Integer, String> tierList = new HashMap<>();
        tierList.put(Constants.FFP_TIER_BLUE, Constants.FFP_TIER_BLUE_LABEL);
        tierList.put(Constants.FFP_TIER_SILVER, Constants.FFP_TIER_SILVER_LABEL);
        tierList.put(Constants.FFP_TIER_GOLD, Constants.FFP_TIER_GOLD_LABEL);

        return tierList.get(tier);
    }

    public int getRequiredMilesForNextTier(int tier) {
        switch (tier) {
            case Constants.FFP_TIER_BLUE:
                return Constants.FFP_TIER_SILVER_REQUIREMENT - customer.getEliteMiles();
            case Constants.FFP_TIER_SILVER:
                return Constants.FFP_TIER_GOLD_REQUIREMENT - customer.getEliteMiles();
            case Constants.FFP_TIER_GOLD:
                return 0;
            default:
                return 0;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<PNR> getCustomerBookings() {
        return customerBookings;
    }

    public void setCustomerBookings(List<PNR> customerBookings) {
        this.customerBookings = customerBookings;
    }

    public List<CustomerLog> getCustomerLogs() {
        return customerLogs;
    }

    public void setCustomerLogs(List<CustomerLog> customerLogs) {
        this.customerLogs = customerLogs;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
