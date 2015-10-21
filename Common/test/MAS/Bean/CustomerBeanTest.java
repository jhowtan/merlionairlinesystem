package MAS.Bean;

import MAS.Entity.Customer;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreateCustomer() throws Exception {
        Customer customer = new Customer();
        assertNotNull(TestEJB.customerBean.createCustomer(customer, "password123"));
    }

    @Test
    public void testGetCustomer() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");
        assertNotNull(TestEJB.customerBean.getCustomer(customer.getId()));

        try {
            TestEJB.customerBean.getCustomer(12345);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Customer customer = new Customer();
        TestEJB.customerBean.createCustomer(customer, "password123");
        TestEJB.customerBean.updateCustomer(customer);

        Customer newCustomer = new Customer();
        try {
            TestEJB.customerBean.updateCustomer(newCustomer);
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        Customer customer = new Customer();
        TestEJB.customerBean.createCustomer(customer, "password123");
        assertFalse(TestEJB.customerBean.getAllCustomers().isEmpty());
    }

    @Test
    public void testIsEmailUnique() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        TestEJB.customerBean.createCustomer(customer, "password123");
        assertTrue(TestEJB.customerBean.isEmailUnique("non-existent@example.com"));
        assertFalse(TestEJB.customerBean.isEmailUnique("test@example.com"));
    }

    @Test
    public void testLogin() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");
        assertNotNull(TestEJB.customerBean.login(customer.getId(), "password123"));

        try {
            assertNotNull(TestEJB.customerBean.login(12345, "password123"));
        } catch (InvalidLoginException e) {}

        try {
            assertNotNull(TestEJB.customerBean.login(customer.getId(), "wrongpassword"));
        } catch (InvalidLoginException e) {}

    }
}