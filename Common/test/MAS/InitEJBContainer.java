package MAS;

import javax.ejb.embeddable.EJBContainer;
import java.util.Properties;

public class InitEJBContainer {

    public static EJBContainer init() {
        MAS.Common.Test.isTesting = true;
        Properties p = new Properties();
        p.put("em", "new://Resource?type=DataSource");
        p.put("em.JdbcDriver", "com.mysql.jdbc.Driver");
        p.put("em.JdbcUrl", "jdbc:mysql://localhost/mastest");
        p.put("em.UserName", "root");
        p.put("em.Password", "admin");
        p.put("javax.persistence.provider", "org.eclipse.persistence.jpa.PersistenceProvider");
        return EJBContainer.createEJBContainer(p);
    }

}
