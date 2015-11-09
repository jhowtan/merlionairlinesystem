package MAS.Bean;

import MAS.Entity.FlightDeferment;
import MAS.Entity.FlightRoster;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless(name = "FlightDefermentEJB")
@LocalBean
public class FlightDefermentBean {
    @PersistenceContext
    EntityManager em;

    public FlightDefermentBean() {
    }

    public long createFlightDeferment(long deffererId, long flightRosterId, String reason) throws NotFoundException {
        User deferrer = em.find(User.class, deffererId);
        FlightRoster flightRoster = em.find(FlightRoster.class, flightRosterId);
        if (deferrer == null || flightRoster == null) throw new NotFoundException();
        FlightDeferment flightDeferment = new FlightDeferment();
        flightDeferment.setCreateDate(new Date());
        flightDeferment.setStatus(0);
        flightDeferment.setDeferrer(deferrer);
        flightDeferment.setFlightRoster(flightRoster);
        flightDeferment.setReason(reason);
        em.persist(flightDeferment);
        em.flush();
        return flightDeferment.getId();
    }

    public FlightDeferment getFlightDeferment(long id) throws NotFoundException {
        FlightDeferment flightDeferment = em.find(FlightDeferment.class, id);
        if (flightDeferment == null) throw new NotFoundException();
        return flightDeferment;
    }

    public void removeFlightDeferment(long id) throws NotFoundException {
        FlightDeferment flightDeferment = em.find(FlightDeferment.class, id);
        if (flightDeferment == null) throw new NotFoundException();
        em.remove(flightDeferment);
    }

    public List<FlightDeferment> getUnresolvedDeferments() {
        return em.createQuery("SELECT fd from FlightDeferment fd WHERE fd.status = 0", FlightDeferment.class).getResultList();
    }

    public void setReplacement(long id, long replacementId) throws NotFoundException {
        FlightDeferment flightDeferment = em.find(FlightDeferment.class, id);
        User replacement = em.find(User.class, replacementId);
        if (flightDeferment == null || replacement == null) throw new NotFoundException();
        flightDeferment.setReplacement(replacement);
        em.persist(flightDeferment);
    }

    public void changeDefermentStatus(long id, int status) throws NotFoundException {
        FlightDeferment flightDeferment = em.find(FlightDeferment.class, id);
        if (flightDeferment == null) throw new NotFoundException();
        flightDeferment.setStatus(status);
        em.persist(flightDeferment);
    }
}
