package MAS.WebService;
import MAS.Bean.LogEntryBean;
import MAS.Entity.LogEntry;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.xml.ws.Endpoint;
import java.util.Date;

@WebService()
public class PartnerMiles {
    LogEntryBean logEntryBean;

    public PartnerMiles() {
        try {
            logEntryBean = (LogEntryBean) new InitialContext().lookup("java:global/PartnerWeb_war_exploded/LogEntryEJB");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public boolean awardMiles(@WebParam(name="ffpNumber") String ffpNumber, @WebParam (name="miles") int miles) {
        logEntryBean.create(miles + " miles credited from Merlion Airlines to " + ffpNumber);
        return true;
    }

    public static void main(String[] argv) {
        Object implementor = new PartnerMiles ();
        String address = "http://localhost:9000/PartnerMiles";
        Endpoint.publish(address, implementor);
    }
}
