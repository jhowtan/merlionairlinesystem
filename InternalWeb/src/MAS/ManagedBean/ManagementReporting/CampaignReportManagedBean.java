package MAS.ManagedBean.ManagementReporting;

import MAS.Bean.BookingClassBean;
import MAS.Bean.CampaignBean;
import MAS.Bean.CustomerBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Campaign;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class CampaignReportManagedBean {
    @EJB
    CustomerBean customerBean;
    @EJB
    CampaignBean campaignBean;
    @EJB
    BookingClassBean bookingClassBean;
    @EJB
    RouteBean routeBean;

    private List<Campaign> campaignList;
    private String campaignIds;
    private Date fromDate;
    private Date toDate;
    private double uptakeRate;
    private int minUsageCount;
    private int maxUsageCount;
    private Campaign campaign;

    @PostConstruct
    public void init() {
        campaignList = campaignBean.getAllCampaigns();
        campaignIds = "";
        for (Campaign c : campaignList) {
            campaignIds = campaignIds.concat(String.valueOf(c.getId())).concat("-");
        }
    }

    public void viewSelectedCampaign(long id) {
        try {
            campaign = campaignBean.getCampaign(id);
        } catch (NotFoundException e) {
            FacesMessage m = new FacesMessage("Unable to retrieve the campaign from the system, it may have already been deleted.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public boolean displayCampaignDetails() {
        return campaign != null;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    public String getCampaignIds() {
        return campaignIds;
    }

    public void setCampaignIds(String campaignIds) {
        this.campaignIds = campaignIds;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public double getUptakeRate() {
        return uptakeRate;
    }

    public void setUptakeRate(double uptakeRate) {
        this.uptakeRate = uptakeRate;
    }

    public int getMinUsageCount() {
        return minUsageCount;
    }

    public void setMinUsageCount(int minUsageCount) {
        this.minUsageCount = minUsageCount;
    }

    public int getMaxUsageCount() {
        return maxUsageCount;
    }

    public void setMaxUsageCount(int maxUsageCount) {
        this.maxUsageCount = maxUsageCount;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
}
