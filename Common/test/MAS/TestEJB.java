package MAS;

import MAS.Bean.*;

import javax.ejb.embeddable.EJBContainer;
import java.util.Properties;

public class TestEJB {

    public static EJBContainer ejbContainer;
    public static AttributesBean attributesBean;
    public static AuditLogBean auditLogBean;
    public static UserBean userBean;
    public static RoleBean roleBean;
    public static WorkgroupBean workgroupBean;
    public static CustomerBean customerBean;
    public static CustomerLogBean customerLogBean;
    public static FFPBean ffpBean;

    public static EJBContainer init() throws Exception {
        MAS.Common.Test.isTesting = true;
        if (ejbContainer == null) {
            Properties p = new Properties();
            p.put("em", "new://Resource?type=DataSource");
            p.put("em.JdbcDriver", "com.mysql.jdbc.Driver");
            p.put("em.JdbcUrl", "jdbc:mysql://localhost/mastest");
            p.put("em.UserName", "root");
            p.put("em.Password", "admin");
            p.put("javax.persistence.provider", "org.eclipse.persistence.jpa.PersistenceProvider");
            ejbContainer = EJBContainer.createEJBContainer(p);

            attributesBean = (AttributesBean) ejbContainer.getContext().lookup("java:global/Common/AttributesEJB");
            auditLogBean = (AuditLogBean) ejbContainer.getContext().lookup("java:global/Common/AuditLogEJB");
            userBean = (UserBean) ejbContainer.getContext().lookup("java:global/Common/UserEJB");
            roleBean = (RoleBean) ejbContainer.getContext().lookup("java:global/Common/RoleEJB");
            workgroupBean = (WorkgroupBean) ejbContainer.getContext().lookup("java:global/Common/WorkgroupEJB");
            customerBean = (CustomerBean) ejbContainer.getContext().lookup("java:global/Common/CustomerEJB");
            customerLogBean = (CustomerLogBean) ejbContainer.getContext().lookup("java:global/Common/CustomerLogEJB");
            ffpBean = (FFPBean) ejbContainer.getContext().lookup("java:global/Common/FFPEJB");
        }
        return ejbContainer;
    }

}
