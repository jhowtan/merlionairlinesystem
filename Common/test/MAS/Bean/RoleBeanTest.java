package MAS.Bean;

import MAS.Entity.Permission;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RoleBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testGetRole() throws Exception {
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION_1");
        long roleId = TestEJB.roleBean.createRole("Test Role 1", Arrays.asList(permissionId));

        assertEquals(TestEJB.roleBean.getRole(roleId).getName(), "Test Role 1");
        try {
            TestEJB.roleBean.getRole(12345);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testEditRole() throws Exception {
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION_2");
        long roleId = TestEJB.roleBean.createRole("Test Role 2", Arrays.asList(permissionId));

        assertEquals(roleId, TestEJB.roleBean.editRole(roleId, "Test Role 2"));
    }

    @Test
    public void testSetPermissions() throws Exception {
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION_3");
        long roleId = TestEJB.roleBean.createRole("Test Role 3", Arrays.asList(permissionId));

        try {
            TestEJB.roleBean.setPermissions(12345, Arrays.asList(permissionId));
            fail();
        } catch (NotFoundException e) {}
        TestEJB.roleBean.setPermissions(roleId, Arrays.asList(permissionId));
    }

    @Test
    public void testIsNameUnique() throws Exception {
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION_4");
        long roleId = TestEJB.roleBean.createRole("Existent Role", Arrays.asList(permissionId));

        assertTrue(TestEJB.roleBean.isNameUnique("Non-existent Role"));
        assertFalse(TestEJB.roleBean.isNameUnique("Existent Role"));
    }

    @Test
    public void testRemoveRole() throws Exception {
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION_5");
        long roleId = TestEJB.roleBean.createRole("Existent Role", Arrays.asList(permissionId));
        TestEJB.roleBean.removeRole(roleId);

        try {
            TestEJB.roleBean.removeRole(roleId);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetAllPermissions() throws Exception {
        assertFalse(TestEJB.roleBean.getAllPermissions().isEmpty());
    }

    @Test
    public void testGetAllRoles() throws Exception {
        assertFalse(TestEJB.roleBean.getAllRoles().isEmpty());
    }

    @Test
    public void testFindPermission() throws Exception {
        long permissionId = TestEJB.roleBean.createPermission("TEST_PERMISSION_6");
        assertEquals("TEST_PERMISSION_6", TestEJB.roleBean.findPermission("TEST_PERMISSION_6").getName());

        try {
            TestEJB.roleBean.findPermission("NONEXISTENT_PERMISSION");
            fail();
        } catch (NotFoundException e) {}
    }
}