package MAS.ScheduleDev;

import MAS.Entity.Aircraft;
import MAS.Entity.Airport;

public class HypoAircraft extends ScheduleDevelopmentClass {
    public Aircraft aircraft;
    public Airport homeBase;
    public Airport location;
    public double timeInTransit = 0;
}
