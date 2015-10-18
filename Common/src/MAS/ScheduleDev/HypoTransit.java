package MAS.ScheduleDev;

import MAS.Entity.Airport;
import MAS.Entity.Route;

import java.util.ArrayList;
import java.util.List;

public class HypoTransit extends ScheduleDevelopmentClass {
    HypoAircraft hypoAircraft;
    boolean flying;
    boolean underMaint;
    double timeLeft;
    double accumulatedMiles;
    List<Route> pathHistory;
    List<Double> pathTimes;
    List<Airport> maintHistory;
    List<Double> maintTimes;

    public HypoTransit() {
        pathHistory = new ArrayList<>();
        pathTimes = new ArrayList<>();
        flying = false;
        underMaint = false;
        maintHistory = new ArrayList<>();
        maintTimes = new ArrayList<>();
    }

    public String printPath() {
        String result = hypoAircraft.aircraft.getTailNumber() + " = ";
        for (int i = 0; i < pathHistory.size(); i++) {
            result = result.concat("{"+pathTimes.get(i)+"}: " + pathHistory.get(i).getOrigin().getName() + " - " + pathHistory.get(i).getDestination().getName() + ", ");
        }
        return result;
    }

    public String printMaint() {
        String result = hypoAircraft.aircraft.getTailNumber() + " = ";
        for (int i = 0; i < maintHistory.size(); i++) {
            result = result.concat("{"+maintTimes.get(i)+"}: " + maintHistory.get(i).getName() +  ", ");
        }
        return result;
    }
}
