package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Campaign;
import MAS.Entity.CampaignGroup;
import MAS.Entity.Route;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

@ManagedBean
public class CampaignsManagedBean {
    @EJB
    CampaignBean campaignBean;
    @EJB
    RouteBean routeBean;

    private List<Campaign> campaigns;
    private List<Route> routes;
    private List<CampaignGroup> campaignGroups;

    private Date startDate;
    private Date endDate;
    private Date targetStartDate;
    private Date targetEndDate;
    private String campaignName;
    private double discount;
    private String code;
    private List<String> routeIds;

    @PostConstruct
    private void init() {
        load();
    }

    private void load() {
        campaigns = campaignBean.getAllCampaigns();
        campaignGroups = campaignBean.getAllCampaignGroups();
        routes = routeBean.getAllRoutes();
    }

    public void createCampaign() {

    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<CampaignGroup> getCampaignGroups() {
        return campaignGroups;
    }

    public void setCampaignGroups(List<CampaignGroup> campaignGroups) {
        this.campaignGroups = campaignGroups;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getTargetStartDate() {
        return targetStartDate;
    }

    public void setTargetStartDate(Date targetStartDate) {
        this.targetStartDate = targetStartDate;
    }

    public Date getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }
}
