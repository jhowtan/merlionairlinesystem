package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Bean.RouteBean;
import MAS.Entity.Campaign;
import MAS.Entity.CampaignGroup;
import MAS.Entity.Route;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
@ViewScoped
public class CampaignManagedBean {
    @EJB
    CampaignBean campaignBean;
    @EJB
    RouteBean routeBean;

    private Map<String,String> params;
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
    private List<String> campaignGroupIds;
    private String bookingClassesString;
    private Campaign campaign;

    @PostConstruct
    private void init() {
        try {
            params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            long campaignId = Long.parseLong(params.get("campaignId"));
            campaign = campaignBean.getCampaign(campaignId);
            startDate = campaign.getStartDate();
            endDate = campaign.getEndDate();
            targetStartDate = campaign.getTargetStartDate();
            targetEndDate = campaign.getTargetEndDate();
            campaignName = campaign.getName();
            discount = campaign.getDiscount();
            code = campaign.getCode();
            campaignGroupIds = new ArrayList<>();
            for (int i = 0; i < campaign.getCampaignGroups().size(); i++) {
                campaignGroupIds.add(String.valueOf(campaign.getCampaignGroups().get(i).getId()));
            }
            System.out.println(campaign.getCampaignGroups());
            routeIds = new ArrayList<>();
            for (int i = 0; i < campaign.getRoutes().size(); i++) {
                routeIds.add(String.valueOf(campaign.getRoutes().get(i).getId()));
            }
            bookingClassesString = "";
            for (int i = 0; i < campaign.getBookingClasses().size(); i++) {
                bookingClassesString = bookingClassesString.concat(campaign.getBookingClasses().get(i) + ",");
            }
            if (bookingClassesString.length() > 1) bookingClassesString = bookingClassesString.substring(0, bookingClassesString.length() - 1);
        } catch (Exception e) {

        }
        load();
    }

    private void load() {
        campaigns = campaignBean.getAllCampaigns();
        campaignGroups = campaignBean.getAllCampaignGroups();
        routes = routeBean.getAllRoutes();
    }

    public void createCampaign() {
        try {
            List<String> bkClasses = Arrays.asList(bookingClassesString.replaceAll("^[,\\s]+", "").split("[,\\s]+"));
            List<Long> routeIdLongs = new ArrayList<>();
            for (String s : routeIds) {
                routeIdLongs.add(Long.parseLong(s));
            }
            long campaignId = campaignBean.createCampaign(campaignName, startDate, endDate, discount, bkClasses, routeIdLongs, targetStartDate, targetEndDate, code);
            if (campaignGroupIds != null) {
                for (String s : campaignGroupIds) {
                    campaignBean.addCampaignGroup(campaignId, Long.valueOf(s));
                }
            }
            FacesMessage m = new FacesMessage("Campaign: " + campaignName + " created.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage m = new FacesMessage("Campaign could not be created.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void saveCampaign() {
        try {
            List<String> bkClasses = Arrays.asList(bookingClassesString.replaceAll("^[,\\s]+", "").split("[,\\s]+"));
            List<Long> routeIdLongs = new ArrayList<>();
            for (String s : routeIds) {
                routeIdLongs.add(Long.parseLong(s));
            }
            campaignBean.updateCampaign(campaign.getId(), campaignName, startDate, endDate, discount, bkClasses, routeIdLongs, targetStartDate, targetEndDate, code);
            for (String s : campaignGroupIds) {
                campaignBean.addCampaignGroup(campaign.getId(), Long.valueOf(s));
            }
            FacesMessage m = new FacesMessage("Campaign: " + campaign.getName() + " updated.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Campaign: " + campaign.getName() + " could not be updated.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void delete(Campaign campaign) {
        try {
            campaignBean.removeCampaign(campaign.getId());
            FacesMessage m = new FacesMessage("Campaign: " + campaign.getName() + " deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Campaign: " + campaign.getName() + " could not be deleted.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
        load();
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

    public String getBookingClassesString() {
        return bookingClassesString;
    }

    public void setBookingClassesString(String bookingClassesString) {
        this.bookingClassesString = bookingClassesString;
    }

    public List<String> getCampaignGroupIds() {
        return campaignGroupIds;
    }

    public void setCampaignGroupIds(List<String> campaignGroupIds) {
        this.campaignGroupIds = campaignGroupIds;
    }
}
