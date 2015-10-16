package MAS.ScheduleDev;

import MAS.Entity.Aircraft;
import MAS.Entity.Airport;

import java.util.ArrayList;
import java.util.List;

public class TransitAircrafts extends ScheduleDevelopmentClass {
    List<HypoTransit> aircraftsInTransits;

    public TransitAircrafts() {
        aircraftsInTransits = new ArrayList<>();
    }

    public void fly(HypoAircraft hypoAircraft, double duration, Airport destination) {
        HypoTransit hypoTransit = new HypoTransit();
        hypoTransit.acLocation = destination;
        hypoTransit.hypoAircraft = hypoAircraft;
        hypoTransit.timeLeft = duration;
        aircraftsInTransits.add(hypoTransit);
    }

    public HypoTransit land() {
        //Find lowest timeleft in aircraftsInTransit

        return null;
    }
}
