package MAS.Entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StringAttribute {
    private String key;

    @Id
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String value;

    @Basic
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
