package MAS.Bean;

import MAS.Entity.Feedback;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public List<Feedback> getAllFeedback() {
        return em.createQuery("SELECT f FROM Feedback f", Feedback.class).getResultList();
    }

    public Feedback getFeedback(long id) throws NotFoundException {
        Feedback feedback = em.find(Feedback.class, id);
        if (feedback == null) throw new NotFoundException();
        return feedback;
    }

    public void setResponded(long id, boolean responded) throws NotFoundException {
        Feedback feedback = em.find(Feedback.class, id);
        if (feedback == null) throw new NotFoundException();
        feedback.setResponded(responded);
        em.persist(feedback);
        em.flush();
    }
}
