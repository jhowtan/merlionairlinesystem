package MAS.ManagedBean;

import MAS.Bean.RouteBean;
import MAS.Entity.City;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class CityManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<City> getAllCities() {
        return routeBean.getAllCities();
    }

    public void delete(long id) {
        try {
            String cityName = routeBean.getCity(id).getName();
            routeBean.removeCity(id);
            authManagedBean.createAuditLog("Deleted city: " + cityName, "delete_city");
            FacesMessage m = new FacesMessage("Successfully deleted the city: " + cityName);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Unable to delete city, please check if there are existing airports created for this city.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

}
