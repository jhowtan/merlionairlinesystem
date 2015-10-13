package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.Entity.Airport;
import MAS.Entity.City;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

@ManagedBean
public class UpdateAirportManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private Map<String,String> params;

    private Airport airport;
    private String airportName;
    private String code;
    private double latitude;
    private double longitude;
    private String cityId;
    private int hangars;

    private List<City> cities;

    @PostConstruct
    public void init() {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String apId = params.get("apId");
        getAirport(apId);
        setCities(routeBean.getAllCities());
    }

    private void getAirport(String id) {
        try {
            airport = routeBean.getAirport(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        airportName = airport.getName();
        code = airport.getId();
        latitude = airport.getLatitude();
        longitude = airport.getLongitude();
        cityId = airport.getCity().getId();
        hangars = airport.getHangars();
    }

    public void save() throws NotFoundException {
        routeBean.updateAirport(airport);
        authManagedBean.createAuditLog("Updated Airport: " + airportName, "update_airport");

        FacesMessage m = new FacesMessage("Airport changes saved successfully.");
        m.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage("status", m);
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getAirportName() {
        return airportName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
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

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getCityName() throws NotFoundException {
        return routeBean.getCity(cityId).getName();
    }

    public void setCityName() { return; }
}
