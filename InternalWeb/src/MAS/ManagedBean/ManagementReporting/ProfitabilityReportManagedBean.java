package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.*;
import MAS.Common.Constants;
import MAS.Common.SeatConfigObject;
import MAS.Common.Utils;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class ProfitabilityReportManagedBean {

    @EJB
    private BookingClassBean bookingClassBean;
    @EJB
    private FleetBean fleetBean;
    @EJB
    private RouteBean routeBean;
    @EJB
    private CostsBean costsBean;
    @EJB
    private FlightScheduleBean flightScheduleBean;

    private List<Flight> templateList;
    private List<Flight> resultList;

    private List<String> aircraftTypeIds;
    private List<String> aircraftIds;
    private List<String> routeIds;
    private List<String> months;
    private double minProfit;
    private double maxProfit;
    private Date fromDate;
    private Date toDate;
    private long selectedFlightId;

    private List<AircraftType> allAircraftTypes;
    private List<Aircraft> allAircraft;
    private List<String> allMonths;


    @PostConstruct
    public void init() {
        templateList = flightScheduleBean.getAllFlights();
        resultList = new ArrayList<>(templateList);

        allAircraftTypes = fleetBean.getAllAircraftTypes();
        allAircraft = fleetBean.getAllAircraft();
        allMonths = new ArrayList<>(Arrays.asList(Constants.MONTHS_OF_YEAR));

        setAircraftTypeIds(new ArrayList<>());
        setAircraftIds(new ArrayList<>());
        setRouteIds(new ArrayList<>());
        setMonths(new ArrayList<>());
        setMinProfit(0);
        setMaxProfit(0);
    }

    public void flightFilterChangeEvent(AjaxBehaviorEvent event) {
        resultList = new ArrayList<>(templateList);
        //apply filter magic
        for (int i = 0; i < resultList.size(); i++) {
            Flight flight = resultList.get(i);
            if (aircraftTypeIds.size() > 0) {
                if (!LongListContains(aircraftTypeIds, flight.getAircraftAssignment().getAircraft().getSeatConfig().getAircraftType().getId())) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (aircraftIds.size() > 0) {
                if (!LongListContains(aircraftIds, flight.getAircraftAssignment().getAircraft().getId())) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (routeIds.size() > 0) {
                if (!LongListContains(routeIds, flight.getAircraftAssignment().getRoute().getId())) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (fromDate != null) {
                if (flight.getDepartureTime().compareTo(fromDate) == -1) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (toDate != null) {
                if (flight.getDepartureTime().compareTo(toDate) == 1) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (months.size() > 0) {
                if (!flightInMonth(flight)) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (minProfit != 0) {
                try {
                    if (getProfitabilityByFlight(flight) < minProfit) {
                        resultList.remove(i);
                        i--;
                        continue;
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (maxProfit != 0) {
                try {
                    if (getProfitabilityByFlight(flight) > maxProfit) {
                        resultList.remove(i);
                        i--;
                        continue;
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean flightInMonth(Flight flight) {
        for (int i = 0; i < months.size(); i++) {
            if (months.get(i).equals(String.valueOf(Utils.monthOf(flight.getDepartureTime())))) {
                return true;
            }
        }
        return false;
    }

    public boolean LongListContains(List<String> list, long val) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(String.valueOf(val)))
                return true;
        }
        return false;
    }

    public List<Flight> getAllFlights() {
        return resultList;
    }

    public List<BookingClass> getAllBookingClass() {
        try {
            return bookingClassBean.findBookingClassByFlight(selectedFlightId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public double getCostByFlight(Flight flight) throws NotFoundException {
        return costsBean.calculateCostPerFlight(flight.getId());
    }

    public double getRevenueByFlight(Flight flight) throws NotFoundException {
        double totalRevenue = 0;

        List<ETicket> etickets = flightScheduleBean.getETicketsForFlight(flight);
        for (ETicket eticket : etickets) {
            totalRevenue += eticket.getBookingClass().getPrice();
        }
        return totalRevenue;
    }

    public double getProfitabilityByFlight(Flight flight) throws NotFoundException {
        return getRevenueByFlight(flight) - getCostByFlight(flight);
    }

    public double getProfitabilityByBookingClass(Flight flight, BookingClass bookingClass) throws NotFoundException {
        SeatConfigObject seatConfigObject = new SeatConfigObject();
        seatConfigObject.parse(flight.getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        double totalCostOfFlight = getCostByFlight(flight);
        int numSeatsOccupied = bookingClass.getOccupied();
        int numSeatsInBookingClass = bookingClass.getAllocation();
        int numSeatsInFlight = seatConfigObject.getTotalSeats();
        double costOfBookingClass = totalCostOfFlight / numSeatsInFlight * numSeatsInBookingClass;
        double revenueFromBookingClass = numSeatsOccupied * bookingClass.getPrice();
        return (revenueFromBookingClass - costOfBookingClass);
    }

    public double getCostByBookingClass(Flight flight, BookingClass bookingClass) throws NotFoundException {
        SeatConfigObject seatConfigObject = new SeatConfigObject();
        seatConfigObject.parse(flight.getAircraftAssignment().getAircraft().getSeatConfig().getSeatConfig());
        double totalCostOfFlight = getCostByFlight(flight);
        int numSeatsInBookingClass = bookingClass.getAllocation();
        int numSeatsInFlight = seatConfigObject.getTotalSeats();
        return totalCostOfFlight / numSeatsInFlight * numSeatsInBookingClass;
    }

    public List<Aircraft> getAllAircrafts() {
        return fleetBean.getAllAircraft();
    }

    public List<Route> getAllRoutes() {
        return routeBean.getAllRoutes();
    }

    public List<String> getAircraftTypeIds() {
        return aircraftTypeIds;
    }

    public void setAircraftTypeIds(List<String> aircraftTypeIds) {
        this.aircraftTypeIds = aircraftTypeIds;
    }

    public List<String> getAircraftIds() {
        return aircraftIds;
    }

    public void setAircraftIds(List<String> aircraftIds) {
        this.aircraftIds = aircraftIds;
    }

    public List<String> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public double getMinProfit() {
        return minProfit;
    }

    public void setMinProfit(double minProfit) {
        this.minProfit = minProfit;
    }

    public double getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(double maxProfit) {
        this.maxProfit = maxProfit;
    }

    public List<AircraftType> getAllAircraftTypes() {
        return allAircraftTypes;
    }

    public void setAllAircraftTypes(List<AircraftType> allAircraftTypes) {
        this.allAircraftTypes = allAircraftTypes;
    }

    public List<Aircraft> getAllAircraft() {
        return allAircraft;
    }

    public void setAllAircraft(List<Aircraft> allAircraft) {
        this.allAircraft = allAircraft;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public long getSelectedFlightId() {
        return selectedFlightId;
    }

    public void setSelectedFlightId(long selectedFlightId) {
        this.selectedFlightId = selectedFlightId;
    }

    public List<String> getAllMonths() {
        return allMonths;
    }

    public void setAllMonths(List<String> allMonths) {
        this.allMonths = allMonths;
    }
}
