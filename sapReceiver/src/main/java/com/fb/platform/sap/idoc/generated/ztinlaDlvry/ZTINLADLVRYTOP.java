//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.12 at 04:09:06 PM IST 
//


package com.fb.platform.sap.idoc.generated.ztinlaDlvry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IMP: This file was autogenerated by Jaxb from the src/test/resources/ztinla_dlvry.xsd. Then the generated classes 
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
@XmlRootElement(name = "ZTINLA_DLVRY_TOP")
public class ZTINLADLVRYTOP {

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
