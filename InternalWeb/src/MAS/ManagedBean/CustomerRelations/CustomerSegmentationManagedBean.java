package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CustomerBean;
import MAS.CustomerAnalysis.AnalysedCustomer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
public class CustomerSegmentationManagedBean {
    @EJB
    CustomerBean customerBean;

    private List<AnalysedCustomer> customers;

    @PostConstruct
    private void init() {
        customers = customerBean.analyseCustomers();
    }


    public List<AnalysedCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<AnalysedCustomer> customers) {
        this.customers = customers;
    }
}
