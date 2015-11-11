package MAS.ManagedBean.RoutePlanning;

import MAS.Bean.RouteBean;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
public class CreateCountryManagedBean {
    @EJB
    RouteBean routeBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String countryName;
    private String code;

    public void createCountry() {
        try {
            routeBean.createCountry(getCountryName(), code);
            authManagedBean.createAuditLog("Created new country: " + countryName, "create_country");
            setCountryName(null);
            setCode(null);
            FacesMessage m = new FacesMessage("Country created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Country could not be created.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
