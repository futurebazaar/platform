//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.26 at 12:13:52 PM IST 
//


package com.fb.platform.sap.idoc.generated.ztinlaDlvry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}T_CODE"/>
 *         &lt;element ref="{}MATNR"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element ref="{}R_WERKS"/>
 *             &lt;element ref="{}R_LGORT"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{}I_WERKS"/>
 *             &lt;element ref="{}I_LGORT"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element ref="{}BWART"/>
 *         &lt;element ref="{}MEINS"/>
 *         &lt;element ref="{}TRNSFR_QUAN"/>
 *         &lt;element ref="{}MAT_DOC"/>
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
    "tcode",
    "matnr",
    "rwerks",
    "rlgort",
    "iwerks",
    "ilgort",
    "bwart",
    "meins",
    "trnsfrquan",
    "matdoc",
    "segnum"
})
@XmlRootElement(name = "ZTINLA_DLVRY")
public class ZTINLADLVRY {

    @XmlElement(name = "T_CODE", required = true)
    protected String tcode;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "R_WERKS")
    protected String rwerks;
    @XmlElement(name = "R_LGORT")
    protected String rlgort;
    @XmlElement(name = "I_WERKS")
    protected String iwerks;
    @XmlElement(name = "I_LGORT")
    protected String ilgort;
    @XmlElement(name = "BWART", required = true)
    protected String bwart;
    @XmlElement(name = "MEINS", required = true)
    protected String meins;
    @XmlElement(name = "TRNSFR_QUAN", required = true)
    protected String trnsfrquan;
    @XmlElement(name = "MAT_DOC", required = true)
    protected String matdoc;
    @XmlElement(name = "SEG_NUM", required = true)
    protected int segnum;
    @XmlAttribute(name = "SEGMENT", required = true)
    protected int segment;

    /**
     * Gets the value of the tcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTCODE() {
        return tcode;
    }

    /**
     * Sets the value of the tcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTCODE(String value) {
        this.tcode = value;
    }

    /**
     * Gets the value of the matnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATNR() {
        return matnr;
    }

    /**
     * Sets the value of the matnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATNR(String value) {
        this.matnr = value;
    }

    /**
     * Gets the value of the rwerks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRWERKS() {
        return rwerks;
    }

    /**
     * Sets the value of the rwerks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRWERKS(String value) {
        this.rwerks = value;
    }

    /**
     * Gets the value of the rlgort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRLGORT() {
        return rlgort;
    }

    /**
     * Sets the value of the rlgort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRLGORT(String value) {
        this.rlgort = value;
    }

    /**
     * Gets the value of the iwerks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIWERKS() {
        return iwerks;
    }

    /**
     * Sets the value of the iwerks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIWERKS(String value) {
        this.iwerks = value;
    }

    /**
     * Gets the value of the ilgort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getILGORT() {
        return ilgort;
    }

    /**
     * Sets the value of the ilgort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setILGORT(String value) {
        this.ilgort = value;
    }

    /**
     * Gets the value of the bwart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBWART() {
        return bwart;
    }

    /**
     * Sets the value of the bwart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBWART(String value) {
        this.bwart = value;
    }

    /**
     * Gets the value of the meins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEINS() {
        return meins;
    }

    /**
     * Sets the value of the meins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEINS(String value) {
        this.meins = value;
    }

    /**
     * Gets the value of the trnsfrquan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRNSFRQUAN() {
        return trnsfrquan;
    }

    /**
     * Sets the value of the trnsfrquan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRNSFRQUAN(String value) {
        this.trnsfrquan = value;
    }

    /**
     * Gets the value of the matdoc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDOC() {
        return matdoc;
    }

    /**
     * Sets the value of the segnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEGNUM(int value) {
        this.segnum = value;
    }
    
    /**
     * Gets the value of the segnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public int getSEGNUM() {
        return segnum;
    }

    /**
     * Sets the value of the matdoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDOC(String value) {
        this.matdoc = value;
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
