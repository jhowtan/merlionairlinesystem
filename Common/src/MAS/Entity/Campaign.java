package MAS.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Daryl on 9/11/2015.
 */
@Entity
public class Campaign {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String name;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Date startDate;

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    private Date endDate;

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    private double discount;

    @Basic
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    private List<Route> routes;

    @ManyToMany
    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    private Date targetStartDate;

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getTargetStartDate() {
        return targetStartDate;
    }

    public void setTargetStartDate(Date targetStartDate) {
        this.targetStartDate = targetStartDate;
    }

    private Date targetEndDate;

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    private List<String> bookingClasses;

    @ElementCollection
    public List<String> getBookingClasses() {
        return bookingClasses;
    }

    public void setBookingClasses(List<String> bookingClasses) {
        this.bookingClasses = bookingClasses;
    }

    private String code;

    @Basic
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private int usage;

    @Basic
    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    private List<CampaignGroup> campaignGroups;

    @ManyToMany
    public List<CampaignGroup> getCampaignGroups() {
        return campaignGroups;
    }

    public void setCampaignGroups(List<CampaignGroup> campaignGroups) {
        this.campaignGroups = campaignGroups;
    }
}
