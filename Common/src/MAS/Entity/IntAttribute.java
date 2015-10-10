package MAS.Entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IntAttribute {
    private String key;

    @Id
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private int value;

    @Basic
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
