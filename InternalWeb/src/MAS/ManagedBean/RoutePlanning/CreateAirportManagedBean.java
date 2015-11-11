package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.City;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class CreateAirportManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String airportName;
    private String code;
    private double latitude;
    private double longitude;
    private String cityId;
    private int hangars;

    private List<City> cities;

    @PostConstruct
    public void init() {
        setCities(routeBean.getAllCities());
    }

    public void createAirport() {
        try {
            routeBean.createAirport(code, airportName, cityId, latitude, longitude, hangars);
            authManagedBean.createAuditLog("Created new airport: " + airportName, "create_airport");
            setAirportName(null);
            setCode(null);
            setLatitude(0);
            setLongitude(0);
            setCityId(null);
            setHangars(0);
            FacesMessage m = new FacesMessage("Airport created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Airport could not be created.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getHangars() {
        return hangars;
    }

    public void setHangars(int hangars) {
        this.hangars = hangars;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
