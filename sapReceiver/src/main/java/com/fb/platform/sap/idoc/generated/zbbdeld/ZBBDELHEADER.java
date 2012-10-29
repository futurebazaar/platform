//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.10.23 at 01:34:26 PM IST 
//


package com.fb.platform.sap.idoc.generated.zbbdeld;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IMP: This file was autogenerated by Jaxb from the src/test/resources/zbbdeld.xsd. Then the generated classes 
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
 *         &lt;element ref="{}ORDER"/>
 *         &lt;element ref="{}DELIVERY"/>
 *         &lt;element ref="{}DELTCODE"/>
 *         &lt;element ref="{}DELDATE"/>
 *         &lt;element ref="{}DELTIME"/>
 *         &lt;element ref="{}ZBBDEL_ITEM" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="SEGMENT" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "order",
    "delivery",
    "deltcode",
    "deldate",
    "deltime",
    "zbbdelitem"
})
@XmlRootElement(name = "ZBBDEL_HEADER")
public class ZBBDELHEADER {

    @XmlElement(name = "ORDER", required = true)
    protected String order;
    @XmlElement(name = "DELIVERY")
    protected int delivery;
    @XmlElement(name = "DELTCODE", required = true)
    protected String deltcode;
    @XmlElement(name = "DELDATE")
    protected String deldate;
    @XmlElement(name = "DELTIME")
    protected String deltime;
    @XmlElement(name = "ZBBDEL_ITEM", required = true)
    protected List<ZBBDELITEM> zbbdelitem;
    @XmlAttribute(name = "SEGMENT", required = true)
    protected int segment;

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDER() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDER(String value) {
        this.order = value;
    }

    /**
     * Gets the value of the delivery property.
     * 
     */
    public int getDELIVERY() {
        return delivery;
    }

    /**
     * Sets the value of the delivery property.
     * 
     */
    public void setDELIVERY(int value) {
        this.delivery = value;
    }

    /**
     * Gets the value of the deltcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELTCODE() {
        return deltcode;
    }

    /**
     * Sets the value of the deltcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELTCODE(String value) {
        this.deltcode = value;
    }

    /**
     * Gets the value of the deldate property.
     * 
     */
    public String getDELDATE() {
        return deldate;
    }

    /**
     * Sets the value of the deldate property.
     * 
     */
    public void setDELDATE(String value) {
        this.deldate = value;
    }

    /**
     * Gets the value of the deltime property.
     * 
     */
    public String getDELTIME() {
        return deltime;
    }

    /**
     * Sets the value of the deltime property.
     * 
     */
    public void setDELTIME(String value) {
        this.deltime = value;
    }

    /**
     * Gets the value of the zbbdelitem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the zbbdelitem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZBBDELITEM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ZBBDELITEM }
     * 
     * 
     */
    public List<ZBBDELITEM> getZBBDELITEM() {
        if (zbbdelitem == null) {
            zbbdelitem = new ArrayList<ZBBDELITEM>();
        }
        return this.zbbdelitem;
    }

    /**
     * Gets the value of the segment property.
     * 
     */
    public int getSEGMENT() {
        return segment;
    }

    /**
     * Sets the value of the segment property.
     * 
     */
    public void setSEGMENT(int value) {
        this.segment = value;
    }

}