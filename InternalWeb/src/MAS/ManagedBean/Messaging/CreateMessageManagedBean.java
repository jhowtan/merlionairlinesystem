package MAS.ManagedBean.Messaging;

import MAS.Bean.MessageBean;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;
import MAS.ManagedBean.Auth.AuthManagedBean;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;

@ManagedBean
public class CreateMessageManagedBean {
    @EJB
    private MessageBean messageBean;
    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private ArrayList recipients;
    private String subject;
    private String body;

    public CreateMessageManagedBean() {
        recipients = new ArrayList(Arrays.asList(new ArrayList<User>(), new ArrayList<Workgroup>()));
    }

    public void createMessage() {
        ArrayList<User> userRecipients = (ArrayList<User>) recipients.get(0);
        ArrayList<Workgroup> workgroupRecipients = (ArrayList<Workgroup>) recipients.get(1);

        ArrayList<Long> userRecipientIds = new ArrayList<>();
        for (User userRecipient : userRecipients) {
            userRecipientIds.add(userRecipient.getId());
        }

        ArrayList<Long> workgroupRecipientIds = new ArrayList<>();
        for (Workgroup workgroupRecipient : workgroupRecipients) {
            workgroupRecipientIds.add(workgroupRecipient.getId());
        }

        try {
            messageBean.createMessage(authManagedBean.getUserId(), subject, body, userRecipientIds, workgroupRecipientIds);

            authManagedBean.createAuditLog("Sent a message.", "send_message");

            FacesMessage m = new FacesMessage("Message successfully sent.");
            m.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage("status", m);

            recipients = new ArrayList(Arrays.asList(new ArrayList<User>(), new ArrayList<Workgroup>()));
            subject = null;
            body = null;
        } catch (NotFoundException e) {
        }
    }

    public ArrayList getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
