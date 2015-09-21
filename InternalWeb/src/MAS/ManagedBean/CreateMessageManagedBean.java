package MAS.ManagedBean;

import MAS.Bean.RoleBean;
import MAS.Bean.UserBean;
import MAS.Bean.WorkgroupBean;
import MAS.Entity.Role;
import MAS.Entity.User;
import MAS.Entity.Workgroup;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
public class CreateMessageManagedBean {
    @EJB
    private UserBean userBean;
    @EJB
    private WorkgroupBean workgroupBean;

    private ArrayList recipients;
    private String subject;
    private String body;

    public CreateMessageManagedBean() {
        recipients = new ArrayList(Arrays.asList(new ArrayList<User>(), new ArrayList<Workgroup>()));
    }

    public void createMessage() {

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
}
