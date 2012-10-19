/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.bigBazaar.invoice;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.bigBazaar.to.InvoiceDocumentRefTO;
import com.fb.commons.mom.bigBazaar.to.InvoiceHeaderTO;
import com.fb.commons.mom.bigBazaar.to.InvoiceLineItemIdentificationTO;
import com.fb.commons.mom.bigBazaar.to.InvoiceLineItemTO;
import com.fb.commons.mom.bigBazaar.to.InvoicePartnerHeaderTO;
import com.fb.commons.mom.bigBazaar.to.InvoiceTO;
import com.fb.commons.mom.bigBazaar.to.InvoiceTypeEnum;
import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.CorruptMessageCause;
import com.fb.commons.mom.to.CorruptMessageTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.commons.to.Money;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.invoic01.E1EDK01;
import com.fb.platform.sap.idoc.generated.invoic01.E1EDK02;
import com.fb.platform.sap.idoc.generated.invoic01.E1EDKA1;
import com.fb.platform.sap.idoc.generated.invoic01.E1EDP01;
import com.fb.platform.sap.idoc.generated.invoic01.E1EDP19;
import com.fb.platform.sap.idoc.generated.invoic01.INVOIC01;
import com.fb.platform.sap.idoc.generated.invoic01.ObjectFactory;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author nehaga
 *
 */
public class InvoiceIdocHandler implements PlatformIDocHandler {
	
	private static Log infoLog = LogFactory.getLog(InvoiceIdocHandler.class);
	
	public static final String INVOICE_IDOC_TYPE = "INVOIC01";
	
	private MomManager momManager = null;

