//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.10.03 at 12:42:49 PM IST 
//


package com.fb.platform.sap.idoc.generated.delvry01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IMP: This file was autogenerated by Jaxb from the src/test/resources/delvry01.xsd. Then the generated classes 
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
 *         &lt;element ref="{}EDI_DC40"/>
 *         &lt;element ref="{}E1EDL20"/>
 *       &lt;/sequence>
 *       &lt;attribute name="BEGIN" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "edidc40",
    "e1EDL20"
})
@XmlRootElement(name = "IDOC")
public class IDOC {

    @XmlElement(name = "EDI_DC40", required = true)
    protected EDIDC40 edidc40;
    @XmlElement(name = "E1EDL20", required = true)
    protected E1EDL20 e1EDL20;
    @XmlAttribute(name = "BEGIN", required = true)
    protected int begin;

    /**
     * Gets the value of the edidc40 property.
     * 
     * @return
     *     possible object is
     *     {@link EDIDC40 }
     *     
     */
    public EDIDC40 getEDIDC40() {
        return edidc40;
    }

    /**
     * Sets the value of the edidc40 property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDIDC40 }
     *     
     */
    public void setEDIDC40(EDIDC40 value) {
        this.edidc40 = value;
    }

    /**
     * Gets the value of the e1EDL20 property.
     * 
     * @return
     *     possible object is
     *     {@link E1EDL20 }
     *     
     */
    public E1EDL20 getE1EDL20() {
        return e1EDL20;
    }

    /**
     * Sets the value of the e1EDL20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link E1EDL20 }
     *     
     */
    public void setE1EDL20(E1EDL20 value) {
        this.e1EDL20 = value;
    }

    /**
     * Gets the value of the begin property.
     * 
     */
    public int getBEGIN() {
        return begin;
    }

    /**
     * Sets the value of the begin property.
     * 
     */
    public void setBEGIN(int value) {
        this.begin = value;
    }

}
