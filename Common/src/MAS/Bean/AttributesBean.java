package MAS.Bean;

import MAS.Common.Constants;
import MAS.Entity.DoubleAttribute;
import MAS.Entity.IntAttribute;
import MAS.Entity.StringAttribute;

import javax.annotation.PostConstruct;
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
    @PostConstruct
    private void init() {
        setDoubleAttribute(Constants.AVERAGE_PERSON_WEIGHT, 62.0);
        setDoubleAttribute(Constants.AVERAGE_BAGGAGE_WEIGHT, 32.0);
        setDoubleAttribute(Constants.CABIN_CREW_SALARY, 48000);
        setDoubleAttribute(Constants.COCKPIT_CREW_SALARY, 100000);
        setDoubleAttribute(Constants.FUEL_WEIGHT, 0.81);
        setIntAttribute(Constants.FLIGHTS_PER_YEAR, 100);
        setIntAttribute(Constants.MAINTENANCE_PER_YEAR, 15);
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
}
