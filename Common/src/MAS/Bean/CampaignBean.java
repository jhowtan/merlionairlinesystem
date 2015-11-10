package MAS.Bean;

import MAS.Entity.Campaign;
import MAS.Entity.CampaignGroup;
import MAS.Entity.Customer;
import MAS.Entity.Route;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "CampaignEJB")
@LocalBean
public class CampaignBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    CustomerBean customerBean;
    @EJB
    RouteBean routeBean;

    public CampaignBean() {
    }

    public long createCampaign(String name, Date startDate, Date endDate, double discount, List<String> bookingClasses,
                               List<Long> routeIds, Date targetStartDate, Date targetEndDate, String code) throws NotFoundException {
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < routeIds.size(); i++) {
            Route route = routeBean.getRoute(routeIds.get(i));
            if (route == null) throw new NotFoundException();
            routes.add(route);
        }
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setTargetEndDate(targetEndDate);
        campaign.setTargetStartDate(targetStartDate);
        campaign.setDiscount(discount);
        campaign.setCode(code);
        campaign.setBookingClasses(bookingClasses);
        campaign.setUsageCount(0);
        campaign.setCampaignGroups(new ArrayList<>());
        campaign.setRoutes(routes);
        em.persist(campaign);
        em.flush();
        return campaign.getId();
    }

    public void updateCampaign(long id, String name, Date startDate, Date endDate, double discount, List<String> bookingClasses, List<Long> routeIds,
                               Date targetStartDate, Date targetEndDate, String code) throws NotFoundException {
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < routeIds.size(); i++) {
            Route route = routeBean.getRoute(routeIds.get(i));
            if (route == null) throw new NotFoundException();
            routes.add(route);
        }
        Campaign campaign = em.find(Campaign.class, id);
        if (campaign == null) throw new NotFoundException();
        campaign.setName(name);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setTargetEndDate(targetEndDate);
        campaign.setTargetStartDate(targetStartDate);
        campaign.setDiscount(discount);
        campaign.setCode(code);
        campaign.setBookingClasses(bookingClasses);
        campaign.setCampaignGroups(new ArrayList<>());
        campaign.setRoutes(routes);
        em.persist(campaign);
    }

    public void addCampaignGroup(long campaignId, long campaignGrpId) throws NotFoundException {
        Campaign campaign = em.find(Campaign.class, campaignId);
        CampaignGroup campaignGroup = em.find(CampaignGroup.class, campaignGrpId);
        if (campaign == null || campaignGroup == null) throw new NotFoundException();
        List<CampaignGroup> campaignGroups = campaign.getCampaignGroups();
        if (campaignGroups.indexOf(campaignGroup) == -1)
            campaignGroups.add(campaignGroup);
        campaign.setCampaignGroups(campaignGroups);
        em.persist(campaign);
    }

    public void removeCampaignGroup(long campaignId, long campaignGrpId) throws NotFoundException {
        Campaign campaign = em.find(Campaign.class, campaignId);
        CampaignGroup campaignGroup = em.find(CampaignGroup.class, campaignGrpId);
        if (campaign == null || campaignGroup == null) throw new NotFoundException();
        List<CampaignGroup> campaignGroups = campaign.getCampaignGroups();
        if (campaignGroups.indexOf(campaignGroup) != -1)
            campaignGroups.remove(campaignGroup);
        campaign.setCampaignGroups(campaignGroups);
        em.persist(campaign);
    }

    public void removeCampaign(long id) throws NotFoundException {
        Campaign campaign = em.find(Campaign.class, id);
        if (campaign == null) throw new NotFoundException();
        em.remove(campaign);
    }

    public Campaign getCampaign(long id) throws NotFoundException {
        Campaign campaign = em.find(Campaign.class, id);
        if (campaign == null) throw new NotFoundException();
        return campaign;
    }

    public List<Campaign> getAllCampaigns() {
        return em.createQuery("SELECT c from Campaign c", Campaign.class).getResultList();
    }

    public long createCampaignGroup(List<Long> customerIds, String name, String description) throws NotFoundException {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < customerIds.size(); i++) {
            Customer customer = customerBean.getCustomer(customerIds.get(i));
            if (customer == null) throw new NotFoundException();
            customers.add(customer);
        }
        System.out.println(customers);
        CampaignGroup campaignGroup = new CampaignGroup();
        campaignGroup.setCustomers(customers);
        campaignGroup.setName(name);
        campaignGroup.setDescription(description);
        em.persist(campaignGroup);
        em.flush();
        return campaignGroup.getId();
    }

    public void updateCampaignGroup(long id, List<Long> customerIds, String name, String description) throws NotFoundException {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < customerIds.size(); i++) {
            Customer customer = customerBean.getCustomer(customerIds.get(i));
            if (customer == null) throw new NotFoundException();
            customers.add(customer);
        }
        CampaignGroup campaignGroup = em.find(CampaignGroup.class, id);
        if (campaignGroup == null) throw new NotFoundException();
        campaignGroup.setCustomers(customers);
        campaignGroup.setName(name);
        campaignGroup.setDescription(description);
    }

    public void removeCampaignGroup(long id) throws NotFoundException {
        CampaignGroup campaignGroup = em.find(CampaignGroup.class, id);
        if (campaignGroup == null) throw new NotFoundException();
        em.remove(campaignGroup);
    }

    public CampaignGroup getCampaignGroup(long id) throws NotFoundException {
        CampaignGroup campaignGroup = em.find(CampaignGroup.class, id);
        if (campaignGroup == null) throw new NotFoundException();
        return campaignGroup;
    }

    public List<CampaignGroup> getAllCampaignGroups() {
        return em.createQuery("SELECT c from CampaignGroup c", CampaignGroup.class).getResultList();
    }
}
