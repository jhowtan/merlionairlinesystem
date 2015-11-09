package MAS.ManagedBean;

import MAS.Entity.LogEntry;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ManagedBean
public class IncomingAllianceMilesManagedBean {

    @PersistenceContext
    EntityManager em;

    public List<LogEntry> getIncoming() {
        return em.createQuery("SELECT l FROM LogEntry l", LogEntry.class).getResultList();
    }

}
