package MAS.ScheduleDev;

import MAS.Entity.Route;

import java.util.ArrayList;
import java.util.List;

public class HypoRoute {
    public List<Route> routes;
    public double actualDistance;
    public double costDistance;

    public HypoRoute (){
        routes = new ArrayList<>();
    }

    public Route route() {
        if (routes.size() == 1) {
            return routes.get(0);
        } else {
            return null;
        }
    }

    public void addShort(HypoRoute routeToAdd) {
        if (routeToAdd.route() != null) {
            routes.add(routeToAdd.route());
            costDistance += routeToAdd.costDistance;
        }
    }

    public void addLong(HypoRoute routeToAdd) {

    }
}
