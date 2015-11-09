package MAS.Common;

import java.util.Date;
import java.util.List;

public class FlightSearchResult {
    private final List<FlightSearchItem> flightSearchItems;


    public FlightSearchResult(List<FlightSearchItem> flightSearchItems) {
        this.flightSearchItems = flightSearchItems;
    }

    public int getStopoverCounts() {
        return flightSearchItems.size() - 1;
    }

    public double getCheapestPrice() {
        double cheapestPrice = 0;
        for (FlightSearchItem flightSearchItem : flightSearchItems) {
            cheapestPrice += flightSearchItem.getCheapestBookingClass().getPrice();
        }
        return cheapestPrice;
    }

    public long getTotalHoursDuration() {
        long totalHours = 0;
        for (FlightSearchItem flightSearchItem : flightSearchItems) {
            Date departTime = flightSearchItem.getFlight().getDepartureTime();
            Date arriveTime = flightSearchItem.getFlight().getArrivalTime();

            long diff = arriveTime.getTime() - departTime.getTime();
            long hours = diff / (60 * 60 * 1000) % 24;
            totalHours += hours;
        }
        return totalHours;
    }

    public long getTotalMinDuration() {
        long totalMinutes = 0;
        for (FlightSearchItem flightSearchItem : flightSearchItems) {
            Date departTime = flightSearchItem.getFlight().getDepartureTime();
            Date arriveTime = flightSearchItem.getFlight().getArrivalTime();

            long diff = arriveTime.getTime() - departTime.getTime();
            long minutes = diff / (60 * 1000) % 60;
            totalMinutes += minutes;
        }
        return totalMinutes;

    }
    public List<FlightSearchItem> getFlightSearchItems() {
        return flightSearchItems;
    }
}
