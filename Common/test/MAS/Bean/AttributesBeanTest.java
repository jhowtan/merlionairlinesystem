package MAS.Bean;

import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testGetStringAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals("test123", TestEJB.attributesBean.getStringAttribute("attr1", "test123"));
        // Getting an existing attribute should return the attribute value
        assertEquals("test123", TestEJB.attributesBean.getStringAttribute("attr1", "test987"));
    }

    @Test
    public void testGetDoubleAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals(0.123, TestEJB.attributesBean.getDoubleAttribute("attr1", 0.123), 0.0001);
        // Getting an existing attribute should return the attribute value
        assertEquals(0.123, TestEJB.attributesBean.getDoubleAttribute("attr1", 0.987), 0.0001);
    }

    @Test
    public void testGetIntAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals(123, TestEJB.attributesBean.getIntAttribute("attr1", 123));
        // Getting an existing attribute should return the attribute value
        assertEquals(123, TestEJB.attributesBean.getIntAttribute("attr1", 987));
    }

    @Test
    public void testGetBooleanAttribute() throws Exception {
        // Getting a non-existent attribute should return the default and set it to the default
        assertEquals(true, TestEJB.attributesBean.getBooleanAttribute("attr1", true));
        // Getting an existing attribute should return the attribute value
        assertEquals(true, TestEJB.attributesBean.getBooleanAttribute("attr1", false));
    }

    @Test
    public void testSetStringAttribute() throws Exception {
        TestEJB.attributesBean.setStringAttribute("attr2", "test123");
        assertEquals("test123", TestEJB.attributesBean.getStringAttribute("attr2", "test987"));
        TestEJB.attributesBean.setStringAttribute("attr2", "test456");
        assertEquals("test456", TestEJB.attributesBean.getStringAttribute("attr2", "test987"));
    }

    @Test
    public void testSetDoubleAttribute() throws Exception {
        TestEJB.attributesBean.setDoubleAttribute("attr2", 0.123);
        assertEquals(0.123, TestEJB.attributesBean.getDoubleAttribute("attr2", 0.987), 0.0001);
        TestEJB.attributesBean.setDoubleAttribute("attr2", 0.456);
        assertEquals(0.456, TestEJB.attributesBean.getDoubleAttribute("attr2", 0.987), 0.0001);
    }

    @Test
    public void testSetIntAttribute() throws Exception {
        TestEJB.attributesBean.setIntAttribute("attr2", 123);
        assertEquals(123, TestEJB.attributesBean.getIntAttribute("attr2", 987));
        TestEJB.attributesBean.setIntAttribute("attr2", 456);
        assertEquals(456, TestEJB.attributesBean.getIntAttribute("attr2", 987));
    }

    @Test
    public void testSetBooleanAttribute() throws Exception {
        TestEJB.attributesBean.setBooleanAttribute("attr2", true);
        assertEquals(true, TestEJB.attributesBean.getBooleanAttribute("attr2", false));
        TestEJB.attributesBean.setBooleanAttribute("attr2", false);
        assertEquals(false, TestEJB.attributesBean.getBooleanAttribute("attr2", false));
    }
}