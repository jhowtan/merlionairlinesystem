package MAS.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CostOverTime {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private double amount;

    @Basic
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private String comments;

    @Basic
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private int days;

    @Basic
    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    private Date startTime;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
