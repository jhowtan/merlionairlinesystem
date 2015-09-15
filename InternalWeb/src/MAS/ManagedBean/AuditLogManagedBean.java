package MAS.ManagedBean;

import MAS.Bean.AuditLogBean;
import MAS.Entity.AuditLog;
import MAS.Exception.NotFoundException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean
public class AuditLogManagedBean {
    @EJB
    private AuditLogBean auditLogBean;

    public List<AuditLog> getAllAuditLogs() {
        return auditLogBean.getAllAuditLogs();
    }

}
