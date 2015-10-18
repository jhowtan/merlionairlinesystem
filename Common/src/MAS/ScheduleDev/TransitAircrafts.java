package MAS.ScheduleDev;

import MAS.Common.Constants;
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

    public void fly(HypoAircraft hypoAircraft, double duration, Route route, double startTime) {
        HypoTransit hypoTransit;
        try {
            hypoTransit = getTransitWithAc(hypoAircraft);
        } catch (NotFoundException e) {
            hypoTransit = new HypoTransit();
            hypoTransit.hypoAircraft = hypoAircraft;
            aircraftsInTransits.add(hypoTransit);
        }
        hypoTransit.timeLeft = duration;

        hypoTransit.hypoAircraft.location = route.getDestination();
        hypoTransit.pathHistory.add(route);
        hypoTransit.pathTimes.add(startTime);
        hypoTransit.accumulatedMiles += route.getDistance();
        if (hypoTransit.accumulatedMiles >= Constants.MILES_BEFORE_MAINTENANCE)
            hypoAircraft.reqMaint = true;
        hypoTransit.flying = true;

        hypoTransit.hypoAircraft.prevLocation = route.getOrigin();
    }

    public void maintenance(HypoAircraft hypoAircraft, double duration, Airport airport, double startTime, boolean saveHistory) {
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
            hypoTransit.maintHistory.add(airport);
            hypoTransit.maintTimes.add(startTime);
            hypoTransit.accumulatedMiles = 0;
            hypoTransit.hypoAircraft.reqMaint = false;
        }
        hypoTransit.hypoAircraft.location = hypoTransit.hypoAircraft.prevLocation = airport;

        hypoTransit.underMaint = true;
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
            if (!aircraftsInTransits.get(i).flying && !aircraftsInTransits.get(i).underMaint) continue; //Not flying or under maintenance
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
            selTransits.get(i).underMaint = false;
        }
        lastTime = lowest;
        //Process the timelefts
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            if (aircraftsInTransits.get(i).flying || aircraftsInTransits.get(i).underMaint) {
                aircraftsInTransits.get(i).timeLeft -= lowest;
            }
        }
        return result;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            if (aircraftsInTransits.get(i).flying)
                result = result.concat("\n" + aircraftsInTransits.get(i).hypoAircraft.aircraft.getTailNumber() + " | " +  aircraftsInTransits.get(i).hypoAircraft.prevLocation.getName() + " -> " + aircraftsInTransits.get(i).hypoAircraft.location.getName() + " : " + aircraftsInTransits.get(i).timeLeft);
        }
        result = result.concat("\n-------------PATHS-----------------");
        for (int i = 0; i < aircraftsInTransits.size(); i++) {
            result = result.concat("\n" + aircraftsInTransits.get(i).printPath());
        }
        return result;
    }
}
