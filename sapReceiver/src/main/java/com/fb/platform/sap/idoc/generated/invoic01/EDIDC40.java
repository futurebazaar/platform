//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.10.03 at 12:52:17 PM IST 
//


package com.fb.platform.sap.idoc.generated.invoic01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * IMP: This file was autogenerated by Jaxb from the src/test/resources/invoic01.xsd. Then the generated classes 
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
 *         &lt;element ref="{}TABNAM"/>
 *         &lt;element ref="{}MANDT"/>
 *         &lt;element ref="{}DOCNUM"/>
 *         &lt;element ref="{}DOCREL"/>
 *         &lt;element ref="{}STATUS"/>
 *         &lt;element ref="{}DIRECT"/>
 *         &lt;element ref="{}OUTMOD"/>
 *         &lt;element ref="{}IDOCTYP"/>
 *         &lt;element ref="{}MESTYP"/>
 *         &lt;element ref="{}SNDPOR"/>
 *         &lt;element ref="{}SNDPRT"/>
 *         &lt;element ref="{}SNDPRN"/>
 *         &lt;element ref="{}RCVPOR"/>
 *         &lt;element ref="{}RCVPRT"/>
 *         &lt;element ref="{}RCVPFC"/>
 *         &lt;element ref="{}RCVPRN"/>
 *         &lt;element ref="{}CREDAT"/>
 *         &lt;element ref="{}CRETIM"/>
 *         &lt;element ref="{}SERIAL"/>
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
    "tabnam",
    "mandt",
    "docnum",
    "docrel",
    "status",
    "direct",
    "outmod",
    "idoctyp",
    "mestyp",
    "sndpor",
    "sndprt",
    "sndprn",
    "rcvpor",
    "rcvprt",
    "rcvpfc",
    "rcvprn",
    "credat",
    "cretim",
    "serial"
})
@XmlRootElement(name = "EDI_DC40")
public class EDIDC40 {

    @XmlElement(name = "TABNAM", required = true)
    protected String tabnam;
    @XmlElement(name = "MANDT")
    protected int mandt;
    @XmlElement(name = "DOCNUM")
    protected String docnum;
    @XmlElement(name = "DOCREL")
    protected int docrel;
    @XmlElement(name = "STATUS")
    protected int status;
    @XmlElement(name = "DIRECT")
    protected int direct;
    @XmlElement(name = "OUTMOD")
    protected int outmod;
    @XmlElement(name = "IDOCTYP", required = true)
    protected String idoctyp;
    @XmlElement(name = "MESTYP", required = true)
    protected String mestyp;
    @XmlElement(name = "SNDPOR", required = true)
    protected String sndpor;
    @XmlElement(name = "SNDPRT", required = true)
    protected String sndprt;
    @XmlElement(name = "SNDPRN", required = true)
    protected String sndprn;
    @XmlElement(name = "RCVPOR", required = true)
    protected String rcvpor;
    @XmlElement(name = "RCVPRT", required = true)
    protected String rcvprt;
    @XmlElement(name = "RCVPFC", required = true)
    protected String rcvpfc;
    @XmlElement(name = "RCVPRN", required = true)
    protected String rcvprn;
    @XmlElement(name = "CREDAT")
    protected int credat;
    @XmlElement(name = "CRETIM")
    protected int cretim;
    @XmlElement(name = "SERIAL")
    protected int serial;
    @XmlAttribute(name = "SEGMENT", required = true)
    protected int segment;

    /**
     * Gets the value of the tabnam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTABNAM() {
        return tabnam;
    }

    /**
     * Sets the value of the tabnam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTABNAM(String value) {
        this.tabnam = value;
    }

    /**
     * Gets the value of the mandt property.
     * 
     */
    public int getMANDT() {
        return mandt;
    }

    /**
     * Sets the value of the mandt property.
     * 
     */
    public void setMANDT(int value) {
        this.mandt = value;
    }

    /**
     * Gets the value of the docnum property.
     * 
     */
    public String getDOCNUM() {
        return docnum;
    }

    /**
     * Sets the value of the docnum property.
     * 
     */
    public void setDOCNUM(String value) {
        this.docnum = value;
    }

    /**
     * Gets the value of the docrel property.
     * 
     */
    public int getDOCREL() {
        return docrel;
    }

    /**
     * Sets the value of the docrel property.
     * 
     */
    public void setDOCREL(int value) {
        this.docrel = value;
    }

    /**
     * Gets the value of the status property.
     * 
     */
    public int getSTATUS() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     */
    public void setSTATUS(int value) {
        this.status = value;
    }

    /**
     * Gets the value of the direct property.
     * 
     */
    public int getDIRECT() {
        return direct;
    }

    /**
     * Sets the value of the direct property.
     * 
     */
    public void setDIRECT(int value) {
        this.direct = value;
    }

    /**
     * Gets the value of the outmod property.
     * 
     */
    public int getOUTMOD() {
        return outmod;
    }

    /**
     * Sets the value of the outmod property.
     * 
     */
    public void setOUTMOD(int value) {
        this.outmod = value;
    }

    /**
     * Gets the value of the idoctyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDOCTYP() {
        return idoctyp;
    }

    /**
     * Sets the value of the idoctyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDOCTYP(String value) {
        this.idoctyp = value;
    }

    /**
     * Gets the value of the mestyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESTYP() {
        return mestyp;
    }

    /**
     * Sets the value of the mestyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESTYP(String value) {
        this.mestyp = value;
    }

    /**
     * Gets the value of the sndpor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNDPOR() {
        return sndpor;
    }

    /**
     * Sets the value of the sndpor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNDPOR(String value) {
        this.sndpor = value;
    }

    /**
     * Gets the value of the sndprt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNDPRT() {
        return sndprt;
    }

    /**
     * Sets the value of the sndprt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNDPRT(String value) {
        this.sndprt = value;
    }

    /**
     * Gets the value of the sndprn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNDPRN() {
        return sndprn;
    }

    /**
     * Sets the value of the sndprn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNDPRN(String value) {
        this.sndprn = value;
    }

    /**
     * Gets the value of the rcvpor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCVPOR() {
        return rcvpor;
    }

    /**
     * Sets the value of the rcvpor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCVPOR(String value) {
        this.rcvpor = value;
    }

    /**
     * Gets the value of the rcvprt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCVPRT() {
        return rcvprt;
    }

    /**
     * Sets the value of the rcvprt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCVPRT(String value) {
        this.rcvprt = value;
    }

    /**
     * Gets the value of the rcvpfc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCVPFC() {
        return rcvpfc;
    }

    /**
     * Sets the value of the rcvpfc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCVPFC(String value) {
        this.rcvpfc = value;
    }

    /**
     * Gets the value of the rcvprn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCVPRN() {
        return rcvprn;
    }

    /**
     * Sets the value of the rcvprn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCVPRN(String value) {
        this.rcvprn = value;
    }

    /**
     * Gets the value of the credat property.
     * 
     */
    public int getCREDAT() {
        return credat;
    }

    /**
     * Sets the value of the credat property.
     * 
     */
    public void setCREDAT(int value) {
        this.credat = value;
    }

    /**
     * Gets the value of the cretim property.
     * 
     */
    public int getCRETIM() {
        return cretim;
    }

    /**
     * Sets the value of the cretim property.
     * 
     */
    public void setCRETIM(int value) {
        this.cretim = value;
    }

    /**
     * Gets the value of the serial property.
     * 
     */
    public int getSERIAL() {
        return serial;
    }

    /**
     * Sets the value of the serial property.
     * 
     */
    public void setSERIAL(int value) {
        this.serial = value;
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
