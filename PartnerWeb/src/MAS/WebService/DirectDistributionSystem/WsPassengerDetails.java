
package MAS.WebService.DirectDistributionSystem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsPassengerDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsPassengerDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ffpProgram" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ffpNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsPassengerDetails", propOrder = {
    "firstName",
    "lastName",
    "ffpProgram",
    "ffpNumber"
})
public class WsPassengerDetails {

    protected String firstName;
    protected String lastName;
    protected String ffpProgram;
    protected String ffpNumber;

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the ffpProgram property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFfpProgram() {
        return ffpProgram;
    }

    /**
     * Sets the value of the ffpProgram property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFfpProgram(String value) {
        this.ffpProgram = value;
    }

    /**
     * Gets the value of the ffpNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFfpNumber() {
        return ffpNumber;
    }

    /**
     * Sets the value of the ffpNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFfpNumber(String value) {
        this.ffpNumber = value;
    }

}
