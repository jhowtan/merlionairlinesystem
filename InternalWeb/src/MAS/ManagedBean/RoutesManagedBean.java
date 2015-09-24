package MAS.ManagedBean;

import MAS.Bean.RouteBean;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
        } catch (NotFoundException e) {
            e.printStackTrace();

        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
