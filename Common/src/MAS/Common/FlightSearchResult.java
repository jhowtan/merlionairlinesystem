package MAS.Common;

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

    public List<FlightSearchItem> getFlightSearchItems() {
        return flightSearchItems;
    }
}
