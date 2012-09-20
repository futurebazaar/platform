//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.27 at 01:59:45 PM IST 
//


package com.fb.platform.sap.idoc.generated.zatgflow;

import java.math.BigDecimal;

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
 *         &lt;element ref="{}MANDT"/>
 *         &lt;element ref="{}SO_VBELN"/>
 *         &lt;element ref="{}SO_POSNR"/>
 *         &lt;element ref="{}VBELV" minOccurs="0"/>
 *         &lt;element ref="{}POSNV"/>
 *         &lt;element ref="{}VBELN"/>
 *         &lt;element ref="{}POSNN"/>
 *         &lt;element ref="{}VBTYP_N"/>
 *         &lt;element ref="{}RFMNG"/>
 *         &lt;element ref="{}MEINS" minOccurs="0"/>
 *         &lt;element ref="{}RFWRT"/>
 *         &lt;choice>
 *           &lt;element ref="{}BWART"/>
 *           &lt;sequence>
 *             &lt;element ref="{}WAERS"/>
 *             &lt;element ref="{}MATNR"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element ref="{}ERZET"/>
 *         &lt;element ref="{}ERDAT"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}PLMIN"/>
 *           &lt;element ref="{}VBTYP_V"/>
 *         &lt;/sequence>
 *         &lt;element ref="{}AEDAT"/>
 *         &lt;element ref="{}BRGEW"/>
 *         &lt;element ref="{}VOLUM"/>
 *         &lt;element ref="{}GBSTK"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}ABSTK"/>
 *           &lt;element ref="{}LFGSK"/>
 *           &lt;element ref="{}LFSTK"/>
 *         &lt;/sequence>
 *         &lt;element ref="{}CMGST" minOccurs="0"/>
 *         &lt;element ref="{}WBSTK" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}FKSTK"/>
 *           &lt;element ref="{}KOSTK"/>
 *           &lt;element ref="{}PKSTK"/>
 *         &lt;/sequence>
 *         &lt;element ref="{}FKSAK" minOccurs="0"/>
 *         &lt;element ref="{}COMPLETENESS" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}MATERIAL"/>
 *           &lt;element ref="{}SITE"/>
 *         &lt;/sequence>
 *         &lt;element ref="{}AUDAT"/>
 *         &lt;element ref="{}AUART"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}ZMEINS"/>
 *           &lt;element ref="{}PSTYV"/>
 *         &lt;/sequence>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}LFART"/>
 *           &lt;element ref="{}ERDAT_DEL"/>
 *           &lt;element ref="{}ERNAM_DEL"/>
 *         &lt;/sequence>
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
    "mandt",
    "sovbeln",
    "soposnr",
    "revbeln",
    "vbelv",
    "posnv",
    "vbeln",
    "posnn",
    "vbtypn",
    "wadatpgi",
    "wadatpgr",
    "fkdat",
    "lgort",
    "netwr",
    "rfmng",
    "meins",
    "rfwrt",
    "bwart",
    "waers",
    "matnr",
    "erzet",
    "erdat",
    "plmin",
    "vbtypv",
    "aedat",
    "brgew",
    "volum",
    "gbstk",
    "abstk",
    "lfgsk",
    "lfstk",
    "cmgst",
    "wbstk",
    "fkstk",
    "fkart",
    "kostk",
    "pkstk",
    "fksak",
    "completeness",
    "material",
    "site",
    "audat",
    "auart",
    "zmeins",
    "pstyv",
    "lfart",
    "erdatdel",
    "ernamdel",
    "lspname",
    "lspnor",
    "lspupddescr",
    "ordstat"
})
@XmlRootElement(name = "ZATGFLOW")
public class ZATGFLOW {

