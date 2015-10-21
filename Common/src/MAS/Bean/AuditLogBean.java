package MAS.Bean;

import MAS.Entity.AuditLog;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless(name = "AuditLogEJB")
@LocalBean
public class AuditLogBean {
    @PersistenceContext
    private EntityManager em;

    public AuditLogBean() {
    }

    public long createAuditLog(long userId, String description, String category) throws NotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) throw new NotFoundException();

        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setDescription(description);
        auditLog.setCategory(category);
        auditLog.setTimestamp(new Date());

        em.persist(auditLog);
        em.flush();

        return auditLog.getId();
    }

    public List<AuditLog> getAllAuditLogs() {
        return em.createQuery("SELECT a FROM AuditLog a", AuditLog.class).getResultList();
    }

    public List<AuditLog> getAuditLogForUser(long userId) throws NotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) throw new NotFoundException();
        return em.createQuery("SELECT a FROM AuditLog a WHERE a.user = :user").setParameter("user", user).getResultList();
    }

}
