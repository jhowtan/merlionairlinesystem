
package MAS.WebService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PartnerMiles", targetNamespace = "http://WebService.MAS/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PartnerMiles {


    /**
     * 
     * @param ffpNumber
     * @param miles
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "awardMiles", targetNamespace = "http://WebService.MAS/", className = "MAS.WebService.AwardMiles")
    @ResponseWrapper(localName = "awardMilesResponse", targetNamespace = "http://WebService.MAS/", className = "MAS.WebService.AwardMilesResponse")
    @Action(input = "http://WebService.MAS/PartnerMiles/awardMilesRequest", output = "http://WebService.MAS/PartnerMiles/awardMilesResponse")
    public boolean awardMiles(
        @WebParam(name = "ffpNumber", targetNamespace = "")
        String ffpNumber,
        @WebParam(name = "miles", targetNamespace = "")
        int miles);

}