    @XmlElement(name = "MANDT")
    protected int mandt;
    @XmlElement(name = "LSP_NAME")
    protected String lspname;
    @XmlElement(name = "LSP_NOR")
    protected String lspnor;
    @XmlElement(name = "LSP_UPD_DESCR")
    protected String lspupddescr;
    @XmlElement(name = "ORD_STAT")
    protected String ordstat;
    @XmlElement(name = "SO_VBELN")
    protected String sovbeln;
    @XmlElement(name = "SO_POSNR")
    protected int soposnr;
    @XmlElement(name = "RE_VBELN")
    protected String revbeln;
    @XmlElement(name = "VBELV")
    protected String vbelv;
    @XmlElement(name = "POSNV")
    protected int posnv;
    @XmlElement(name = "VBELN")
    protected String vbeln;
    @XmlElement(name = "POSNN")
    protected int posnn;
    @XmlElement(name = "VBTYP_N", required = true)
    protected String vbtypn;
    @XmlElement(name = "WADAT_PGI", required = true)
    protected String wadatpgi;
    @XmlElement(name = "WADAT_PGR", required = true)
    protected String wadatpgr;
    @XmlElement(name = "FKDAT", required = true)
    protected String fkdat;
    @XmlElement(name = "LGORT", required = true)
    protected String lgort;
    @XmlElement(name = "NETWR", required = true)
    protected String netwr;
    @XmlElement(name = "RFMNG", required = true)
    protected BigDecimal rfmng;
    @XmlElement(name = "MEINS")
    protected String meins;
    @XmlElement(name = "RFWRT", required = true)
    protected BigDecimal rfwrt;
    @XmlElement(name = "BWART")
    protected int bwart;
    @XmlElement(name = "WAERS")
    protected String waers;
    @XmlElement(name = "MATNR")
    protected String matnr;
    @XmlElement(name = "ERZET")
    protected int erzet;
    @XmlElement(name = "ERDAT")
    protected String erdat;
    @XmlElement(name = "PLMIN")
    protected String plmin;
    @XmlElement(name = "VBTYP_V")
    protected String vbtypv;
    @XmlElement(name = "AEDAT")
    protected int aedat;
    @XmlElement(name = "BRGEW", required = true)
    protected BigDecimal brgew;
    @XmlElement(name = "VOLUM", required = true)
    protected BigDecimal volum;
    @XmlElement(name = "GBSTK", required = true)
    protected String gbstk;
    @XmlElement(name = "ABSTK")
    protected String abstk;
    @XmlElement(name = "LFGSK")
    protected String lfgsk;
    @XmlElement(name = "LFSTK")
    protected String lfstk;
    @XmlElement(name = "CMGST")
    protected String cmgst;
    @XmlElement(name = "WBSTK")
    protected String wbstk;
    @XmlElement(name = "FKSTK")
    protected String fkstk;
    @XmlElement(name = "FKART")
    protected String fkart;
    @XmlElement(name = "KOSTK")
    protected String kostk;
    @XmlElement(name = "PKSTK")
    protected String pkstk;
    @XmlElement(name = "FKSAK")
    protected String fksak;
    @XmlElement(name = "COMPLETENESS")
    protected String completeness;
    @XmlElement(name = "MATERIAL")
    protected int material;
    @XmlElement(name = "SITE")
    protected String site;
    @XmlElement(name = "AUDAT")
    protected int audat;
    @XmlElement(name = "AUART", required = true)
    protected String auart;
    @XmlElement(name = "ZMEINS")
    protected String zmeins;
    @XmlElement(name = "PSTYV")
    protected String pstyv;
    @XmlElement(name = "LFART")
    protected String lfart;
    @XmlElement(name = "ERDAT_DEL")
    protected String erdatdel;
    @XmlElement(name = "ERNAM_DEL")
    protected String ernamdel;
    @XmlAttribute(name = "SEGMENT", required = true)
    protected int segment;

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
     * Gets the value of the sovbeln property.
     * 
     */
    public String getSOVBELN() {
        return sovbeln;
    }

    /**
     * Sets the value of the sovbeln property.
     * 
     */
    public void setSOVBELN(String value) {
        this.sovbeln = value;
    }
    
    /**
     * Gets the value of the revbeln property.
     * 
     */
    public String getREVBELN() {
        return revbeln;
    }

    /**
     * Sets the value of the revbeln property.
     * 
     */
    public void setREVBELN(String value) {
        this.revbeln = value;
    }

    /**
     * Gets the value of the soposnr property.
     * 
     */
    public int getSOPOSNR() {
        return soposnr;
    }

    /**
     * Sets the value of the soposnr property.
     * 
     */
    public void setSOPOSNR(int value) {
        this.soposnr = value;
    }

    /**
     * Gets the value of the vbelv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBELV() {
        return vbelv;
    }

    /**
     * Sets the value of the vbelv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBELV(String value) {
        this.vbelv = value;
    }

    /**
     * Gets the value of the posnv property.
     * 
     */
    public int getPOSNV() {
        return posnv;
    }

    /**
     * Sets the value of the posnv property.
     * 
     */
    public void setPOSNV(int value) {
        this.posnv = value;
    }

    /**
     * Gets the value of the vbeln property.
     * 
     */
    public String getVBELN() {
        return vbeln;
    }

