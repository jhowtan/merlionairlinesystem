
package MAS.WebService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the MAS.WebService package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AwardMilesResponse_QNAME = new QName("http://WebService.MAS/", "awardMilesResponse");
    private final static QName _AwardMiles_QNAME = new QName("http://WebService.MAS/", "awardMiles");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: MAS.WebService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AwardMilesResponse }
     * 
     */
    public AwardMilesResponse createAwardMilesResponse() {
        return new AwardMilesResponse();
    }

    /**
     * Create an instance of {@link AwardMiles }
     * 
     */
    public AwardMiles createAwardMiles() {
        return new AwardMiles();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AwardMilesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "awardMilesResponse")
    public JAXBElement<AwardMilesResponse> createAwardMilesResponse(AwardMilesResponse value) {
        return new JAXBElement<AwardMilesResponse>(_AwardMilesResponse_QNAME, AwardMilesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AwardMiles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "awardMiles")
    public JAXBElement<AwardMiles> createAwardMiles(AwardMiles value) {
        return new JAXBElement<AwardMiles>(_AwardMiles_QNAME, AwardMiles.class, null, value);
    }

}
