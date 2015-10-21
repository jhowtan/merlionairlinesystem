package MAS.Bean;

import MAS.InitEJBContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;

import static org.junit.Assert.assertEquals;

public class AttributesBeanTest {
    private EJBContainer container;
    private AttributesBean attributesBean;

    @Before
    public void setUp() throws Exception {
        container = InitEJBContainer.init();
        attributesBean = (AttributesBean) container.getContext().lookup("java:global/Common/AttributesEJB");
    }

    @After
    public void tearDown() throws Exception {
        container.close();
    }

    @Test
    public void testGetStringAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals("test123", attributesBean.getStringAttribute("attr1", "test123"));
        // Getting an existing attribute should return the attribute value
        assertEquals("test123", attributesBean.getStringAttribute("attr1", "test987"));
    }

    @Test
    public void testGetDoubleAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals(0.123, attributesBean.getDoubleAttribute("attr1", 0.123), 0.0001);
        // Getting an existing attribute should return the attribute value
        assertEquals(0.123, attributesBean.getDoubleAttribute("attr1", 0.987), 0.0001);
    }

    @Test
    public void testGetIntAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals(123, attributesBean.getIntAttribute("attr1", 123));
        // Getting an existing attribute should return the attribute value
        assertEquals(123, attributesBean.getIntAttribute("attr1", 987));
    }

    @Test
    public void testGetBooleanAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals(true, attributesBean.getBooleanAttribute("attr1", true));
        // Getting an existing attribute should return the attribute value
        assertEquals(true, attributesBean.getBooleanAttribute("attr1", false));
    }

    @Test
    public void testSetStringAttribute() throws Exception {
        attributesBean.setStringAttribute("attr2", "test123");
        assertEquals("test123", attributesBean.getStringAttribute("attr2", "test987"));
        attributesBean.setStringAttribute("attr2", "test456");
        assertEquals("test456", attributesBean.getStringAttribute("attr2", "test987"));
    }

    @Test
    public void testSetDoubleAttribute() throws Exception {
        attributesBean.setDoubleAttribute("attr2", 0.123);
        assertEquals(0.123, attributesBean.getDoubleAttribute("attr2", 0.987), 0.0001);
        attributesBean.setDoubleAttribute("attr2", 0.456);
        assertEquals(0.456, attributesBean.getDoubleAttribute("attr2", 0.987), 0.0001);
    }

    @Test
    public void testSetIntAttribute() throws Exception {
        attributesBean.setIntAttribute("attr2", 123);
        assertEquals(123, attributesBean.getIntAttribute("attr2", 987));
        attributesBean.setIntAttribute("attr2", 456);
        assertEquals(456, attributesBean.getIntAttribute("attr2", 987));
    }

    @Test
    public void testSetBooleanAttribute() throws Exception {
        attributesBean.setBooleanAttribute("attr2", true);
        assertEquals(true, attributesBean.getBooleanAttribute("attr2", false));
        attributesBean.setBooleanAttribute("attr2", false);
        assertEquals(false, attributesBean.getBooleanAttribute("attr2", false));
    }
}