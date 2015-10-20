package MAS.Bean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;

import java.util.Properties;

import static org.junit.Assert.*;

public class AttributesBeanTest {

    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        p.put("em", "new://Resource?type=DataSource");
        p.put("em.JdbcDriver", "com.mysql.jdbc.Driver");
        p.put("em.JdbcUrl", "jdbc:mysql://localhost/mastest");
        p.put("em.UserName", "root");
        p.put("em.Password", "admin");
        p.put("em.Provider", "org.eclipse.persistence.jpa.PersistenceProvider");
//        scmaccess-unit.eclipselink.jdbc.batch-writing=JDBC
//        scmaccess-unit.eclipselink.target-database=Auto
//        scmaccess-unit.eclipselink.ddl-generation=drop-and-create-tables
//        scmaccess-unit.eclipselink.ddl-generation.output-mode=database
        EJBContainer.createEJBContainer(p);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetStringAttribute() throws Exception {

    }

    @Test
    public void testSetStringAttribute() throws Exception {

    }
}