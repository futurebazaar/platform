
package com.fb.platform.sap.idoc.generated.zatgDeld;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IMP: This file was autogenerated by Jaxb from the src/test/resources/zatgdeld.xsd. Then the generated classes 
 * were manually copied in this package as jaxb generated classes dont have a package.
 * If there is any change in the xml format from sap, regenerate the classes
 * from xsd and replace these generated files.
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}IDOC"/>
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
    "idoc"
})
@XmlRootElement(name = "ZATGDELD")
public class ZATGDELD {

    @XmlElement(name = "IDOC", required = true)
    protected IDOC idoc;

    /**
     * Gets the value of the idoc property.
     * 
     * @return
     *     possible object is
     *     {@link IDOC }
     *     
     */
    public IDOC getIDOC() {
        return idoc;
    }

    /**
     * Sets the value of the idoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDOC }
     *     
     */
    public void setIDOC(IDOC value) {
        this.idoc = value;
    }

}
