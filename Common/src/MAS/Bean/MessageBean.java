package MAS.Bean;

import MAS.Entity.Message;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Stateless(name = "MessageEJB")
@LocalBean
public class MessageBean {
    @PersistenceContext
    private EntityManager em;

    public MessageBean() {
    }

    public long createMessage(long senderUserId, String subject, String body, List<Long> userRecipientsId, List<Long> workgroupRecipientsId) throws NotFoundException {
        User sender = em.find(User.class, senderUserId);
        if (sender == null) throw new NotFoundException();

        HashSet<User> recipients = new HashSet<>();
        User userRecipient;
        for (long userRecipientId : userRecipientsId) {
            userRecipient = em.find(User.class, userRecipientId);
            if (userRecipient == null) throw new NotFoundException();
            if (userRecipient.isDeleted()) throw new NotFoundException();
            recipients.add(userRecipient);
        }
        Workgroup workgroupRecipient;
        for (long workgroupRecipientId : workgroupRecipientsId) {
            workgroupRecipient = em.find(Workgroup.class, workgroupRecipientId);
            if (workgroupRecipient == null) throw new NotFoundException();
            recipients.addAll(workgroupRecipient.getUsers());
        }

        Message message = new Message();
        message.setSender(sender);
        message.setSubject(subject);
        message.setBody(body);
        message.setSentTime(new Date());
        message.setRecipients(new ArrayList<User>(recipients));
        em.persist(message);
        em.flush();
        return message.getId();
    }

    public List<Message> getUserMessages(long userId) throws NotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) throw new NotFoundException();
        List<Message> messages = user.getMessages();
        Collections.reverse(messages);
        return messages;
    }

}
