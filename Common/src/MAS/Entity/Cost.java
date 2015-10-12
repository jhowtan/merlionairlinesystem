package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Daryl Ho on 10/10/2015.
 */
@Entity
public class Cost {

    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String comments;

    @Basic
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private int type;

    @Basic
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private double amount;

    @Basic
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private long assocId;

    @Basic
    public long getAssocId() {
        return assocId;
    }

    public void setAssocId(long assocId) {
        this.assocId = assocId;
    }

    private Date date;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