    /**
     * Sets the value of the vbeln property.
     * 
     */
    public void setVBELN(String value) {
        this.vbeln = value;
    }

    /**
     * Gets the value of the posnn property.
     * 
     */
    public int getPOSNN() {
        return posnn;
    }

    /**
     * Sets the value of the posnn property.
     * 
     */
    public void setPOSNN(int value) {
        this.posnn = value;
    }

    /**
     * Gets the value of the wadatpgi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWADATPGI() {
        return wadatpgi;
    }

    /**
     * Sets the value of the wadatpgi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWADATPGI(String value) {
        this.wadatpgi = value;
    }
    
    /**
     * Gets the value of the wadatpgr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWADATPGR() {
        return wadatpgr;
    }

    /**
     * Sets the value of the wadatpgr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWADATPGR(String value) {
        this.wadatpgr = value;
    }
    
    /**
     * Gets the value of the fkdat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFKDAT() {
        return fkdat;
    }

    /**
     * Sets the value of the fkdat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    
    /**
     * Gets the value of the lgort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLGORT() {
        return lgort;
    }

    /**
     * Sets the value of the lgort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    
    public void setLGORT(String value) {
        this.lgort = value;
    }
    
    /**
     * Gets the value of the netwr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNETWR() {
        return netwr;
    }

    /**
     * Sets the value of the netwr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    
    public void setNETWR(String value) {
        this.netwr = value;
    }
    
    /**
     * Gets the value of the vbtypn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBTYPN() {
        return vbtypn;
    }

    /**
     * Sets the value of the vbtypn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBTYPN(String value) {
        this.vbtypn = value;
    }

    /**
     * Gets the value of the rfmng property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRFMNG() {
        return rfmng;
    }

    /**
     * Sets the value of the rfmng property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRFMNG(BigDecimal value) {
        this.rfmng = value;
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
     * Gets the value of the rfwrt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRFWRT() {
        return rfwrt;
    }

    /**
     * Sets the value of the rfwrt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRFWRT(BigDecimal value) {
        this.rfwrt = value;
    }

    /**
     * Gets the value of the bwart property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getBWART() {
        return bwart;
    }

    /**
     * Sets the value of the bwart property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setBWART(int value) {
        this.bwart = value;
    }

    /**
     * Gets the value of the waers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWAERS() {
        return waers;
    }

    /**
     * Sets the value of the waers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWAERS(String value) {
        this.waers = value;
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
     * Gets the value of the erzet property.
     * 
     */
    public int getERZET() {
        return erzet;
    }

    /**
     * Sets the value of the erzet property.
     * 
     */
    public void setERZET(int value) {
        this.erzet = value;
    }

    /**
     * Gets the value of the erdat property.
     * 
     */
    public String getERDAT() {
        return erdat;
    }

    /**
     * Sets the value of the erdat property.
     * 
     */
    public void setERDAT(String value) {
        this.erdat = value;
    }

    /**
     * Gets the value of the plmin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLMIN() {
        return plmin;
    }

    /**
     * Sets the value of the plmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLMIN(String value) {
        this.plmin = value;
    }

    /**
     * Gets the value of the vbtypv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBTYPV() {
        return vbtypv;
    }

    /**
     * Sets the value of the vbtypv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBTYPV(String value) {
        this.vbtypv = value;
    }

    /**
     * Gets the value of the aedat property.
     * 
     */
    public int getAEDAT() {
        return aedat;
    }

    /**
     * Sets the value of the aedat property.
     * 
     */
    public void setAEDAT(int value) {
        this.aedat = value;
    }

