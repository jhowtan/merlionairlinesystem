package MAS.ScheduleDev;

import MAS.Entity.Airport;
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
            System.out.println("Expected single route, actual is longer");
            return null;
        }
    }

    public Route latestRoute() {
        return routes.get(routes.size() - 1);
    }

    private void addShort(HypoRoute routeToAdd) {
        if (routeToAdd.route() != null) {
            if (routeToAdd.route().getOrigin() == latestRoute().getDestination()) {
                routes.add(routeToAdd.route());
                costDistance += routeToAdd.costDistance;
            }
            else {
                System.out.println("Unable to link: Destination and new Origin don't match.");
            }
        }
    }

    public HypoRoute addShortRRoute(HypoRoute routeToAdd) {
        HypoRoute result = new HypoRoute();
        result.copy(this);
        if (routeToAdd.route() != null) {
            result.addShort(routeToAdd);
        }
        return result;
    }

    public void addLong(HypoRoute routeToAdd) {

    }

    public boolean isOriginAlongRoute(Airport airport) {
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).getOrigin() == airport)
                return true;
        }
        return false;
    }

    public void copy(HypoRoute copyRoute) {
        actualDistance = copyRoute.actualDistance;
        costDistance = copyRoute.costDistance;
        routes = new ArrayList<>(copyRoute.routes);
    }

    public String print() {
        String result = "";
        if (routes.size() > 0) {
            result = result.concat(routes.get(0).getOrigin().getName() + " -> " + routes.get(0).getDestination().getName());
            for (int i = 1; i < routes.size(); i++) {
                result = result.concat(" -> " + routes.get(i).getDestination().getName());
            }
        }
        return result;
    }
}
