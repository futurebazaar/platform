//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.12 at 12:25:40 PM IST 
//


package com.fb.platform.sap.idoc.generated.ztinlaIDocType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * IMP: This file was autogenerated by Jaxb from the src/test/resources/ztinla_idoctype.xsd. Then the generated classes 
 * were manually copied in this package as jaxb generated classes dont have a package.
 * If there is any change in the xml format from sap, regenerate the classes
 * from xsd and replace these generated files.
 * 
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BWART_QNAME = new QName("", "BWART");
    private final static QName _MANDT_QNAME = new QName("", "MANDT");
    private final static QName _DIRECT_QNAME = new QName("", "DIRECT");
    private final static QName _RWERKS_QNAME = new QName("", "R_WERKS");
    private final static QName _OUTMOD_QNAME = new QName("", "OUTMOD");
    private final static QName _STATUS_QNAME = new QName("", "STATUS");
    private final static QName _TCODE_QNAME = new QName("", "T_CODE");
    private final static QName _SNDPRN_QNAME = new QName("", "SNDPRN");
    private final static QName _DOCREL_QNAME = new QName("", "DOCREL");
    private final static QName _MESTYP_QNAME = new QName("", "MESTYP");
    private final static QName _RCVPRN_QNAME = new QName("", "RCVPRN");
    private final static QName _MEINS_QNAME = new QName("", "MEINS");
    private final static QName _RCVPRT_QNAME = new QName("", "RCVPRT");
    private final static QName _MATNR_QNAME = new QName("", "MATNR");
    private final static QName _SNDPRT_QNAME = new QName("", "SNDPRT");
    private final static QName _RCVPOR_QNAME = new QName("", "RCVPOR");
    private final static QName _CREDAT_QNAME = new QName("", "CREDAT");
    private final static QName _RLGORT_QNAME = new QName("", "R_LGORT");
    private final static QName _DOCNUM_QNAME = new QName("", "DOCNUM");
    private final static QName _TRNSFRQUAN_QNAME = new QName("", "TRNSFR_QUAN");
    private final static QName _IDOCTYP_QNAME = new QName("", "IDOCTYP");
    private final static QName _SNDPOR_QNAME = new QName("", "SNDPOR");
    private final static QName _IWERKS_QNAME = new QName("", "I_WERKS");
    private final static QName _TABNAM_QNAME = new QName("", "TABNAM");
    private final static QName _ILGORT_QNAME = new QName("", "I_LGORT");
    private final static QName _CRETIM_QNAME = new QName("", "CRETIM");
    private final static QName _SERIAL_QNAME = new QName("", "SERIAL");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ZTINLAIDOCTYP }
     * 
     */
    public ZTINLAIDOCTYP createZTINLAIDOCTYP() {
        return new ZTINLAIDOCTYP();
    }

    /**
     * Create an instance of {@link IDOC }
     * 
     */
    public IDOC createIDOC() {
        return new IDOC();
    }

    /**
     * Create an instance of {@link EDIDC40 }
     * 
     */
    public EDIDC40 createEDIDC40() {
        return new EDIDC40();
    }

    /**
     * Create an instance of {@link ZTINLASEGDLVR }
     * 
     */
    public ZTINLASEGDLVR createZTINLASEGDLVR() {
        return new ZTINLASEGDLVR();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BWART")
    public JAXBElement<String> createBWART(String value) {
        return new JAXBElement<String>(_BWART_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MANDT")
    public JAXBElement<Integer> createMANDT(Integer value) {
        return new JAXBElement<Integer>(_MANDT_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DIRECT")
    public JAXBElement<Integer> createDIRECT(Integer value) {
        return new JAXBElement<Integer>(_DIRECT_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R_WERKS")
    public JAXBElement<String> createRWERKS(String value) {
        return new JAXBElement<String>(_RWERKS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "OUTMOD")
    public JAXBElement<Integer> createOUTMOD(Integer value) {
        return new JAXBElement<Integer>(_OUTMOD_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "STATUS")
    public JAXBElement<Integer> createSTATUS(Integer value) {
        return new JAXBElement<Integer>(_STATUS_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "T_CODE")
    public JAXBElement<String> createTCODE(String value) {
        return new JAXBElement<String>(_TCODE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SNDPRN")
    public JAXBElement<String> createSNDPRN(String value) {
        return new JAXBElement<String>(_SNDPRN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DOCREL")
    public JAXBElement<Integer> createDOCREL(Integer value) {
        return new JAXBElement<Integer>(_DOCREL_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MESTYP")
    public JAXBElement<String> createMESTYP(String value) {
        return new JAXBElement<String>(_MESTYP_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RCVPRN")
    public JAXBElement<String> createRCVPRN(String value) {
        return new JAXBElement<String>(_RCVPRN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MEINS")
    public JAXBElement<String> createMEINS(String value) {
        return new JAXBElement<String>(_MEINS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RCVPRT")
    public JAXBElement<String> createRCVPRT(String value) {
        return new JAXBElement<String>(_RCVPRT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MATNR")
    public JAXBElement<String> createMATNR(String value) {
        return new JAXBElement<String>(_MATNR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SNDPRT")
    public JAXBElement<String> createSNDPRT(String value) {
        return new JAXBElement<String>(_SNDPRT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "RCVPOR")
    public JAXBElement<String> createRCVPOR(String value) {
        return new JAXBElement<String>(_RCVPOR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CREDAT")
    public JAXBElement<Integer> createCREDAT(Integer value) {
        return new JAXBElement<Integer>(_CREDAT_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "R_LGORT")
    public JAXBElement<String> createRLGORT(String value) {
        return new JAXBElement<String>(_RLGORT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "DOCNUM")
    public JAXBElement<Integer> createDOCNUM(Integer value) {
        return new JAXBElement<Integer>(_DOCNUM_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TRNSFR_QUAN")
    public JAXBElement<String> createTRNSFRQUAN(String value) {
        return new JAXBElement<String>(_TRNSFRQUAN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IDOCTYP")
    public JAXBElement<String> createIDOCTYP(String value) {
        return new JAXBElement<String>(_IDOCTYP_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SNDPOR")
    public JAXBElement<String> createSNDPOR(String value) {
        return new JAXBElement<String>(_SNDPOR_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "I_WERKS")
    public JAXBElement<String> createIWERKS(String value) {
        return new JAXBElement<String>(_IWERKS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TABNAM")
    public JAXBElement<String> createTABNAM(String value) {
        return new JAXBElement<String>(_TABNAM_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "I_LGORT")
    public JAXBElement<String> createILGORT(String value) {
        return new JAXBElement<String>(_ILGORT_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CRETIM")
    public JAXBElement<Integer> createCRETIM(Integer value) {
        return new JAXBElement<Integer>(_CRETIM_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SERIAL")
    public JAXBElement<Integer> createSERIAL(Integer value) {
        return new JAXBElement<Integer>(_SERIAL_QNAME, Integer.class, null, value);
    }

}
