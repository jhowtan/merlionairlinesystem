package MAS.ManagedBean.CustomerRelations;

import MAS.Bean.CustomerBean;
import MAS.Bean.FeedbackBean;
import MAS.Entity.Customer;
import MAS.Entity.Feedback;
import MAS.Exception.NotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class CustomerFeedbackManagedBean implements Serializable {
    @EJB
    FeedbackBean feedbackBean;

    private Feedback feedback;

    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            long id = Long.parseLong(params.get("id"));
            feedback = feedbackBean.getFeedback(id);
        } catch (Exception e) {
            feedback = null;
        }
    }

    public List<Feedback> getFeedbacks() {
        return feedbackBean.getAllFeedback();
    }

    public void setResponded(boolean responded) throws NotFoundException {
        feedback.setResponded(responded);
        feedbackBean.setResponded(feedback.getId(), responded);
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
