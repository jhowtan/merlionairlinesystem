package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Bean.CustomerBean;
import MAS.Entity.CampaignGroup;
import MAS.Entity.Customer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class CampaignGroupManagedBean {

    @EJB
    CampaignBean campaignBean;
    @EJB
    CustomerBean customerBean;

    private List<CampaignGroup> campaignGroups;
    private String groupName;
    private String desc;
    private List<String> customerIds;
    private List<Customer> customers;
    private Map<Long, Boolean> customerMap;

    private Map<String,String> params;
    private CampaignGroup campaignGroup;
    private long campaignGroupId;

    @PostConstruct
    private void init() {
        try {
            params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            campaignGroupId = Long.parseLong(params.get("cmGrpId"));
            campaignGroup = campaignBean.getCampaignGroup(campaignGroupId);
            groupName = campaignGroup.getName();
            desc = campaignGroup.getDescription();
            populateExistingCustomers();
        } catch (Exception e) {
            populateCustomers();
            load();
        }
    }

    private void load() {
        campaignGroups = campaignBean.getAllCampaignGroups();
    }

    private void populateCustomers() {
        customers = customerBean.getAllCustomers();
        customerMap = new HashMap<>();
        for (Customer customer : customers) {
            customerMap.put(customer.getId(), Boolean.FALSE);
        }
    }

    private void populateExistingCustomers() {
        customers = customerBean.getAllCustomers();
        customerMap = new HashMap<>();
        List<Customer> groupMembers = campaignGroup.getCustomers();
        for (Customer customer : customers) {
            if (groupMembers.contains(customer)){
                customerMap.put(customer.getId(), Boolean.TRUE);
            } else {
                customerMap.put(customer.getId(), Boolean.FALSE);
            }
        }
    }

    public void createCampaignGroup() {
        try {
            ArrayList<Long> customerIds = new ArrayList<>();
            for (Object o : customerMap.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                if ((Boolean) pair.getValue()) {
                    customerIds.add((Long) pair.getKey());
                }
            }
            campaignBean.createCampaignGroup(customerIds, groupName, desc);
            groupName = "";
            desc = "";
            FacesMessage m = new FacesMessage("Customer created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Could not create customer group.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void saveCampaignGroup() {
        try {
            ArrayList<Long> customerIds = new ArrayList<>();
            for (Object o : customerMap.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                if ((Boolean) pair.getValue()) {
                    customerIds.add((Long) pair.getKey());
                }
            }
            campaignBean.updateCampaignGroup(campaignGroup.getId(), customerIds, groupName, desc);
            FacesMessage m = new FacesMessage("Customer group updated successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Customer group could not be updated.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public void delete(CampaignGroup campaignGroup) {
        try {
            campaignBean.removeCampaignGroup(campaignGroup.getId());
            FacesMessage m = new FacesMessage("Customer group: " + campaignGroup.getName() + " deleted.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Customer group: " + campaignGroup.getName() + " could not be deleted.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
        load();
    }

    public List<CampaignGroup> getCampaignGroups() {
        return campaignGroups;
    }

    public void setCampaignGroups(List<CampaignGroup> campaignGroups) {
        this.campaignGroups = campaignGroups;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<String> customerIds) {
        this.customerIds = customerIds;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Map<Long, Boolean> getCustomerMap() {
        return customerMap;
    }

    public void setCustomerMap(Map<Long, Boolean> customerMap) {
        this.customerMap = customerMap;
    }
}
