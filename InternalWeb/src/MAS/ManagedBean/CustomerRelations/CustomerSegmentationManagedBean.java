package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CustomerBean;
import MAS.CustomerAnalysis.AnalysedCustomer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class CustomerSegmentationManagedBean {
    @EJB
    CustomerBean customerBean;

    private List<AnalysedCustomer> customers;
    private List<List<AnalysedCustomer>> segmentedCustomers;

    @PostConstruct
    private void init() {
        customers = customerBean.analyseCustomers();
        segmentedCustomers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            segmentedCustomers.add(new ArrayList<>());
            for (int j = 0; j < customers.size(); j++) {
                if (customers.get(j).segment == i) {
                    segmentedCustomers.get(i).add(customers.get(j));
                    customers.remove(j);
                    j--;
                }
            }
        }
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
}
