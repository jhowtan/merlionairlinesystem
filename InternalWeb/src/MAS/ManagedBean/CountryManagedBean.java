package MAS.ManagedBean;

import MAS.Bean.FleetBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Country;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

@ManagedBean
public class CountryManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Country> getAllCountries() {
        return routeBean.getAllCountries();
    }

    public void delete(long id) {
        try {
            routeBean.removeCountry(id);
            authManagedBean.createAuditLog("Deleted country: " + routeBean.getCountry(id).getName(), "delete_country");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
