package MAS.ScheduleDev;

import MAS.Entity.Aircraft;
import MAS.Entity.Airport;

public class HypoAircraft extends ScheduleDevelopmentClass implements Comparable<HypoAircraft> {
    public Aircraft aircraft;
    public Airport homeBase;
    public Airport location;
    public Airport prevLocation;
    public boolean reqMaint = false;

    @Override
    public int compareTo(HypoAircraft another) {
        if (this.aircraft.getFlyingCost() < another.aircraft.getFlyingCost())
            return -1;
        else if (this.aircraft.getFlyingCost() > another.aircraft.getFlyingCost())
            return 1;
        return 0;
    }
}
