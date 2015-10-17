package MAS.ScheduleDev;

import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Entity.Route;

import java.util.ArrayList;
import java.util.List;

public class TransitAircrafts extends ScheduleDevelopmentClass {
    List<HypoTransit> aircraftsInTransits;

    public TransitAircrafts() {
        aircraftsInTransits = new ArrayList<>();
    }

    public void fly(HypoAircraft hypoAircraft, double duration, Route route, double startTime, boolean saveHistory) {
        HypoTransit hypoTransit = new HypoTransit();
        hypoTransit.hypoAircraft = hypoAircraft;
        hypoTransit.timeLeft = duration;
        if (saveHistory) {
            hypoTransit.acLocation = route.getDestination();
            hypoTransit.pathHistory.add(route);
            hypoTransit.pathTimes.add(duration);
        } else {
            hypoTransit.acLocation = route.getOrigin();
        }
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

    public String toString() {
        String result = "";
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            result = result.concat("\n" + aircraftsInTransits.get(i).hypoAircraft.aircraft.getTailNumber() + " | " + aircraftsInTransits.get(i).acLocation.getName() + " : " + aircraftsInTransits.get(i).timeLeft);
        }
        return result;
    }
}
