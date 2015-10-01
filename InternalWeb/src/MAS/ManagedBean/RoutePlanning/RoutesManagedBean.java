package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class RoutesManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Route> getAllRoutes() {
        return routeBean.getAllRoutes();
    }

    public void delete(long id) {
        try {
            String originName = routeBean.getRoute(id).getOrigin().getName();
            String destName = routeBean.getRoute(id).getDestination().getName();
            routeBean.removeRoute(id);
            authManagedBean.createAuditLog("Deleted route: " + originName + " - " +
                    destName, "delete_route");
            FacesMessage m = new FacesMessage("Successfully deleted route: " + originName + " - " + destName);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("Unable to delete route, please check if there are existing aircraft assignments created for this route.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The route cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }


    private class MapRoute {
        public String origin;
        public String destination;
        public double originLat;
        public double originLon;
        public double destinationLat;
        public double destinationLon;

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MapRoute && (
                    (this.origin.equals(((MapRoute) obj).origin) && this.destination.equals(((MapRoute) obj).destination)) ||
                    (this.origin.equals(((MapRoute) obj).destination) && this.destination.equals(((MapRoute) obj).origin))
            );
        }
    }

    public void mapRoutesJson() {
        ArrayList<MapRoute> mapRoutes = new ArrayList<>();
        for (Route route : routeBean.getAllRoutes()) {
            MapRoute r = new MapRoute();
            r.origin = route.getOrigin().getCode();
            r.destination = route.getDestination().getCode();
            r.originLat = route.getOrigin().getLatitude();
            r.originLon = route.getOrigin().getLongitude();
            r.destinationLat = route.getDestination().getLatitude();
            r.destinationLon = route.getDestination().getLongitude();
            if(!mapRoutes.contains(r)) {
                mapRoutes.add(r);
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(mapRoutes);

        if(!authManagedBean.isAuthenticated()) {
            json = "[]";
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
        response.setContentLength(json.length());
        response.setContentType("application/json");

        try {
            response.getOutputStream().write(json.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctx.responseComplete();
    }

}
