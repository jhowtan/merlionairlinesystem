package MAS.ManagedBean.PriceManagement;

import MAS.Bean.FareRuleBean;
import MAS.Entity.FareRule;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
public class FareRuleManagedBean {
    @EJB
    private FareRuleBean fareRuleBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<FareRule> getAllFareRules() {
        return fareRuleBean.getAllFareRules();
    }

    public void delete(long id) {
        try {
            FareRule fareRule = fareRuleBean.getFareRule(id);
            fareRuleBean.removeFareRule(id);
            authManagedBean.createAuditLog("Deleted fare rule: " + fareRule.getName(), "delete_fare_rule");
        } catch (NotFoundException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The fare rule cannot be found, or may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (EJBException e) {
            e.getMessage();
            FacesMessage m = new FacesMessage("The fare rule you are trying to delete is in use by existing flights in operation.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
