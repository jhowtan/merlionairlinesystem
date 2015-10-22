package MAS.Bean;

import MAS.Entity.BooleanAttribute;
import MAS.Entity.DoubleAttribute;
import MAS.Entity.IntAttribute;
import MAS.Entity.StringAttribute;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "AttributesEJB")
@LocalBean
public class AttributesBean {
    @PersistenceContext
    EntityManager em;

    public AttributesBean() {

    }

    public String getStringAttribute(String key, String defaultValue) {
        StringAttribute attribute = em.find(StringAttribute.class, key);
        if (attribute == null) {
            attribute = new StringAttribute();
            attribute.setKey(key);
            attribute.setValue(defaultValue);
            em.persist(attribute);
        }
        return attribute.getValue();
    }

    public void setStringAttribute(String key, String value) {
        StringAttribute attribute = new StringAttribute();
        attribute.setKey(key);
        attribute.setValue(value);
        em.merge(attribute);
        em.flush();
    }

    public double getDoubleAttribute(String key, double defaultValue) {
        DoubleAttribute attribute = em.find(DoubleAttribute.class, key);
        if (attribute == null) {
            attribute = new DoubleAttribute();
            attribute.setKey(key);
            attribute.setValue(defaultValue);
            em.persist(attribute);
        }
        return attribute.getValue();
    }

    public void setDoubleAttribute(String key, double value) {
        DoubleAttribute attribute = new DoubleAttribute();
        attribute.setKey(key);
        attribute.setValue(value);
        em.merge(attribute);
        em.flush();
    }

    public int getIntAttribute(String key, int defaultValue) {
        IntAttribute attribute = em.find(IntAttribute.class, key);
        if (attribute == null) {
            attribute = new IntAttribute();
            attribute.setKey(key);
            attribute.setValue(defaultValue);
            em.persist(attribute);
            em.flush();
        }
        return attribute.getValue();
    }

    public void setIntAttribute(String key, int value) {
        IntAttribute attribute = new IntAttribute();
        attribute.setKey(key);
        attribute.setValue(value);
        em.merge(attribute);
        em.flush();
    }

    public boolean getBooleanAttribute(String key, boolean defaultValue) {
        BooleanAttribute attribute = em.find(BooleanAttribute.class, key);
        if (attribute == null) {
            attribute = new BooleanAttribute();
            attribute.setKey(key);
            attribute.setValue(defaultValue);
            em.persist(attribute);
            em.flush();
        }
        return attribute.getValue();
    }

    public void setBooleanAttribute(String key, boolean value) {
        BooleanAttribute attribute = new BooleanAttribute();
        attribute.setKey(key);
        attribute.setValue(value);
        em.merge(attribute);
        em.flush();
    }
}
