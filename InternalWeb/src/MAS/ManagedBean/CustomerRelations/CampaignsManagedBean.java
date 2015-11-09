package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Entity.Campaign;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
public class CampaignsManagedBean {
    @EJB
    CampaignBean campaignBean;

    private List<Campaign> campaigns;

    @PostConstruct
    private void init() {
        load();
    }

    private void load() {
        campaigns = campaignBean.getAllCampaigns();
    }
}
