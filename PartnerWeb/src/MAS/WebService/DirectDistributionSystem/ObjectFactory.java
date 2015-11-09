
package MAS.WebService.DirectDistributionSystem;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the MAS.WebService.DirectDistributionSystem package. 
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

    private final static QName _BookResponse_QNAME = new QName("http://WebService.MAS/", "bookResponse");
    private final static QName _Book_QNAME = new QName("http://WebService.MAS/", "book");
    private final static QName _SearchAvailableFlightsResponse_QNAME = new QName("http://WebService.MAS/", "searchAvailableFlightsResponse");
    private final static QName _WsBookingClass_QNAME = new QName("http://WebService.MAS/", "wsBookingClass");
    private final static QName _SearchAvailableFlights_QNAME = new QName("http://WebService.MAS/", "searchAvailableFlights");
    private final static QName _WsFlightResult_QNAME = new QName("http://WebService.MAS/", "wsFlightResult");
    private final static QName _BookingException_QNAME = new QName("http://WebService.MAS/", "BookingException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: MAS.WebService.DirectDistributionSystem
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchAvailableFlightsResponse }
     * 
     */
    public SearchAvailableFlightsResponse createSearchAvailableFlightsResponse() {
        return new SearchAvailableFlightsResponse();
    }

    /**
     * Create an instance of {@link WsBookingClass }
     * 
     */
    public WsBookingClass createWsBookingClass() {
        return new WsBookingClass();
    }

    /**
     * Create an instance of {@link WsFlightResult }
     * 
     */
    public WsFlightResult createWsFlightResult() {
        return new WsFlightResult();
    }

    /**
     * Create an instance of {@link SearchAvailableFlights }
     * 
     */
    public SearchAvailableFlights createSearchAvailableFlights() {
        return new SearchAvailableFlights();
    }

    /**
     * Create an instance of {@link BookResponse }
     * 
     */
    public BookResponse createBookResponse() {
        return new BookResponse();
    }

    /**
     * Create an instance of {@link Book }
     * 
     */
    public Book createBook() {
        return new Book();
    }

    /**
     * Create an instance of {@link BookingException }
     * 
     */
    public BookingException createBookingException() {
        return new BookingException();
    }

    /**
     * Create an instance of {@link WsPassengerDetails }
     * 
     */
    public WsPassengerDetails createWsPassengerDetails() {
        return new WsPassengerDetails();
    }

    /**
     * Create an instance of {@link WsFlightResultArray }
     * 
     */
    public WsFlightResultArray createWsFlightResultArray() {
        return new WsFlightResultArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "bookResponse")
    public JAXBElement<BookResponse> createBookResponse(BookResponse value) {
        return new JAXBElement<BookResponse>(_BookResponse_QNAME, BookResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Book }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "book")
    public JAXBElement<Book> createBook(Book value) {
        return new JAXBElement<Book>(_Book_QNAME, Book.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchAvailableFlightsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "searchAvailableFlightsResponse")
    public JAXBElement<SearchAvailableFlightsResponse> createSearchAvailableFlightsResponse(SearchAvailableFlightsResponse value) {
        return new JAXBElement<SearchAvailableFlightsResponse>(_SearchAvailableFlightsResponse_QNAME, SearchAvailableFlightsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsBookingClass }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "wsBookingClass")
    public JAXBElement<WsBookingClass> createWsBookingClass(WsBookingClass value) {
        return new JAXBElement<WsBookingClass>(_WsBookingClass_QNAME, WsBookingClass.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchAvailableFlights }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "searchAvailableFlights")
    public JAXBElement<SearchAvailableFlights> createSearchAvailableFlights(SearchAvailableFlights value) {
        return new JAXBElement<SearchAvailableFlights>(_SearchAvailableFlights_QNAME, SearchAvailableFlights.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsFlightResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "wsFlightResult")
    public JAXBElement<WsFlightResult> createWsFlightResult(WsFlightResult value) {
        return new JAXBElement<WsFlightResult>(_WsFlightResult_QNAME, WsFlightResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.MAS/", name = "BookingException")
    public JAXBElement<BookingException> createBookingException(BookingException value) {
        return new JAXBElement<BookingException>(_BookingException_QNAME, BookingException.class, null, value);
    }

}
