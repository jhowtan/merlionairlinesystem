package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CampaignBean;
import MAS.Bean.CustomerBean;
import MAS.Common.Constants;
import MAS.CustomerAnalysis.AnalysedCustomer;
import MAS.ManagedBean.Auth.AuthManagedBean;
import MAS.ManagedBean.CommonManagedBean;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@ManagedBean
public class CustomerSegmentationManagedBean {
    @EJB
    CustomerBean customerBean;
    @EJB
    CampaignBean campaignBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private List<AnalysedCustomer> customers;
    private List<List<AnalysedCustomer>> segmentedCustomers;
    private Map<Long, Boolean> customerMap;

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    private class CustomerItem {
        public String name;
        public String y;
        public String x;
        public String cV;
        public String size;
    }

    @PostConstruct
    private void init() {
        customers = customerBean.analyseCustomers();
        segmentedCustomers = new ArrayList<>();
        customerMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            segmentedCustomers.add(new ArrayList<>());
            for (int j = 0; j < customers.size(); j++) {
                if (customers.get(j).segment == i) {
                    segmentedCustomers.get(i).add(customers.get(j));
                    customerMap.put(customers.get(j).customer.getId(), Boolean.FALSE);
                    customers.remove(j);
                    j--;
                }
            }
        }
    }

    public void getSegmentJSON() {
        ArrayList<CustomerItem> custItems = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < segmentedCustomers.get(i).size(); j++) {
                AnalysedCustomer customer = segmentedCustomers.get(i).get(j);
                CustomerItem customerItem = new CustomerItem();
                customerItem.name = customer.customer.getFirstName() + " " + customer.customer.getLastName();
                customerItem.x = String.valueOf(customer.flightCount);
                customerItem.y = CommonManagedBean.formatDoubleTwoDecimal(customer.revenuePerMile);
                customerItem.cV = CommonManagedBean.formatDoubleTwoDecimal(customer.cV);
                customerItem.size = CommonManagedBean.formatDoubleTwoDecimal(customer.pV);
                custItems.add(customerItem);
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(custItems);

        outputJSON(json);
    }

    public void outputJSON(String json) {
        if(!authManagedBean.isAuthenticated()) {
            json = "[]";
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
        response.setContentLength(json.length());
        response.setContentType("application/json");

        try {
            response.getOutputStream().write(json.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ctx.responseComplete();
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
            campaignBean.createCampaignGroup(customerIds, "Customer group analysed on " + CommonManagedBean.formatDate("dd,MMM,yy", new Date()), "Auto-generated group from customer analysis.");
            FacesMessage m = new FacesMessage("Customer group created successfully.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage("Could not create customer group.");
            m.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("status", m);
        }
    }

    public String displaySegment(AnalysedCustomer customer) {
        return Constants.CUSTOMER_SEGMENT_NAMES[customer.segment];
    }

    public List<AnalysedCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<AnalysedCustomer> customers) {
        this.customers = customers;
    }

    public List<List<AnalysedCustomer>> getSegmentedCustomers() {
        return segmentedCustomers;
    }

    public void setSegmentedCustomers(List<List<AnalysedCustomer>> segmentedCustomers) {
        this.segmentedCustomers = segmentedCustomers;
    }

    public Map<Long, Boolean> getCustomerMap() {
        return customerMap;
    }

    public void setCustomerMap(Map<Long, Boolean> customerMap) {
        this.customerMap = customerMap;
    }

}
