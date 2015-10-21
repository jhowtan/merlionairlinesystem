package MAS.Bean;

import MAS.Entity.User;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class MessageBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testCreateMessage() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long workgroupId = TestEJB.workgroupBean.createWorkgroup("Test Workgroup", "Description", user.getId(), Collections.singletonList(user.getId()));

        assertNotNull(TestEJB.messageBean.createMessage(user.getId(), "Subject", "Message", Collections.singletonList(user.getId()), Collections.singletonList(workgroupId)));

        try {
            TestEJB.messageBean.createMessage(12345, "Subject", "Message", Collections.singletonList(user.getId()), Collections.emptyList());
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.messageBean.createMessage(user.getId(), "Subject", "Message", Collections.singletonList(Long.parseLong("12345")), Collections.emptyList());
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.messageBean.createMessage(user.getId(), "Subject", "Message", Collections.emptyList(), Collections.singletonList(Long.parseLong("12345")));
            fail();
        } catch (NotFoundException e) {}

    }

    @Test
    public void testGetUserMessages() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        TestEJB.messageBean.createMessage(user.getId(), "Subject", "Message", Collections.singletonList(user.getId()), Collections.emptyList());
        assertFalse(TestEJB.messageBean.getUserMessages(user.getId()).isEmpty());
    }

    @Test
    public void testGetMessage() throws Exception {
        User user = TestEJB.userBean.getAllUsers().get(0);
        long messageId = TestEJB.messageBean.createMessage(user.getId(), "Subject", "Message", Collections.singletonList(user.getId()), Collections.emptyList());
        assertNotNull(TestEJB.messageBean.getMessage(messageId));

        try {
            TestEJB.messageBean.getMessage(12345);
        } catch (NotFoundException e) {}

    }
}