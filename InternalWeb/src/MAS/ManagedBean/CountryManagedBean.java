package MAS.ManagedBean;

import MAS.Bean.RouteBean;
import MAS.Entity.Country;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
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
            String countryName = routeBean.getCountry(id).getName();
            routeBean.removeCountry(id);
            authManagedBean.createAuditLog("Deleted country: " + countryName, "delete_country");
            FacesMessage m = new FacesMessage("Successfully deleted the country: " + countryName);
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Unable to delete country, please check if there are existing cities created for this country.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
