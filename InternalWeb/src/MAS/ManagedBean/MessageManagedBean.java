package MAS.ManagedBean;

import MAS.Bean.MessageBean;
import MAS.Entity.Message;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class MessageManagedBean {
    @EJB
    private MessageBean messageBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    public List<Message> getMessages() {
        try {
            return messageBean.getUserMessages(authManagedBean.getUserId());
        } catch (NotFoundException e) {
            return new ArrayList<Message>();
        }
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }
}
