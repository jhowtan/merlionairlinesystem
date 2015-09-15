package MAS.Bean;

import MAS.Entity.AuditLog;
import MAS.Entity.Permission;
import MAS.Entity.Role;
import MAS.Entity.User;
import MAS.Exception.NotFoundException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

}
