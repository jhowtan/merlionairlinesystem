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
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
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
    private List<Campaign> resultList;
    private String campaignIds;
    private Date fromDate;
    private Date toDate;
    private int minUsageCount;
    private int maxUsageCount;
    private Campaign campaign;

    @PostConstruct
    public void init() {
        campaignList = campaignBean.getAllCampaigns();
        resultList = new ArrayList<>(campaignList);
        campaignIds = "";
        for (Campaign c : campaignList) {
            campaignIds = campaignIds.concat(String.valueOf(c.getId())).concat("-");
        }
        setMinUsageCount(0);
        setMaxUsageCount(0);
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

    public double getConversionRate(long id) {
        try {
            return campaignBean.getConversionRate(id);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean displayCampaignDetails() {
        return campaign != null;
    }

    public void filterChangeEvent(AjaxBehaviorEvent event) {
        resultList = new ArrayList<>(campaignList);
        for (int i = 0; i < resultList.size(); i++) {
            Campaign c = resultList.get(i);
            if (fromDate != null) {
                if (c.getStartDate().compareTo(fromDate) == -1) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (toDate != null) {
                if (c.getEndDate().compareTo(toDate) == 1) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (minUsageCount != 0) {
                if (c.getUsageCount() < minUsageCount) {
                    resultList.remove(i);
                    i--;
                    continue;
                }
            }
            if (maxUsageCount != 0) {
                if (c.getUsageCount() > maxUsageCount) {
                    resultList.remove(i);
                    i--;
                }
            }
        }
        campaignIds = "";
        for (Campaign c : resultList) {
            campaignIds = campaignIds.concat(String.valueOf(c.getId())).concat("-");
        }
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

    public List<Campaign> getResultList() {
        return resultList;
    }

    public void setResultList(List<Campaign> resultList) {
        this.resultList = resultList;
    }
}
