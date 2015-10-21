package MAS.Bean;

import MAS.Common.Constants;
import MAS.Entity.Customer;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FFPBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreditMiles() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");

        assertEquals(1000, TestEJB.ffpBean.creditMiles(customer.getId(), 1000).getMiles());

        assertEquals(2000, TestEJB.ffpBean.creditMiles(customer.getId(), 1000).getMiles());

        try {
            TestEJB.ffpBean.creditMiles(12345, 1000);
        } catch (NotFoundException e) {}

    }

    @Test
    public void testCreditEliteMiles() throws Exception {
        Customer customer = new Customer();
        customer = TestEJB.customerBean.createCustomer(customer, "password123");

        assertEquals(Constants.FFP_TIER_BLUE, TestEJB.ffpBean.creditEliteMiles(customer.getId(), 20000).getTier());
        assertEquals(Constants.FFP_TIER_SILVER, TestEJB.ffpBean.creditEliteMiles(customer.getId(), 20000).getTier());
        assertEquals(Constants.FFP_TIER_GOLD, TestEJB.ffpBean.creditEliteMiles(customer.getId(), 50000).getTier());

        try {
            TestEJB.ffpBean.creditEliteMiles(12345, 1000);
        } catch (NotFoundException e) {}

    }
}