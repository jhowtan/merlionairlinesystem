package MAS.Entity;

import javax.persistence.*;

@Entity
public class MaintenanceReport {
    private long id;

    @GeneratedValue
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private User user;

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private AircraftMaintenanceSlot maintenanceSlot;

    @ManyToOne
    public AircraftMaintenanceSlot getMaintenanceSlot() {
        return maintenanceSlot;
    }

    public void setMaintenanceSlot(AircraftMaintenanceSlot maintenanceSlot) {
        this.maintenanceSlot = maintenanceSlot;
    }

    private int status;

    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String body;

    @Basic
    @Lob
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private int category;

    @Basic
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
