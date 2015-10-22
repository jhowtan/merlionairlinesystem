package MAS.Bean;

import MAS.Entity.User;
import MAS.Exception.InvalidLoginException;
import MAS.Exception.InvalidResetHashException;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class UserBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreateUser() throws Exception {
        assertNotNull(TestEJB.userBean.createUser("testuser1", "John", "Smith", "johnsmith1@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN")));
    }

    @Test
    public void testForgotPassword() throws Exception {
        TestEJB.userBean.createUser("testuser2", "John", "Smith", "johnsmith2@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertNotNull(TestEJB.userBean.forgotPassword("testuser2"));
        assertNotNull(TestEJB.userBean.forgotPassword("johnsmith2@example.com"));

        try {
            TestEJB.userBean.forgotPassword("nonexistent");
        } catch (NotFoundException e) {}
    }

    @Test
    public void testSetRoles() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser3", "John", "Smith", "johnsmith3@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION");
        long roleId = TestEJB.roleBean.createRole("Test Role", Collections.singletonList(permissionId));
        assertNotNull(TestEJB.userBean.setRoles(userId, Collections.singletonList(roleId)));
    }

    @Test
    public void testIsUsernameUnique() throws Exception {
        TestEJB.userBean.createUser("testuser4", "John", "Smith", "johnsmith4@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertTrue(TestEJB.userBean.isUsernameUnique("nonexistent"));
        assertFalse(TestEJB.userBean.isUsernameUnique("testuser4"));
    }

    @Test
    public void testIsEmailUnique() throws Exception {
        TestEJB.userBean.createUser("testuser5", "John", "Smith", "johnsmith5@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertTrue(TestEJB.userBean.isEmailUnique("nonexistent@example.com"));
        assertFalse(TestEJB.userBean.isEmailUnique("johnsmith5@example.com"));
    }

    @Test
    public void testRemoveUser() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser6", "John", "Smith", "johnsmith6@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Description", userId, Collections.singletonList(userId));

        TestEJB.userBean.removeUser(userId);

        try {
            TestEJB.userBean.removeUser(12345);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testResetPassword() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser6", "John", "Smith", "johnsmith6@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        User user = TestEJB.userBean.getUser(userId);
        TestEJB.userBean.resetPassword(userId, user.getResetHash(), "password123");

        try {
            TestEJB.userBean.resetPassword(userId, "abc", "password123");
            fail();
        } catch (InvalidResetHashException e) {}

        try {
            TestEJB.userBean.resetPassword(12345, "abc", "password123");
            fail();
        } catch (NotFoundException e) {}

    }

    @Test
    public void testChangePassword() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser7", "John", "Smith", "johnsmith7@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        TestEJB.userBean.changePassword(userId, "password123");

        try {
            TestEJB.userBean.changePassword(12345, "password123");
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testLogin() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser8", "John", "Smith", "johnsmith8@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        TestEJB.userBean.changePassword(userId, "password123");

        assertNotNull(TestEJB.userBean.login("testuser8", "password123"));

        try {
            TestEJB.userBean.login("testuser8", "wrongpassword");
            fail();
        } catch (InvalidLoginException e) {}

        TestEJB.userBean.setLocked(userId, true);

        try {
            TestEJB.userBean.login("testuser8", "password123");
            fail();
        } catch (InvalidLoginException e) {}

        TestEJB.userBean.removeUser(userId);

        try {
            TestEJB.userBean.login("testuser8", "password123");
            fail();
        } catch (InvalidLoginException e) {}

        try {
            TestEJB.userBean.login("nonexistent", "password123");
            fail();
        } catch (InvalidLoginException e) {}
    }

    @Test
    public void testGetAllUsers() throws Exception {
        TestEJB.userBean.createUser("testuser9", "John", "Smith", "johnsmith9@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertFalse(TestEJB.userBean.getAllUsers().isEmpty());
    }

    @Test
    public void testIsResetHashValid() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser10", "John", "Smith", "johnsmith10@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        User user = TestEJB.userBean.getUser(userId);
        assertTrue(TestEJB.userBean.isResetHashValid(userId, user.getResetHash()));
        assertFalse(TestEJB.userBean.isResetHashValid(userId, "abc"));
        assertFalse(TestEJB.userBean.isResetHashValid((long) 12345, "abc"));
    }

    @Test
    public void testSetLocked() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser11", "John", "Smith", "johnsmith11@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        TestEJB.userBean.setLocked(userId, true);

        try {
            TestEJB.userBean.setLocked(12345, true);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testUpdateUserInfo() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser12", "John", "Smith", "johnsmith12@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        TestEJB.userBean.updateUserInfo(userId, "69876543");

        try {
            TestEJB.userBean.updateUserInfo(12345, "69876543");
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testAdminUpdateUserInfo() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser13", "John", "Smith", "johnsmith13@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertNotNull(TestEJB.userBean.adminUpdateUserInfo(userId, "John", "Smith", "johnsmith13@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN")));

        try {
            TestEJB.userBean.adminUpdateUserInfo(12345, "John", "Smith", "johnsmith13@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetUser() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser14", "John", "Smith", "johnsmith14@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertNotNull(TestEJB.userBean.getUser(userId));

        try {
            TestEJB.userBean.getUser(12345);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testSearchForUser() throws Exception {
        long userId = TestEJB.userBean.createUser("testuser15", "John", "Smith", "johnsmith15@example.com", "61234567", TestEJB.routeBean.findAirportByCode("SIN"));
        assertTrue(TestEJB.userBean.searchForUser("Nonexistent User").isEmpty());
        assertFalse(TestEJB.userBean.searchForUser("John Smith").isEmpty());
        assertFalse(TestEJB.userBean.searchForUser("John Sm").isEmpty());
    }
}