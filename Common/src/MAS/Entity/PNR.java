package MAS.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class PNR {

    private Date createdTimeStamp;

    @Basic
    public Date getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Date createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    private String PNRNumber;

    @Id
    public String getPNRNumber() {
        return PNRNumber;
    }

    public void setPNRNumber(String PNRNumber) {
        this.PNRNumber = PNRNumber;
    }

    private List<PNRItem> PNRItems;

    @OneToMany
    public List<PNRItem> getPNRItems() {
        return PNRItems;
    }

    public void setPNRItems(List<PNRItem> PNRItems) {
        this.PNRItems = PNRItems;
    }
}
