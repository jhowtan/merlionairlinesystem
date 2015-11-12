package MAS.ManagedBean;

import MAS.Bean.FeedbackBean;
import MAS.Entity.Feedback;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
public class FeedbackManagedBean {
    @EJB
    FeedbackBean feedbackBean;

    @ManagedProperty(value="#{authManagedBean}")
    private AuthManagedBean authManagedBean;

    private String email;
    private String subject;
    private String message;
    private boolean submitted;

    @PostConstruct
    public void init() {
        if (authManagedBean.isAuthenticated()) {
            email = authManagedBean.retrieveCustomer().getEmail();
        }
    }

    public void send() {
        Feedback feedback = new Feedback();
        feedback.setEmail(email);
        feedback.setSubject(subject);
        feedback.setBody(message);
        if (authManagedBean.isAuthenticated()) {
            feedback.setCustomer(authManagedBean.retrieveCustomer());
        }
        feedbackBean.createFeedback(feedback);
        submitted = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthManagedBean(AuthManagedBean authManagedBean) {
        this.authManagedBean = authManagedBean;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
}
