package MAS.ManagedBean;

import MAS.Bean.FareRuleBean;
import MAS.Entity.FareRule;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
            authManagedBean.createAuditLog("Deleted fare rule: " + fareRule.getName(), "delete_role");
        } catch (NotFoundException e) {
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
