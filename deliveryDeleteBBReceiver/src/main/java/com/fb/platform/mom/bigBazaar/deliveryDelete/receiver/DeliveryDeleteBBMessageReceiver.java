/**
 * 
 */
package com.fb.platform.mom.bigBazaar.deliveryDelete.receiver;

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
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.bigbazaar.deliverydelete._1_0.DeliveryDeleteBBHeaderTO;
import com.fb.platform.bigbazaar.deliverydelete._1_0.DeliveryDeleteItemBBTO;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteBBMessageReceiver implements PlatformMessageReceiver{
	
	private static Log infoLog = LogFactory.getLog(DeliveryDeleteBBMessageReceiver.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.DELIVERY_DELETE_BB_AUDIT_LOG);
	
	private static Properties prop = initProperties();
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = DeliveryDeleteBBMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
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
			return JAXBContext.newInstance("com.fb.platform.bigBazaar.deliveryDelete._1_0");
		} catch (JAXBException e) {
			infoLog.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}

	@Override
	public void handleMessage(Object message) {
		DeliveryDeleteBBTO deliveryDeleteBB = (DeliveryDeleteBBTO) message;
		long uid = deliveryDeleteBB.getSapIdoc().getAckUID();
		String idocNumber = deliveryDeleteBB.getSapIdoc().getIdocNumber();
		String timestamp = deliveryDeleteBB.getSapIdoc().getTimestamp().toString();

		auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");
		infoLog.info("Received the message : " + message);

		sendAck(deliveryDeleteBB);
	}

	private void sendAck(DeliveryDeleteBBTO deliveryDeleteBB) {
		String deliveryDeleteURL = prop.getProperty("receiver.bigBazaar.deliveryDelete.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(deliveryDeleteURL);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		com.fb.platform.bigbazaar.deliverydelete._1_0.DeliveryDeleteBBTO xmlDeliveryDelete = new com.fb.platform.bigbazaar.deliverydelete._1_0.DeliveryDeleteBBTO();
		xmlDeliveryDelete.setSapMomTO(xmlSapMomTO(deliveryDeleteBB.getSapIdoc()));
		xmlDeliveryDelete.setDeliveryDeleteBBHeaderTO(xmlDeliveryDeleteHeader(deliveryDeleteBB.getDeliveryDeleteHeader()));
		
		try {
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(xmlDeliveryDelete, outStringWriter);
	
			String xmlResponse = outStringWriter.toString();
			
			parameters.add(new BasicNameValuePair("deliveryDelete", xmlResponse));
			parameters.add(new BasicNameValuePair("sender", "MOM"));
			
			UrlEncodedFormEntity entity;
			entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				infoLog.error("delivery delete big bazaar ack not delivered : " + deliveryDeleteBB.toString());
				throw new PlatformException("delivery delete big bazaar ack delivered to tinla on URL : " + deliveryDeleteURL);
			}
			auditLog.info(deliveryDeleteBB.getSapIdoc().getAckUID() + "," + deliveryDeleteBB.getSapIdoc().getIdocNumber() + "," + deliveryDeleteBB.getSapIdoc().getTimestamp() + ",true");
			infoLog.info("delivery ack delivered to tinla. Status code : " + statusCode);
		} catch (UnsupportedEncodingException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryDeleteURL, e);
			infoLog.error("delivery delete big bazaar ack delivered : " + deliveryDeleteBB.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		} catch (ClientProtocolException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryDeleteURL, e);
			infoLog.error("delivery delete big bazaar ack delivered : " + deliveryDeleteBB.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		} catch (IOException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryDeleteURL, e);
			infoLog.error("delivery delete big bazaar ack delivered : " + deliveryDeleteBB.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		} catch (JAXBException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryDeleteURL + " , ", e);
			infoLog.error("delivery delete big bazaar ack delivered : " + deliveryDeleteBB.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryDeleteURL, e);
		}
	}

	private DeliveryDeleteBBHeaderTO xmlDeliveryDeleteHeader(com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBHeaderTO deliveryDeleteHeader) {
		DeliveryDeleteBBHeaderTO xmlDeliveryDeleteBBHeader = new DeliveryDeleteBBHeaderTO();
		
		xmlDeliveryDeleteBBHeader.setDeletedCode(deliveryDeleteHeader.getDeletedCode());
		xmlDeliveryDeleteBBHeader.setDelivery(deliveryDeleteHeader.getDelivery());
		xmlDeliveryDeleteBBHeader.setOrder(deliveryDeleteHeader.getOrder());
		xmlDeliveryDeleteBBHeader.setDeletedDate(deliveryDeleteHeader.getDeletedDate().toDate());
		xmlDeliveryDeleteBBHeader.getDeliveryDeleteItemBBTO().addAll(xmlDeletedItems(deliveryDeleteHeader.getDeletedItems()));
		
		return xmlDeliveryDeleteBBHeader;
	}

	private List<DeliveryDeleteItemBBTO> xmlDeletedItems(List<com.fb.commons.mom.bigBazaar.to.DeliveryDeleteItemBBTO> deletedItems) {
		List<DeliveryDeleteItemBBTO> xmlDeletedItemsList = new ArrayList<DeliveryDeleteItemBBTO>();
		
		for(com.fb.commons.mom.bigBazaar.to.DeliveryDeleteItemBBTO apiItem : deletedItems) {
			DeliveryDeleteItemBBTO xmlItem = new DeliveryDeleteItemBBTO();
			
			xmlItem.setItemNum(apiItem.getItemNum());
			xmlItem.setUser(apiItem.getUser());
			
			xmlDeletedItemsList.add(xmlItem);
		}
		
		return xmlDeletedItemsList;
	}

	private com.fb.platform.bigbazaar.deliverydelete._1_0.SapMomTO xmlSapMomTO(SapMomTO sapIdoc) {
		com.fb.platform.bigbazaar.deliverydelete._1_0.SapMomTO xmlSapMomTO = new com.fb.platform.bigbazaar.deliverydelete._1_0.SapMomTO();
		
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
