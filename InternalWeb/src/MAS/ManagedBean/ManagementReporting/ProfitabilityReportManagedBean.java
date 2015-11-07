package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.BookingClassBean;
import MAS.Bean.CostsBean;
import MAS.Bean.FleetBean;
import MAS.Bean.FlightScheduleBean;
import MAS.Bean.RouteBean;
import MAS.Common.SeatConfigObject;
import MAS.Entity.*;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
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

    public List<Flight> getAllFlights() {
        return flightScheduleBean.getAllFlights();
    }

    public List<BookingClass> getAllBookingClass() {
        return bookingClassBean.getAllBookingClasses();
    }

    public double getCostByFlight(Flight flight) throws NotFoundException {
        return costsBean.calculateCostPerFlight(flight.getId());
    }

    public double getProfitabilityByFlight(Flight flight) throws NotFoundException {
        double totalRevenue = 0;
        double totalCost = getCostByFlight(flight);

        List<ETicket> etickets = flightScheduleBean.getETicketsForFlight(flight);
        for (ETicket eticket : etickets) {
            totalRevenue += eticket.getBookingClass().getPrice();
        }
        return (totalRevenue - totalCost);
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
}