	private AckUIDSequenceGenerator ackUIDSequenceGenerator = null;
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			//TODO move from default package to inventory package somehow
			return JAXBContext.newInstance(ObjectFactory.class);
		} catch (JAXBException e) {
			infoLog.error("Error Initializing the JAXBContext to bind the inventory idoc schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the inventory idoc schema classes", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler#init(com.fb.platform.mom.manager.MomManager, com.fb.platform.sap.util.AckUIDSequenceGenerator)
	 */
	@Override
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler#handle(java.lang.String)
	 */
	@Override
	public void handle(String idocXml) {
		infoLog.info("Begin handling invoice idoc message : " + idocXml);

		//convert the message xml into jaxb bean
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			INVOIC01 invoiceIdoc = (INVOIC01)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));
			
			InvoiceTO apiInvoice = new InvoiceTO();
			
			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.INVOICE));
			
			sapIdoc.setIdoc(idocXml);
			sapIdoc.setIdocNumber(invoiceIdoc.getIDOC().getEDIDC40().getDOCNUM());
			
			apiInvoice.setInvoiceType(InvoiceTypeEnum.getInstance(invoiceIdoc.getIDOC().getE1EDK14().getORGID()));
			apiInvoice.setSapIdoc(sapIdoc);
			apiInvoice.setInvoicePartnerHeader(getApiInvoicePartnerHeader(invoiceIdoc.getIDOC().getE1EDKA1()));
			apiInvoice.setInvoiceHeader(getInvoiceHeader(invoiceIdoc.getIDOC().getE1EDK01()));
			apiInvoice.setInvoiceLineItem(getInvoiceLineItem(invoiceIdoc.getIDOC().getE1EDP01()));
			apiInvoice.setInvoiceDocRef(getDocumentHeaderRef(invoiceIdoc.getIDOC().getE1EDK02()));
			
			infoLog.info("Sending InvoiceTO to Invoice destination : " + apiInvoice.toString());
			momManager.send(PlatformDestinationEnum.INVOICE, apiInvoice);
			
		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();

			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.CORRUPT_IDOCS));
			sapIdoc.setIdoc(idocXml);

			corruptMessage.setSapIdoc(sapIdoc);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			infoLog.error("Logged Unable to create Inventory Message for inventory idoc :\n" + sapIdoc.getIdoc(), e);
		} catch (Exception e) {
			infoLog.error("Error in processing inventory idoc", e);
			throw new PlatformException(e);
		}
	}
	
	private List<InvoicePartnerHeaderTO> getApiInvoicePartnerHeader(List<E1EDKA1> idocInvoicePartnerHeader) {
		List<InvoicePartnerHeaderTO> invoicePartnerHeaderList = new ArrayList<InvoicePartnerHeaderTO>();
		
		for(E1EDKA1 partner : idocInvoicePartnerHeader) {
			InvoicePartnerHeaderTO apiInvoiceHeader = new InvoicePartnerHeaderTO();
			AddressTO addressTO = new AddressTO();
			
			addressTO.setFirstName(partner.getNAME1());
			addressTO.setAddress(partner.getSTRAS() + "," + partner.getSTRS2());
			addressTO.setCity(partner.getORT01());
			addressTO.setPincode(partner.getPSTLZ());
			addressTO.setPrimaryTelephone(partner.getTELF1());
			addressTO.setSecondaryTelephone(partner.getTELFX());
			
			apiInvoiceHeader.setAddress(addressTO);
			
			apiInvoiceHeader.setCustomerLocVendorNum(partner.getLIFNR());
			apiInvoiceHeader.setIdocOrgCode(partner.getPAORG());
			apiInvoiceHeader.setLanguageKey(partner.getSPRAS());
			apiInvoiceHeader.setPartnerFunction(partner.getPARVW());
			apiInvoiceHeader.setRegion(partner.getREGIO());
			
			invoicePartnerHeaderList.add(apiInvoiceHeader);
			
		}
		return invoicePartnerHeaderList;
	}
	
	private InvoiceHeaderTO getInvoiceHeader(E1EDK01 invoiceHeader) {
		InvoiceHeaderTO apiInvoiceHeader = new InvoiceHeaderTO();
		
		apiInvoiceHeader.setCurrency(invoiceHeader.getCURCY());
		apiInvoiceHeader.setBillingCategory(invoiceHeader.getFKTYP());
		apiInvoiceHeader.setDocumentNumber(invoiceHeader.getBELNR());
		apiInvoiceHeader.setDocumentType(invoiceHeader.getBSART());
		
		BigDecimal exchangeRate = invoiceHeader.getWKURS().round(new MathContext(2));
		apiInvoiceHeader.setExchangeRate(new Money(exchangeRate));
		apiInvoiceHeader.setInvoiceType(invoiceHeader.getFKARTRL());
		apiInvoiceHeader.setLocalCurrency(invoiceHeader.getHWAER());
		apiInvoiceHeader.setPaymentKey(invoiceHeader.getZTERM());
		apiInvoiceHeader.setReceiptNum(invoiceHeader.getRECIPNTNO());
		apiInvoiceHeader.setWeightUnit(invoiceHeader.getGEWEI());
		
		return apiInvoiceHeader;
	}
	
	private InvoiceLineItemTO getInvoiceLineItem(E1EDP01 documentItem) {
		InvoiceLineItemTO apiInvoiceLineItem = new InvoiceLineItemTO();
		
		apiInvoiceLineItem.setItemCategory(documentItem.getPSTYV());
		apiInvoiceLineItem.setItemNumber(documentItem.getPOSEX());
		apiInvoiceLineItem.setPlant(documentItem.getWERKS());
		apiInvoiceLineItem.setQuantity(documentItem.getMENGE());
		apiInvoiceLineItem.setUnitOfMeasurement(documentItem.getMENEE());
		apiInvoiceLineItem.setWeightUnit(documentItem.getGEWEI());
		
		List<InvoiceLineItemIdentificationTO> apilineItemIdentification = new ArrayList<InvoiceLineItemIdentificationTO>();
		
		for(E1EDP19 lineItem : documentItem.getE1EDP19()) {
			InvoiceLineItemIdentificationTO apiLineItem = new InvoiceLineItemIdentificationTO();
			
			apiLineItem.setIdocShortText(lineItem.getKTEXT());
			apiLineItem.setMaterialId(lineItem.getIDTNR());
			apiLineItem.setQualifier(lineItem.getQUALF());
			
			apilineItemIdentification.add(apiLineItem);
		}
		
		apiInvoiceLineItem.setLineItemIdentificationTO(apilineItemIdentification);
		return apiInvoiceLineItem;
	}
	
	private List<InvoiceDocumentRefTO> getDocumentHeaderRef(List<E1EDK02> documentRefList) {
		List<InvoiceDocumentRefTO> apiDocumentRefList = new ArrayList<InvoiceDocumentRefTO>();
		int year;
		int month;
		int day;
		for(E1EDK02 documentRef : documentRefList) {
			InvoiceDocumentRefTO apiDocumentRef = new InvoiceDocumentRefTO();
			
			apiDocumentRef.setIdocDocumentNumber(documentRef.getBELNR());
			apiDocumentRef.setQualifier(documentRef.getQUALF());
			
			if(StringUtils.isNotBlank(documentRef.getDATUM()) && documentRef.getDATUM().length() == 8) {
				year = Integer.valueOf(documentRef.getDATUM().substring(0, 4));
				month = Integer.valueOf(documentRef.getDATUM().substring(4, 6));
				day = Integer.valueOf(documentRef.getDATUM().substring(6));
				apiDocumentRef.setIdocDate(new DateTime(year, month, day, 0, 0));
			}
			
			apiDocumentRefList.add(apiDocumentRef);
		}
		return apiDocumentRefList;
	}
}
