package MAS.Bean;

import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class AuditLogBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreateAuditLog() throws Exception {
        try {
            TestEJB.auditLogBean.createAuditLog(12345, "Description", "test");
            fail();
        } catch (NotFoundException e) {}
        assertNotNull(TestEJB.auditLogBean.createAuditLog(TestEJB.userBean.getAllUsers().get(0).getId(), "Description", "test"));
    }

    @Test
    public void testGetAllAuditLogs() throws Exception {
        TestEJB.auditLogBean.createAuditLog(TestEJB.userBean.getAllUsers().get(0).getId(), "Description", "test");
        assertFalse(TestEJB.auditLogBean.getAllAuditLogs().isEmpty());
    }

    @Test
    public void testGetAuditLogForUser() throws Exception {
        try {
            TestEJB.auditLogBean.getAuditLogForUser(12345);
            fail();
        } catch (NotFoundException e) {}
        long userId = TestEJB.userBean.getAllUsers().get(0).getId();
        TestEJB.auditLogBean.createAuditLog(userId, "Description", "test");
        assertFalse(TestEJB.auditLogBean.getAuditLogForUser(userId).isEmpty());
    }
}