    /**
     * Gets the value of the brgew property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBRGEW() {
        return brgew;
    }

    /**
     * Sets the value of the brgew property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBRGEW(BigDecimal value) {
        this.brgew = value;
    }

    /**
     * Gets the value of the volum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVOLUM() {
        return volum;
    }

    /**
     * Sets the value of the volum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVOLUM(BigDecimal value) {
        this.volum = value;
    }

    /**
     * Gets the value of the gbstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGBSTK() {
        return gbstk;
    }

    /**
     * Sets the value of the gbstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGBSTK(String value) {
        this.gbstk = value;
    }

    /**
     * Gets the value of the abstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getABSTK() {
        return abstk;
    }

    /**
     * Sets the value of the abstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setABSTK(String value) {
        this.abstk = value;
    }

    /**
     * Gets the value of the lfgsk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLFGSK() {
        return lfgsk;
    }

    /**
     * Sets the value of the lfgsk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLFGSK(String value) {
        this.lfgsk = value;
    }

    /**
     * Gets the value of the lfstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLFSTK() {
        return lfstk;
    }

    /**
     * Sets the value of the lfstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLFSTK(String value) {
        this.lfstk = value;
    }

    /**
     * Gets the value of the cmgst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMGST() {
        return cmgst;
    }

    /**
     * Sets the value of the cmgst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMGST(String value) {
        this.cmgst = value;
    }

    /**
     * Gets the value of the wbstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWBSTK() {
        return wbstk;
    }

    /**
     * Sets the value of the wbstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWBSTK(String value) {
        this.wbstk = value;
    }

    /**
     * Gets the value of the fkstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFKSTK() {
        return fkstk;
    }

    /**
     * Sets the value of the fkstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFKSTK(String value) {
        this.fkstk = value;
    }

    /**
     * Gets the value of the fkart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFKART() {
        return fkart;
    }

    /**
     * Sets the value of the fkart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFKART(String value) {
        this.fkart = value;
    }

    /**
     * Gets the value of the kostk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKOSTK() {
        return kostk;
    }

    /**
     * Sets the value of the kostk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKOSTK(String value) {
        this.kostk = value;
    }

    /**
     * Gets the value of the pkstk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPKSTK() {
        return pkstk;
    }

    /**
     * Sets the value of the pkstk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPKSTK(String value) {
        this.pkstk = value;
    }

    /**
     * Gets the value of the fksak property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFKSAK() {
        return fksak;
    }

    /**
     * Sets the value of the fksak property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFKSAK(String value) {
        this.fksak = value;
    }

    /**
     * Gets the value of the completeness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPLETENESS() {
        return completeness;
    }

    /**
     * Sets the value of the completeness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPLETENESS(String value) {
        this.completeness = value;
    }

    /**
     * Gets the value of the material property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getMATERIAL() {
        return material;
    }

    /**
     * Sets the value of the material property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setMATERIAL(int value) {
        this.material = value;
    }

    /**
     * Gets the value of the site property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public String getSITE() {
        return site;
    }

    /**
     * Sets the value of the site property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setSITE(String value) {
        this.site = value;
    }

    /**
     * Gets the value of the audat property.
     * 
     */
    public int getAUDAT() {
        return audat;
    }

    /**
     * Sets the value of the audat property.
     * 
     */
    public void setAUDAT(int value) {
        this.audat = value;
    }

    /**
     * Gets the value of the auart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUART() {
        return auart;
    }

    /**
     * Sets the value of the auart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUART(String value) {
        this.auart = value;
    }

    /**
     * Gets the value of the zmeins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZMEINS() {
        return zmeins;
    }

    /**
     * Sets the value of the zmeins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZMEINS(String value) {
        this.zmeins = value;
    }

    /**
     * Gets the value of the pstyv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPSTYV() {
        return pstyv;
    }

    /**
     * Sets the value of the pstyv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPSTYV(String value) {
        this.pstyv = value;
    }

    /**
     * Gets the value of the lfart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLFART() {
        return lfart;
    }

    /**
     * Sets the value of the lfart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLFART(String value) {
        this.lfart = value;
    }

    /**
     * Gets the value of the erdatdel property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public String getERDATDEL() {
        return erdatdel;
    }

    /**
     * Sets the value of the erdatdel property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setERDATDEL(String value) {
        this.erdatdel = value;
    }

    /**
     * Gets the value of the lspname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLSPNAME() {
        return lspname;
    }

    /**
     * Sets the value of the lspname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLSPNAME(String value) {
        this.lspname = value;
    }
    
    /**
     * Gets the value of the lspnor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLSPNOR() {
        return lspnor;
    }

    /**
     * Sets the value of the lspnor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLSPNOR(String value) {
        this.lspnor = value;
    }
    
    /**
     * Gets the value of the ordstat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDSTAT() {
        return ordstat;
    }

    /**
     * Sets the value of the ordstat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDSTAT(String value) {
        this.ordstat = value;
    }
    
    /**
     * Gets the value of the lspupddescr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLSPUPDDESCR() {
        return lspupddescr;
    }

    /**
     * Sets the value of the lspupddescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLSPUPDDESCR(String value) {
        this.lspupddescr = value;
    }
    
    /**
     * Gets the value of the ernamdel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getERNAMDEL() {
        return ernamdel;
    }

    /**
     * Sets the value of the ernamdel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setERNAMDEL(String value) {
        this.ernamdel = value;
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
