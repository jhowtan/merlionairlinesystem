
package MAS.WebService.FFPAlliance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for awardMiles complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="awardMiles">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ffpNumber" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="miles" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "awardMiles", propOrder = {
    "ffpNumber",
    "miles"
})
public class AwardMiles {

    protected long ffpNumber;
    protected int miles;

    /**
     * Gets the value of the ffpNumber property.
     * 
     */
    public long getFfpNumber() {
        return ffpNumber;
    }

    /**
     * Sets the value of the ffpNumber property.
     * 
     */
    public void setFfpNumber(long value) {
        this.ffpNumber = value;
    }

    /**
     * Gets the value of the miles property.
     * 
     */
    public int getMiles() {
        return miles;
    }

    /**
     * Sets the value of the miles property.
     * 
     */
    public void setMiles(int value) {
        this.miles = value;
    }

}
