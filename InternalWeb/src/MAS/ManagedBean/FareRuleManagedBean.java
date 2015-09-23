package MAS.ManagedBean;

import MAS.Bean.FareRuleBean;
import MAS.Entity.FareRule;

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

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
