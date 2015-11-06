package MAS.ManagedBean;

import MAS.Common.Constants;
import MAS.Entity.Customer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.HashMap;

@ManagedBean
public class FfpManagedBean {

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;
    private Customer customer;

    @PostConstruct
    public void init() {
        customer = authManagedBean.retrieveCustomer();
    }

    public String getTierName(int tier) {
        HashMap<Integer, String> tierList = new HashMap<>();
        tierList.put(Constants.FFP_TIER_BLUE, Constants.FFP_TIER_BLUE_LABEL);
        tierList.put(Constants.FFP_TIER_SILVER, Constants.FFP_TIER_SILVER_LABEL);
        tierList.put(Constants.FFP_TIER_GOLD, Constants.FFP_TIER_GOLD_LABEL);

        return tierList.get(tier);
    }

    public int getRequiredMilesforNextTier(int tier) {
        switch (tier) {
            case Constants.FFP_TIER_BLUE:
                return Constants.FFP_TIER_SILVER_REQUIREMENT - customer.getEliteMiles();
            case Constants.FFP_TIER_SILVER:
                return Constants.FFP_TIER_GOLD_REQUIREMENT - customer.getEliteMiles();
            case Constants.FFP_TIER_GOLD:
                return 0;
            default:
                return 0;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
