package MAS.Entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Daryl on 9/11/2015.
 */
@Entity
public class CampaignGroup {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private List<Customer> customers;

    @ManyToMany
    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    private String name;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
