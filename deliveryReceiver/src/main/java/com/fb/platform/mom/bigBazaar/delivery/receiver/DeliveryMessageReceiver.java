/**
 * 
 */
package com.fb.platform.mom.bigBazaar.delivery.receiver;

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
import com.fb.commons.mom.bigBazaar.to.DeliveryTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.delivery._1_0.DeliveryAdditionalHeaderTO;
import com.fb.platform.delivery._1_0.DeliveryControlTO;
import com.fb.platform.delivery._1_0.DeliveryDeadlineTO;
import com.fb.platform.delivery._1_0.DeliveryHeaderTO;
import com.fb.platform.delivery._1_0.DeliveryItemTO;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class DeliveryMessageReceiver implements PlatformMessageReceiver{
	
	private static Log infoLog = LogFactory.getLog(DeliveryMessageReceiver.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.DELIVERY_BB_AUDIT_LOG);
	
	private static Properties prop = initProperties();
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = DeliveryMessageReceiver.class.getClassLoader().getResourceAsStream("receivers.properties");
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
		DeliveryTO deliveryTO = (DeliveryTO) message;
		long uid = deliveryTO.getSapIdoc().getAckUID();
		String idocNumber = deliveryTO.getSapIdoc().getIdocNumber();
		String timestamp = deliveryTO.getSapIdoc().getTimestamp().toString();

		auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");
		infoLog.info("Received the message : " + message);

		sendAck(deliveryTO);
	}

	private void sendAck(DeliveryTO deliveryTO) {
		String deliveryURL = prop.getProperty("receiver.bigBazaar.delivery.url");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(deliveryURL);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		com.fb.platform.delivery._1_0.DeliveryTO xmldeliveryTO = new com.fb.platform.delivery._1_0.DeliveryTO();
		
		xmldeliveryTO.setSapMomTO(xmlSapMomTO(deliveryTO.getSapIdoc()));
		xmldeliveryTO.setDeliveryHeaderTO(xmlDeliveryHeaderTO(deliveryTO.getDeliveryHeaderTO()));
		
		try {
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(xmldeliveryTO, outStringWriter);
	
			String xmlResponse = outStringWriter.toString();
			
			parameters.add(new BasicNameValuePair("delivery", xmlResponse));
			parameters.add(new BasicNameValuePair("sender", "MOM"));
			
			UrlEncodedFormEntity entity;
			entity = new UrlEncodedFormEntity(parameters, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				infoLog.error("delivery ack not delivered : " + deliveryTO.toString());
				throw new PlatformException("delivery ack not delivered to tinla on URL : " + deliveryURL);
			}
			auditLog.info(deliveryTO.getSapIdoc().getAckUID() + "," + deliveryTO.getSapIdoc().getIdocNumber() + "," + deliveryTO.getSapIdoc().getTimestamp() + ",true");
			infoLog.info("delivery ack delivered to tinla. Status code : " + statusCode);
		} catch (UnsupportedEncodingException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryURL, e);
			infoLog.error("delivery ack not delivered : " + deliveryTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryURL, e);
		} catch (ClientProtocolException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryURL, e);
			infoLog.error("delivery ack not delivered : " + deliveryTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryURL, e);
		} catch (IOException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryURL, e);
			infoLog.error("delivery ack not delivered : " + deliveryTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryURL, e);
		} catch (JAXBException e) {
			infoLog.error("Error communicating with tinla on url : " + deliveryURL + " , ", e);
			infoLog.error("delivery ack not delivered : " + deliveryTO.toString());
			throw new PlatformException("Error communicating with tinla on url : " + deliveryURL, e);
		}
	}
	
	private DeliveryHeaderTO xmlDeliveryHeaderTO(com.fb.commons.mom.bigBazaar.to.DeliveryHeaderTO deliveryHeaderTO) {
		DeliveryHeaderTO xmlDeliveryHeader = new DeliveryHeaderTO();
		
		xmlDeliveryHeader.setDeliveryAdditionalHeaderTO(xmlDeliveryAdditionalHeaderTO(deliveryHeaderTO.getDeliveryAdditionalHeaderTO()));
		xmlDeliveryHeader.setDeliveryControlTO(xmlDeliveryControlTO(deliveryHeaderTO.getDeliveryControlTO()));
		xmlDeliveryHeader.setDeliveryDate(deliveryHeaderTO.getDeliveryDate().toDate());
		xmlDeliveryHeader.setDeliveryDeadlineTO(xmlDeliveryDeadlineTO(deliveryHeaderTO.getDeliveryDeadlineTO()));
		xmlDeliveryHeader.getDeliveryItemTO().addAll(xmlDeliveryItemTO(deliveryHeaderTO.getDeliveryItemList()));
		xmlDeliveryHeader.setNetWeight(deliveryHeaderTO.getNetWeight());
		xmlDeliveryHeader.setPackageCount(deliveryHeaderTO.getPackageCount());
		xmlDeliveryHeader.setReceivingPoint(deliveryHeaderTO.getReceivingPoint());
		xmlDeliveryHeader.setSalesDistributionDoc(deliveryHeaderTO.getSalesDistributionDoc());
		xmlDeliveryHeader.setSalesOrganization(deliveryHeaderTO.getSalesOrganization());
		xmlDeliveryHeader.setSegment(deliveryHeaderTO.getSegment());
		xmlDeliveryHeader.setShippingConditions(deliveryHeaderTO.getShippingConditions());
		xmlDeliveryHeader.setTotalWeight(deliveryHeaderTO.getTotalWeight());
		xmlDeliveryHeader.setVolume(deliveryHeaderTO.getVolume());
		xmlDeliveryHeader.setWarehouseRef(deliveryHeaderTO.getWarehouseRef());
		
		return xmlDeliveryHeader;
	}

	private List<DeliveryItemTO> xmlDeliveryItemTO(List<com.fb.commons.mom.bigBazaar.to.DeliveryItemTO> deliveryItemList) {
		List<DeliveryItemTO> xmlDeliveryItemList = new ArrayList<DeliveryItemTO>();
		
		for(com.fb.commons.mom.bigBazaar.to.DeliveryItemTO deliveryItem : deliveryItemList) {
			xmlDeliveryItemList.add(xmlDeliveryItem(deliveryItem));
		}
		
		return xmlDeliveryItemList;
	}
	
	private DeliveryItemTO xmlDeliveryItem(com.fb.commons.mom.bigBazaar.to.DeliveryItemTO deliveryItemTO) {
		DeliveryItemTO xmlDeliveryItem = new DeliveryItemTO();
		
		xmlDeliveryItem.setActualQuantity(deliveryItemTO.getActualQuantity());
		xmlDeliveryItem.setActualQuantityStockUnit(deliveryItemTO.getActualQuantityStockUnit());
		xmlDeliveryItem.setArticleEntered(deliveryItemTO.getArticleEntered());
		xmlDeliveryItem.setArticleNumber(deliveryItemTO.getArticleNumber());
		xmlDeliveryItem.setDeliveryGroup(deliveryItemTO.getDeliveryGroup());
		xmlDeliveryItem.setDistributionChannel(deliveryItemTO.getDistributionChannel());
		xmlDeliveryItem.setDivision(deliveryItemTO.getDivision());
		xmlDeliveryItem.setExpirationDate(deliveryItemTO.getExpirationDate().toDate());
		xmlDeliveryItem.setExternalItemNumber(deliveryItemTO.getExternalItemNumber());
		xmlDeliveryItem.setGrossWeight(deliveryItemTO.getGrossWeight());
		xmlDeliveryItem.setInternationalArticleNumber(deliveryItemTO.getInternationalArticleNumber());
		xmlDeliveryItem.setItemNumber(deliveryItemTO.getItemNumber());
		xmlDeliveryItem.setLoadingGroup(deliveryItemTO.getLoadingGroup());
		xmlDeliveryItem.setMaterialGroup(deliveryItemTO.getMaterialGroup());
		xmlDeliveryItem.setNetWeight(deliveryItemTO.getNetWeight());
		xmlDeliveryItem.setPlant(deliveryItemTO.getPlant());
		xmlDeliveryItem.setSalesDesc(deliveryItemTO.getSalesDesc());
		xmlDeliveryItem.setSalesUnit(deliveryItemTO.getSalesUnit());
		xmlDeliveryItem.setSegment(deliveryItemTO.getSegment());
		xmlDeliveryItem.setStorageLocation(deliveryItemTO.getStorageLocation());
		xmlDeliveryItem.setTransportationGroup(deliveryItemTO.getTransportationGroup());
		xmlDeliveryItem.setUnitOfMeasurement(deliveryItemTO.getUnitOfMeasurement());
		xmlDeliveryItem.setVolume(deliveryItemTO.getVolume());
		xmlDeliveryItem.setWeightUnit(deliveryItemTO.getWeightUnit());
		
		return xmlDeliveryItem;
	}

	private DeliveryDeadlineTO xmlDeliveryDeadlineTO(com.fb.commons.mom.bigBazaar.to.DeliveryDeadlineTO deliveryDeadlineTO) {
		DeliveryDeadlineTO xmlDeliveryDeadline = new DeliveryDeadlineTO();
		
		xmlDeliveryDeadline.setActivityFinishDate(deliveryDeadlineTO.getActivityFinishDate().toDate());
		xmlDeliveryDeadline.setActivityStartDate(deliveryDeadlineTO.getActivityStartDate().toDate());
		xmlDeliveryDeadline.setActualFinishDate(deliveryDeadlineTO.getActualFinishDate().toDate());
		xmlDeliveryDeadline.setActualStartDate(deliveryDeadlineTO.getActualStartDate().toDate());
		xmlDeliveryDeadline.setQualifier(deliveryDeadlineTO.getQualifier());
		xmlDeliveryDeadline.setSegment(deliveryDeadlineTO.getSegment());
		
		return xmlDeliveryDeadline;
	}

	private DeliveryControlTO xmlDeliveryControlTO(com.fb.commons.mom.bigBazaar.to.DeliveryControlTO deliveryControlTO) {
		DeliveryControlTO xmlDeliveryControl = new DeliveryControlTO();
		
		xmlDeliveryControl.setQualifier(deliveryControlTO.getQualifier());
		xmlDeliveryControl.setSegment(deliveryControlTO.getSegment());
		
		return xmlDeliveryControl;
	}

	private DeliveryAdditionalHeaderTO xmlDeliveryAdditionalHeaderTO(com.fb.commons.mom.bigBazaar.to.DeliveryAdditionalHeaderTO deliveryAdditionalHeaderTO) {
		DeliveryAdditionalHeaderTO xmlDeliveryAdditionalHeader = new DeliveryAdditionalHeaderTO();
		
		xmlDeliveryAdditionalHeader.setDeliveryPriority(deliveryAdditionalHeaderTO.getDeliveryPriority());
		xmlDeliveryAdditionalHeader.setDeliveryType(deliveryAdditionalHeaderTO.getDeliveryType());
		xmlDeliveryAdditionalHeader.setDeliveryTypeDesc(deliveryAdditionalHeaderTO.getDeliveryTypeDesc());
		xmlDeliveryAdditionalHeader.setSegment(deliveryAdditionalHeaderTO.getSegment());
		xmlDeliveryAdditionalHeader.setTransportationGroup(deliveryAdditionalHeaderTO.getTransportationGroup());
		xmlDeliveryAdditionalHeader.setTransportationGroupDesc(deliveryAdditionalHeaderTO.getTransportationGroupDesc());
		
		return xmlDeliveryAdditionalHeader;
	}

	private com.fb.platform.delivery._1_0.SapMomTO xmlSapMomTO(SapMomTO sapIdoc) {
		com.fb.platform.delivery._1_0.SapMomTO xmlSapMomTO = new com.fb.platform.delivery._1_0.SapMomTO();
		
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
