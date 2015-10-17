package MAS.ScheduleDev;

import MAS.Entity.Airport;
import MAS.Entity.Route;

import java.util.ArrayList;
import java.util.List;

public class HypoTransit extends ScheduleDevelopmentClass {
    HypoAircraft hypoAircraft;
    double timeLeft;
    Airport acLocation;
    double accumulatedMiles;
    List<Route> pathHistory;

    public HypoTransit() {
        pathHistory = new ArrayList<>();
    }
}
