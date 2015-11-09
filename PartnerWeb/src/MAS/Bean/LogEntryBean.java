package MAS.Bean;

import MAS.Entity.LogEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless(name = "LogEntryEJB")
public class LogEntryBean {
    public LogEntryBean() {
    }

    @PersistenceContext
    EntityManager em;

    public void create(String description) {
        LogEntry logEntry = new LogEntry();
        logEntry.setDescription(description);
        logEntry.setDate(new Date());
        em.persist(logEntry);
        em.flush();
    }
}
