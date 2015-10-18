package MAS.ScheduleDev;

import MAS.Entity.Aircraft;
import MAS.Entity.Airport;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class TransitAircrafts extends ScheduleDevelopmentClass {
    List<HypoTransit> aircraftsInTransits;
    public double lastTime;

    public TransitAircrafts() {
        aircraftsInTransits = new ArrayList<>();
    }

    public void fly(HypoAircraft hypoAircraft, double duration, Route route, double startTime, boolean saveHistory) {
        HypoTransit hypoTransit;
        try {
            hypoTransit = getTransitWithAc(hypoAircraft);
        } catch (NotFoundException e) {
            hypoTransit = new HypoTransit();
            hypoTransit.hypoAircraft = hypoAircraft;
            aircraftsInTransits.add(hypoTransit);
        }
        hypoTransit.timeLeft = duration;
        if (saveHistory) {
            hypoTransit.hypoAircraft.location = route.getDestination();
            hypoTransit.pathHistory.add(route);
            hypoTransit.pathTimes.add(startTime);
            hypoTransit.flying = true;
        } else {
            hypoTransit.hypoAircraft.location = route.getOrigin();
            hypoTransit.flying = true;
        }
        hypoTransit.hypoAircraft.prevLocation = route.getOrigin();
    }

    private HypoTransit getTransitWithAc(HypoAircraft hypoAircraft) throws NotFoundException {
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            if (aircraftsInTransits.get(i).hypoAircraft == hypoAircraft)
                return aircraftsInTransits.get(i);
        }
        throw new NotFoundException();
    }

    public List<HypoAircraft> land() {
        if (aircraftsInTransits.size() < 1) return null;
        //Find lowest timeleft in aircraftsInTransit
        List<HypoAircraft> result = new ArrayList<>();
        List<HypoTransit> selTransits = new ArrayList<>();
        double lowest = Double.MAX_VALUE;
        HypoTransit landingAc;// = aircraftsInTransits.get(0);
        //result.add(landingAc.hypoAircraft);
        //selTransits.add(landingAc);
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            if (!aircraftsInTransits.get(i).flying) continue;
            if (aircraftsInTransits.get(i).timeLeft < lowest) {
                landingAc = aircraftsInTransits.get(i);
                lowest = landingAc.timeLeft;
                result = new ArrayList<>();
                selTransits = new ArrayList<>();
                result.add(landingAc.hypoAircraft);
                selTransits.add(landingAc);
            } else if (aircraftsInTransits.get(i).timeLeft == lowest) {
                result.add(aircraftsInTransits.get(i).hypoAircraft);
                selTransits.add(aircraftsInTransits.get(i));
            }
        }
        for (int i = 0; i < selTransits.size(); i++) {
            selTransits.get(i).timeLeft = 0;
            selTransits.get(i).flying = false;
        }
        lastTime = lowest;
        //Process the timelefts
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            if (aircraftsInTransits.get(i).flying) {
                aircraftsInTransits.get(i).timeLeft -= lowest;
            }
        }
        return result;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            if (aircraftsInTransits.get(i).flying)
                result = result.concat("\n" + aircraftsInTransits.get(i).hypoAircraft.aircraft.getTailNumber() + " | " + aircraftsInTransits.get(i).hypoAircraft.location.getName() + " : " + aircraftsInTransits.get(i).timeLeft);
        }
        return result;
    }
}
