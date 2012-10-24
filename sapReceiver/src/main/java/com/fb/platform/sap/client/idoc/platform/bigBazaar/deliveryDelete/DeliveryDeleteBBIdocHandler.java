/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.bigBazaar.deliveryDelete;

import java.io.StringReader;
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
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBHeaderTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteBBTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryDeleteItemBBTO;
import com.fb.commons.mom.to.CorruptMessageCause;
import com.fb.commons.mom.to.CorruptMessageTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.invoic01.ObjectFactory;
import com.fb.platform.sap.idoc.generated.zbbdeld.ZBBDELD;
import com.fb.platform.sap.idoc.generated.zbbdeld.ZBBDELHEADER;
import com.fb.platform.sap.idoc.generated.zbbdeld.ZBBDELITEM;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteBBIdocHandler implements PlatformIDocHandler {
	
	private static Log infoLog = LogFactory.getLog(DeliveryDeleteBBIdocHandler.class);
	
	public static final String DELIVERY_DELETE_BB_IDOC_TYPE = "ZBBDELD";
	
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
	public void init(MomManager momManager,AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler#handle(java.lang.String)
	 */
	@Override
	public void handle(String idocXml) {
		infoLog.info("Begin handling delivery delete big bazaar idoc message : " + idocXml);

		//convert the message xml into jaxb bean
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			ZBBDELD deliveryDeleteIdoc = (ZBBDELD)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));
			DeliveryDeleteBBTO apiDeliveryDelete = new DeliveryDeleteBBTO();
			
			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY_DELETE_BB));
			
			sapIdoc.setIdoc(idocXml);
			sapIdoc.setIdocNumber(deliveryDeleteIdoc.getIDOC().getEDIDC40().getDOCNUM());
			
			apiDeliveryDelete.setSapIdoc(sapIdoc);
			apiDeliveryDelete.setDeliveryDeleteHeader(apiDeliveryDeleteHeader(deliveryDeleteIdoc.getIDOC().getZBBDELHEADER()));
			
			infoLog.info("Sending DeliveryDeleteBBTO to delivery delete big bazaar destination : " + apiDeliveryDelete.toString());
			momManager.send(PlatformDestinationEnum.DELIVERY_DELETE_BB, apiDeliveryDelete);
		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();

			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.CORRUPT_IDOCS));
			sapIdoc.setIdoc(idocXml);

			corruptMessage.setSapIdoc(sapIdoc);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			infoLog.error("Logged Unable to create delivery delete big bazaar Message for delivery delete idoc :\n" + sapIdoc.getIdoc(), e);
		} catch (Exception e) {
			infoLog.error("Error in processing inventory idoc", e);
			throw new PlatformException(e);
		}
	}

	
	private DeliveryDeleteBBHeaderTO apiDeliveryDeleteHeader(ZBBDELHEADER xmlDeliveryDeleteHeader) {
		DeliveryDeleteBBHeaderTO apiDeliveryDeleteHeader = new DeliveryDeleteBBHeaderTO();
		
		apiDeliveryDeleteHeader.setDeletedCode(xmlDeliveryDeleteHeader.getDELTCODE());
		apiDeliveryDeleteHeader.setDelivery(xmlDeliveryDeleteHeader.getDELIVERY());
		apiDeliveryDeleteHeader.setOrder(xmlDeliveryDeleteHeader.getORDER());
		apiDeliveryDeleteHeader.setDeletedDate(getDateTime(xmlDeliveryDeleteHeader.getDELDATE(), xmlDeliveryDeleteHeader.getDELTIME()));
		apiDeliveryDeleteHeader.setDeletedItems(apiDeletedItems(xmlDeliveryDeleteHeader.getZBBDELITEM()));
		
		return apiDeliveryDeleteHeader;
	}

	private List<DeliveryDeleteItemBBTO> apiDeletedItems(List<ZBBDELITEM> xmlDeletedItems) {
		List<DeliveryDeleteItemBBTO> apiDeletedItems = new ArrayList<DeliveryDeleteItemBBTO>();
		
		for(ZBBDELITEM xmlDeletedItem : xmlDeletedItems) {
			DeliveryDeleteItemBBTO apiDeletedItem = new DeliveryDeleteItemBBTO();
			
			apiDeletedItem.setItemNum(xmlDeletedItem.getPOSNR());
			apiDeletedItem.setUser(xmlDeletedItem.getDELUSER());
			
			apiDeletedItems.add(apiDeletedItem);
		}
		
		return apiDeletedItems;
	}

	private DateTime getDateTime(String date, String time) {
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int min = 0;
		int sec = 0;
		
		if(isDateValid(date)) {
			year = Integer.valueOf(date.substring(0, 4));
			month = Integer.valueOf(date.substring(4, 6));
			month = Integer.valueOf(date.substring(6));
			if(isTimeValid(time)) {
				hour = Integer.valueOf(time.substring(0, 2));
				min = Integer.valueOf(time.substring(2, 4));
				sec = Integer.valueOf(time.substring(4));
			}
		}
		
		return new DateTime(year, month, day, hour, min, sec);
	}
	
	private boolean isDateValid(String date) {
		return (StringUtils.isNotBlank(date) && date.length() >= 8);
	}
	
	private boolean isTimeValid(String time) {
		return (StringUtils.isNotBlank(time) && time.length() >= 8);
	}
}
