package MAS.WebService;
import MAS.Bean.CustomerLogBean;
import MAS.Bean.FFPBean;
import MAS.Exception.NotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.Endpoint;

@WebService()
public class FFPAlliance {

    FFPBean ffpBean;
    CustomerLogBean customerLogBean;

    public FFPAlliance() {
        try {
            ffpBean = (FFPBean) new InitialContext().lookup("java:global/WebService_war_exploded/FFPEJB");
            customerLogBean = (CustomerLogBean) new InitialContext().lookup("java:global/WebService_war_exploded/CustomerLogEJB");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public boolean awardMiles(@WebParam(name="ffpNumber") long ffpNumber, @WebParam (name="miles") int miles) {
        try {
            ffpBean.creditMiles(ffpNumber, miles);
            customerLogBean.createCustomerLog(ffpNumber, miles + " Miles earned from partner airline", "partner_miles");
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public static void main(String[] argv) {
        Object implementor = new FFPAlliance ();
        String address = "http://localhost:9000/FFPAlliance";
        Endpoint.publish(address, implementor);
    }
}
