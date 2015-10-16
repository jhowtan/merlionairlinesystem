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

    public HypoTransit land(boolean remove) {
        if (aircraftsInTransits.size() < 1) return null;
        //Find lowest timeleft in aircraftsInTransit
        double lowest = aircraftsInTransits.get(0).timeLeft;
        HypoTransit landingAc = aircraftsInTransits.get(0);
        for (int i = 1; i < aircraftsInTransits.size(); i++) {
            if (aircraftsInTransits.get(i).timeLeft < lowest) {
                landingAc = aircraftsInTransits.get(i);
                lowest = landingAc.timeLeft;
            }
        }
        if (remove)
            aircraftsInTransits.remove(landingAc);
        return landingAc;
    }
}
