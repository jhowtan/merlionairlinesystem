package MAS.Bean;

import MAS.Entity.User;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class WorkgroupBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreateWorkgroup() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        assertNotNull(TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId())));
    }

    @Test
    public void testEditWorkgroup() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));
        assertEquals(workgroupId, TestEJB.workgroupBean.editWorkgroup(workgroupId, "Test Workgroup", "Test Description"));
    }

    @Test
    public void testSetWorkgroupUsers() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        TestEJB.workgroupBean.setWorkgroupUsers(workgroupId, Collections.singletonList(user.getId()));

        try {
            TestEJB.workgroupBean.setWorkgroupUsers(12345, Collections.singletonList(user.getId()));
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testChangeWorkgroupOwner() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        User user1 = TestEJB.userBean.getAllUsers().get(1);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        TestEJB.workgroupBean.changeWorkgroupOwner(workgroupId, user.getId());

        try {
            TestEJB.workgroupBean.changeWorkgroupOwner(workgroupId, 12345);
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.workgroupBean.changeWorkgroupOwner(12345, user.getId());
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.workgroupBean.changeWorkgroupOwner(12345, user.getId());
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.workgroupBean.changeWorkgroupOwner(workgroupId, user1.getId());
            fail();
        } catch (NotFoundException e) {}

    }

    @Test
    public void testRemoveWorkgroup() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        TestEJB.workgroupBean.removeWorkgroup(workgroupId);

        try {
            TestEJB.workgroupBean.removeWorkgroup(workgroupId);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testRemoveUserFromWorkgroup() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        TestEJB.workgroupBean.removeUserFromWorkgroup(user.getId(), workgroupId);

        try {
            TestEJB.workgroupBean.removeUserFromWorkgroup(user.getId(), 12345);
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.workgroupBean.removeUserFromWorkgroup(12345, workgroupId);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testIsNameUnique() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        assertTrue(TestEJB.workgroupBean.isNameUnique("Non-existent Workgoup"));
        assertFalse(TestEJB.workgroupBean.isNameUnique("Test Workgroup"));
    }

    @Test
    public void testGetOwnedWorkgroups() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        assertFalse(TestEJB.workgroupBean.getOwnedWorkgroups(user.getId()).isEmpty());

        try {
            TestEJB.workgroupBean.getOwnedWorkgroups(12345);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetUserWorkgroups() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        assertFalse(TestEJB.workgroupBean.getUserWorkgroups(user.getId()).isEmpty());

        try {
            TestEJB.workgroupBean.getUserWorkgroups(12345);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testSearchForWorkgroup() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        assertTrue(TestEJB.workgroupBean.searchForWorkgroup("Non Existent").isEmpty());
        assertFalse(TestEJB.workgroupBean.searchForWorkgroup("Test Wor").isEmpty());

    }

    @Test
    public void testGetWorkgroup() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Test Description", user.getId(), Collections.singletonList(user.getId()));

        TestEJB.workgroupBean.getWorkgroup(workgroupId);

        try {
            TestEJB.workgroupBean.getWorkgroup(12345);
            fail();
        } catch (NotFoundException e) {}
    }
}