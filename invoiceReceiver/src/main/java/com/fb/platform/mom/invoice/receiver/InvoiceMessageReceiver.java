/**
 * 
 */
package com.fb.platform.mom.invoice.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.bigBazaar.to.InvoiceTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.invoice._1_0.AddressTO;
import com.fb.platform.invoice._1_0.InvoiceDocumentRefTO;
import com.fb.platform.invoice._1_0.InvoiceHeaderTO;
import com.fb.platform.invoice._1_0.InvoiceLineItemTO;
import com.fb.platform.invoice._1_0.InvoicePartnerHeaderTO;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class InvoiceMessageReceiver implements PlatformMessageReceiver{
	
	private static Log infoLog = LogFactory.getLog(InvoiceMessageReceiver.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.INVOICE_AUDIT_LOG);
	
	private static Properties prop = initProperties();
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = InvoiceMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
		try {
			properties.load(propertiesStream);
		} catch (IOException e) {
			infoLog.error("Error loading properties file.", e);
			throw new PlatformException("Error loading properties file.", e);
		}
		return properties;
	}
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.promotion._1_0");
		} catch (JAXBException e) {
			infoLog.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@Override
	public void handleMessage(Object message) {
		InvoiceTO invoiceTO = (InvoiceTO) message;
		long uid = invoiceTO.getSapIdoc().getAckUID();
		String idocNumber = invoiceTO.getSapIdoc().getIdocNumber();
		String timestamp = invoiceTO.getSapIdoc().getTimestamp().toString();

		auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");
		infoLog.info("Received the message : " + message);

		sendAck(invoiceTO);
	}

	private void sendAck(InvoiceTO invoiceTO) {
		String invoiceURL = prop.getProperty("receiver.invoice.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(invoiceURL);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		com.fb.platform.invoice._1_0.InvoiceTO xmlInvoiceTO = new com.fb.platform.invoice._1_0.InvoiceTO();
		
		xmlInvoiceTO.setSapMomTO(xmlSapMomTO(invoiceTO.getSapIdoc()));
		xmlInvoiceTO.setInvoiceHeaderTO(xmlInvoiceHeaderTO(invoiceTO.getInvoiceHeader()));
		xmlInvoiceTO.setInvoiceLineItemTO(xmlInvoiceLineItemTO(invoiceTO.getInvoiceLineItem()));
		xmlInvoiceTO.getInvoicePartnerHeaderTO().addAll(xmlInvoicePartnerHeaderTOList(invoiceTO.getInvoicePartnerHeader()));
		xmlInvoiceTO.getInvoiceDocumentRefTO().addAll(xmlInvoiceDocumentRefTOList(invoiceTO.getInvoiceDocRef()));
		
		try {
			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlInvoiceTO, outStringWriter);
	
			String xmlResponse = outStringWriter.toString();
			
			parameters.add(new BasicNameValuePair("invoice", xmlResponse));
			parameters.add(new BasicNameValuePair("sender", "MOM"));
			
			UrlEncodedFormEntity entity;
			entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				infoLog.error("Invoice ack not delivered : " + invoiceTO.toString());
				throw new PlatformException("Invoice ack not delivered to tinla on URL : " + invoiceURL);
			}
			auditLog.info(invoiceTO.getSapIdoc().getAckUID() + "," + invoiceTO.getSapIdoc().getIdocNumber() + "," + invoiceTO.getSapIdoc().getTimestamp() + ",true");
			infoLog.info("Invoice ack delivered to tinla. Status code : " + statusCode);
		} catch (UnsupportedEncodingException e) {
			infoLog.error("Error communicating with tinla on url : " + invoiceURL, e);
			infoLog.error("Invoice ack not delivered : " + invoiceTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + invoiceURL, e);
		} catch (ClientProtocolException e) {
			infoLog.error("Error communicating with tinla on url : " + invoiceURL, e);
			infoLog.error("Invoice ack not delivered : " + invoiceTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + invoiceURL, e);
		} catch (IOException e) {
			infoLog.error("Error communicating with tinla on url : " + invoiceURL, e);
			infoLog.error("Invoice ack not delivered : " + invoiceTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + invoiceURL, e);
		} catch (JAXBException e) {
			infoLog.error("Error communicating with tinla on url : " + invoiceURL + " , ", e);
			infoLog.error("Invoice ack not delivered : " + invoiceTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + invoiceURL, e);
		}
	}

	private List<InvoiceDocumentRefTO> xmlInvoiceDocumentRefTOList(List<com.fb.commons.mom.bigBazaar.to.InvoiceDocumentRefTO> invoiceDocRef) {
		List<InvoiceDocumentRefTO> invoiceDocumentRefTOList = new ArrayList<InvoiceDocumentRefTO>();
		
		for(com.fb.commons.mom.bigBazaar.to.InvoiceDocumentRefTO invoiceDocumentRefTO : invoiceDocRef) {
			invoiceDocumentRefTOList.add(xmlInvoiceDocumentRefTO(invoiceDocumentRefTO));
		}
		
		return invoiceDocumentRefTOList;
	}

	private InvoiceDocumentRefTO xmlInvoiceDocumentRefTO(com.fb.commons.mom.bigBazaar.to.InvoiceDocumentRefTO invoiceDocumentRefTO) {
		InvoiceDocumentRefTO xmlInvoiceDocumentRefTO = new InvoiceDocumentRefTO();
		
		xmlInvoiceDocumentRefTO.setIdocDate(invoiceDocumentRefTO.getIdocDate().toDate());
		xmlInvoiceDocumentRefTO.setIdocDocumentNumber(invoiceDocumentRefTO.getIdocDocumentNumber());
		xmlInvoiceDocumentRefTO.setQualifier(invoiceDocumentRefTO.getQualifier());
		
		return xmlInvoiceDocumentRefTO;
	}

	private List<InvoicePartnerHeaderTO> xmlInvoicePartnerHeaderTOList(List<com.fb.commons.mom.bigBazaar.to.InvoicePartnerHeaderTO> invoicePartnerHeader) {
		List<InvoicePartnerHeaderTO> invoicePartnerHeaderTOList = new ArrayList<InvoicePartnerHeaderTO>();
		
		for(com.fb.commons.mom.bigBazaar.to.InvoicePartnerHeaderTO invoicePartnerHeaderTO : invoicePartnerHeader) {
			invoicePartnerHeaderTOList.add(xmlInvoicePartnerHeaderTO(invoicePartnerHeaderTO));
		}
		
		return invoicePartnerHeaderTOList;
	}

	private InvoicePartnerHeaderTO xmlInvoicePartnerHeaderTO(com.fb.commons.mom.bigBazaar.to.InvoicePartnerHeaderTO invoicePartnerHeaderTO) {
		InvoicePartnerHeaderTO xmlInvoicePartnerHeaderTO = new InvoicePartnerHeaderTO();
		
		xmlInvoicePartnerHeaderTO.setCustomerLocVendorNum(invoicePartnerHeaderTO.getCustomerLocVendorNum());
		xmlInvoicePartnerHeaderTO.setIdocOrgCode(invoicePartnerHeaderTO.getIdocOrgCode());
		xmlInvoicePartnerHeaderTO.setIdocUserName(invoicePartnerHeaderTO.getIdocUserName());
		xmlInvoicePartnerHeaderTO.setLanguageKey(invoicePartnerHeaderTO.getLanguageKey());
		xmlInvoicePartnerHeaderTO.setPartnerFunction(invoicePartnerHeaderTO.getPartnerFunction());
		xmlInvoicePartnerHeaderTO.setRegion(invoicePartnerHeaderTO.getRegion());
		xmlInvoicePartnerHeaderTO.setSegment(invoicePartnerHeaderTO.getSegment());
		xmlInvoicePartnerHeaderTO.setAddressTO(xmlAddressTO(invoicePartnerHeaderTO.getAddress()));
		
		return xmlInvoicePartnerHeaderTO;
	}

	private AddressTO xmlAddressTO(com.fb.commons.mom.to.AddressTO address) {
		AddressTO addressTO = new AddressTO();
		
		addressTO.setAddress(address.getAddress());
		addressTO.setCity(address.getCity());
		addressTO.setCountry(address.getCountry());
		addressTO.setFirstName(address.getFirstName());
		addressTO.setLastName(address.getLastName());
		addressTO.setMiddleName(address.getMiddleName());
		addressTO.setPincode(address.getPincode());
		addressTO.setPrimaryTelephone(address.getPrimaryTelephone());
		addressTO.setSecondaryTelephone(address.getSecondaryTelephone());
		addressTO.setState(address.getState());
		
		return addressTO;
	}

	private InvoiceLineItemTO xmlInvoiceLineItemTO(com.fb.commons.mom.bigBazaar.to.InvoiceLineItemTO invoiceLineItem) {
		InvoiceLineItemTO invoiceLineItemTO = new InvoiceLineItemTO();
		
		invoiceLineItemTO.setItemCategory(invoiceLineItem.getItemCategory());
		invoiceLineItemTO.setItemNumber(invoiceLineItem.getItemNumber());
		invoiceLineItemTO.setPlant(invoiceLineItem.getPlant());
		invoiceLineItemTO.setQuantity(invoiceLineItem.getQuantity());
		invoiceLineItemTO.setSegment(invoiceLineItem.getSegment());
		invoiceLineItemTO.setUnitOfMeasurement(invoiceLineItem.getUnitOfMeasurement());
		invoiceLineItemTO.setWeightUnit(invoiceLineItem.getWeightUnit());
		
		return invoiceLineItemTO;
	}

	private InvoiceHeaderTO xmlInvoiceHeaderTO(com.fb.commons.mom.bigBazaar.to.InvoiceHeaderTO invoiceHeader) {
		
		InvoiceHeaderTO invoiceHeaderTO = new InvoiceHeaderTO();
		
		invoiceHeaderTO.setBillingCategory(invoiceHeader.getBillingCategory());
		invoiceHeaderTO.setCurrency(invoiceHeader.getCurrency());
		invoiceHeaderTO.setDocumentNumber(invoiceHeader.getDocumentNumber());
		invoiceHeaderTO.setDocumentType(invoiceHeader.getDocumentType());
		invoiceHeaderTO.setExchangeRate(invoiceHeader.getExchangeRate().getAmount());
		invoiceHeaderTO.setInvoiceType(invoiceHeader.getInvoiceType());
		invoiceHeaderTO.setLocalCurrency(invoiceHeader.getLocalCurrency());
		invoiceHeaderTO.setPaymentKey(invoiceHeader.getPaymentKey());
		invoiceHeaderTO.setReceiptNum(invoiceHeader.getReceiptNum());
		invoiceHeaderTO.setSegment(invoiceHeader.getSegment());
		invoiceHeaderTO.setWeightUnit(invoiceHeader.getWeightUnit());
		
		return invoiceHeaderTO;
	}

	private com.fb.platform.invoice._1_0.SapMomTO xmlSapMomTO(SapMomTO sapIdoc) {
		com.fb.platform.invoice._1_0.SapMomTO xmlSapMomTO = new com.fb.platform.invoice._1_0.SapMomTO();
		
		xmlSapMomTO.setAckUID(sapIdoc.getAckUID());
		xmlSapMomTO.setCanGr(sapIdoc.getCanGr());
		xmlSapMomTO.setIdoc(sapIdoc.getIdoc());
		xmlSapMomTO.setIdocNumber(sapIdoc.getIdocNumber());
		xmlSapMomTO.setPoNumber(sapIdoc.getPoNumber());
		xmlSapMomTO.setRefUID(sapIdoc.getRefUID());
		xmlSapMomTO.setSegmentNumber(sapIdoc.getSegmentNumber());
		xmlSapMomTO.setTimestamp(sapIdoc.getTimestamp().toDate());
		
		return xmlSapMomTO;
	}
}
