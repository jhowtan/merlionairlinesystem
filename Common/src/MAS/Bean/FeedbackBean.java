package MAS.Bean;

import MAS.Entity.Feedback;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "FeedbackEJB")
@LocalBean
public class FeedbackBean {
    public FeedbackBean() {
    }

    @PersistenceContext
    EntityManager em;

    public Feedback createFeedback(Feedback feedback) {
        em.persist(feedback);
        em.flush();
        return feedback;
    }
}
