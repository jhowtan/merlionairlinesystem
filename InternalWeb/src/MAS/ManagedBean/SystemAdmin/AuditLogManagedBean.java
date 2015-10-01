package MAS.ManagedBean.SystemAdmin;

import MAS.Bean.AuditLogBean;
import MAS.Bean.UserBean;
import MAS.Entity.AuditLog;
import MAS.Entity.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
public class AuditLogManagedBean {
    @EJB
    private AuditLogBean auditLogBean;
    @EJB
    private UserBean userBean;

    private List<AuditLog> auditLogs;
    private User user;

    @PostConstruct
    public void init() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            long userId = Long.parseLong(params.get("userId"));
            user = userBean.getUser(userId);
            auditLogs = auditLogBean.getAuditLogForUser(userId);
        } catch (Exception e) {
            user = null;
            auditLogs = auditLogBean.getAllAuditLogs();
        }
    }

    public void exportToCSV() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        StringBuilder sb = new StringBuilder();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(sb, csvFormat);
            csvPrinter.printRecord(new String[] {"Timestamp", "Username", "Category", "Log Entry"});
            ArrayList<String> record;
            for (AuditLog auditLog : auditLogs) {
                record = new ArrayList<>();
                record.add(auditLog.getTimestamp().toString());
                record.add(auditLog.getUser().getUsername());
                record.add(auditLog.getCategory());
                record.add(auditLog.getDescription());
                csvPrinter.printRecord(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=export.csv");
        response.setContentLength(sb.length());
        response.setContentType("text/csv");
        try {
            response.getOutputStream().write(sb.toString().getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            ctx.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
