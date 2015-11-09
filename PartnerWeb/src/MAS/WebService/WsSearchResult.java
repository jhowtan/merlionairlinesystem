
package MAS.WebService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsSearchResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsSearchResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flight_results" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="flightResults" type="{http://WebService.MAS/}wsFlightResult" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsSearchResult", propOrder = {
    "flightResults"
})
public class WsSearchResult {

    @XmlElement(name = "flight_results")
    protected WsSearchResult.FlightResults flightResults;

    /**
     * Gets the value of the flightResults property.
     * 
     * @return
     *     possible object is
     *     {@link WsSearchResult.FlightResults }
     *     
     */
    public WsSearchResult.FlightResults getFlightResults() {
        return flightResults;
    }

    /**
     * Sets the value of the flightResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link WsSearchResult.FlightResults }
     *     
     */
    public void setFlightResults(WsSearchResult.FlightResults value) {
        this.flightResults = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="flightResults" type="{http://WebService.MAS/}wsFlightResult" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "flightResults"
    })
    public static class FlightResults {

        @XmlElement(nillable = true)
        protected List<WsFlightResult> flightResults;

        /**
         * Gets the value of the flightResults property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the flightResults property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFlightResults().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link WsFlightResult }
         * 
         * 
         */
        public List<WsFlightResult> getFlightResults() {
            if (flightResults == null) {
                flightResults = new ArrayList<WsFlightResult>();
            }
            return this.flightResults;
        }

    }

}
