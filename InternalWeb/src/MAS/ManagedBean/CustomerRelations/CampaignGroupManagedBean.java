package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Bean.CustomerBean;
import MAS.Entity.CampaignGroup;
import MAS.Entity.Customer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
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

    @PostConstruct
    private void init() {
        populateCustomers();
    }

    private void populateCustomers() {
        customers = customerBean.getAllCustomers();
        customerMap = new HashMap<>();
        for (Customer customer : customers) {
            customerMap.put(customer.getId(), Boolean.FALSE);
        }
    }

    public void createCampaignGroup() {
        try {

        } catch (Exception e) {

        }
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
