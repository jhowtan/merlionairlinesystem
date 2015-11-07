package MAS.WebService;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService()
public class DirectDistributionSystem {

    @WebMethod
    public String sayHelloWorldFrom(String from) {
        String result = "Hello, world, from " + from;
        System.out.println(result);
        return result;
    }
    public static void main(String[] argv) {
        Object implementor = new DirectDistributionSystem ();
        String address = "http://localhost:9000/DirectDistributionSystem";
        Endpoint.publish(address, implementor);
    }
}
