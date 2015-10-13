package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.City;
import MAS.Entity.Country;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.TimeZone;

@ManagedBean
public class CreateCityManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<City> getAllCities() {
        return routeBean.getAllCities();
    }
    private List<Country> countries;

    private String cityId;
    private String cityName;
    private String countryId;
    private String timezone;

    @PostConstruct
    public void init() {
        setCountries(routeBean.getAllCountries());
    }

    public void createCity() throws NotFoundException {
        routeBean.createCity(cityId, cityName, countryId, timezone);
        authManagedBean.createAuditLog("Created new city: " + getCityName(), "create_city");
        setCityName(null);
        setCountryId(null);
        setTimezone(null);
        setCityId(null);
        FacesMessage m = new FacesMessage("City created successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getCityName() {
        return cityName;
    }

    public String[] retrieveTimezone() {
        return TimeZone.getAvailableIDs();
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
