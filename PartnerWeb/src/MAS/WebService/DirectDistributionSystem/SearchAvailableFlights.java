
package MAS.WebService.DirectDistributionSystem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for searchAvailableFlights complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="searchAvailableFlights">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="origin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="passengerCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="travelClass" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="travelDuration" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchAvailableFlights", propOrder = {
    "origin",
    "destination",
    "date",
    "passengerCount",
    "travelClass",
    "travelDuration"
})
public class SearchAvailableFlights {

    protected String origin;
    protected String destination;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected int passengerCount;
    protected int travelClass;
    protected int travelDuration;

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigin(String value) {
        this.origin = value;
    }

    /**
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestination(String value) {
        this.destination = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the passengerCount property.
     * 
     */
    public int getPassengerCount() {
        return passengerCount;
    }

    /**
     * Sets the value of the passengerCount property.
     * 
     */
    public void setPassengerCount(int value) {
        this.passengerCount = value;
    }

    /**
     * Gets the value of the travelClass property.
     * 
     */
    public int getTravelClass() {
        return travelClass;
    }

    /**
     * Sets the value of the travelClass property.
     * 
     */
    public void setTravelClass(int value) {
        this.travelClass = value;
    }

    /**
     * Gets the value of the travelDuration property.
     * 
     */
    public int getTravelDuration() {
        return travelDuration;
    }

    /**
     * Sets the value of the travelDuration property.
     * 
     */
    public void setTravelDuration(int value) {
        this.travelDuration = value;
    }

}
