package MAS.ManagedBean;

import MAS.WebService.FFPAlliance.FFPAllianceService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class CreditAllianceMilesManagedBean {

    private Long ffpNumber;
    private Integer miles;

    public void submitRequest() {
        FFPAllianceService ffpAllianceService = new FFPAllianceService();
        if (ffpAllianceService.getFFPAlliancePort().awardMiles(ffpNumber, miles)) {
            FacesMessage m = new FacesMessage("Miles credited successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
            ffpNumber = null;
            miles = null;
        } else {
            FacesMessage m = new FacesMessage("Unable to credit miles.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public Long getFfpNumber() {
        return ffpNumber;
    }

    public void setFfpNumber(Long ffpNumber) {
        this.ffpNumber = ffpNumber;
    }

    public Integer getMiles() {
        return miles;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }
}
