package MAS.Bean;

import MAS.Entity.Certification;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "CrewCertificationEJB")
public class CrewCertificationBean {
    @PersistenceContext
    EntityManager em;

    public CrewCertificationBean() {
    }

    public Certification createCrewCertification(Certification certification) {
        em.persist(certification);
        em.flush();
        return certification;
    }

    public List<Certification> getCrewCertifications(long userId) throws NotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) throw new NotFoundException();
        return em.createQuery("SELECT c FROM Certification c WHERE c.owner = :owner", Certification.class).setParameter("owner", user).getResultList();
    }
}
