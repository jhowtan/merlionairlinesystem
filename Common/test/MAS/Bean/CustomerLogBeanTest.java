package MAS.Bean;

import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerLogBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreateCustomerLog() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");

        assertNotNull(TestEJB.customerLogBean.createCustomerLog(customer.getId(), "Description", "category"));

        try {
            TestEJB.customerLogBean.createCustomerLog(12345, "Description", "category");
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetAllCustomerLogs() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");
        TestEJB.customerLogBean.createCustomerLog(customer.getId(), "Description", "category");

        assertFalse(TestEJB.customerLogBean.getAllCustomerLogs().isEmpty());
    }

    @Test
    public void testGetCustomerLogForCustomer() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");
        TestEJB.customerLogBean.createCustomerLog(customer.getId(), "Description", "category");

        assertFalse(TestEJB.customerLogBean.getCustomerLogForCustomer(customer.getId()).isEmpty());

        try {
            TestEJB.customerLogBean.getCustomerLogForCustomer(12345);
            fail();
        } catch (NotFoundException e) {}
    }
}