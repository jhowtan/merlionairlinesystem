package MAS.Entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DoubleAttribute {
    private String key;

    @Id
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private double value;

    @Basic
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
