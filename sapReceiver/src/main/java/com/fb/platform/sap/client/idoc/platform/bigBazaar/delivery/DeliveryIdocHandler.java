/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.bigBazaar.delivery;

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
import com.fb.commons.mom.bigBazaar.to.DeliveryAdditionalHeaderTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryControlTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryDeadlineTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryHeaderTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryItemTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryTO;
import com.fb.commons.mom.to.CorruptMessageCause;
import com.fb.commons.mom.to.CorruptMessageTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.delvry01.DELVRY01;
import com.fb.platform.sap.idoc.generated.delvry01.E1EDL18;
import com.fb.platform.sap.idoc.generated.delvry01.E1EDL20;
import com.fb.platform.sap.idoc.generated.delvry01.E1EDL21;
import com.fb.platform.sap.idoc.generated.delvry01.E1EDL24;
import com.fb.platform.sap.idoc.generated.delvry01.E1EDT13;
import com.fb.platform.sap.idoc.generated.invoic01.ObjectFactory;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author nehaga
 *
 */
public class DeliveryIdocHandler implements PlatformIDocHandler {
	
	private static Log infoLog = LogFactory.getLog(DeliveryIdocHandler.class);
	
	public static final String DELIVERY_IDOC_TYPE = "DELVRY01";
	
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
		infoLog.info("Begin handling delivery idoc message : " + idocXml);

		//convert the message xml into jaxb bean
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			DELVRY01 deliveryIdoc = (DELVRY01)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));
			DeliveryTO apiDelivery = new DeliveryTO();
			
			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY));
			
			sapIdoc.setIdoc(idocXml);
			sapIdoc.setIdocNumber(deliveryIdoc.getIDOC().getEDIDC40().getDOCNUM());
			
			apiDelivery.setSapIdoc(sapIdoc);
			apiDelivery.setDeliveryHeaderTO(getAPIDeliveryHeader(deliveryIdoc.getIDOC().getE1EDL20()));
			
			infoLog.info("Sending deliveryTO to delivery destination : " + apiDelivery.toString());
			momManager.send(PlatformDestinationEnum.DELIVERY, apiDelivery);
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

	private DeliveryHeaderTO getAPIDeliveryHeader(E1EDL20 xmlDeliveryHeader) {
		DeliveryHeaderTO apiDeliveryHeader = new DeliveryHeaderTO();
		
		apiDeliveryHeader.setSalesDistributionDoc(xmlDeliveryHeader.getVBELN());
		apiDeliveryHeader.setReceivingPoint(xmlDeliveryHeader.getVSTEL());
		apiDeliveryHeader.setSalesOrganization(xmlDeliveryHeader.getVKORG());
		apiDeliveryHeader.setWarehouseRef(xmlDeliveryHeader.getLGNUM());
		apiDeliveryHeader.setShippingConditions(xmlDeliveryHeader.getVSBED());
		apiDeliveryHeader.setTotalWeight(xmlDeliveryHeader.getBTGEW());
		apiDeliveryHeader.setNetWeight(xmlDeliveryHeader.getNTGEW());
		apiDeliveryHeader.setVolume(xmlDeliveryHeader.getVOLUM());
		apiDeliveryHeader.setPackageCount(xmlDeliveryHeader.getANZPK());
		apiDeliveryHeader.setDeliveryDate(getDateTime(xmlDeliveryHeader.getPODAT(), xmlDeliveryHeader.getPOTIM()));
		apiDeliveryHeader.setDeliveryAdditionalHeaderTO(apiDeliveryAdditionalHeaderTO(xmlDeliveryHeader.getE1EDL21()));
		apiDeliveryHeader.setDeliveryControlTO(apiDeliveryControlTO(xmlDeliveryHeader.getE1EDL18()));
		apiDeliveryHeader.setDeliveryDeadlineTO(apiDeliveryDeadlineTO(xmlDeliveryHeader.getE1EDT13()));
		apiDeliveryHeader.setDeliveryItemList(apiDeliveryItemList(xmlDeliveryHeader.getE1EDL24()));
		apiDeliveryHeader.setSegment(xmlDeliveryHeader.getSEGMENT());
		
		return apiDeliveryHeader;
	}

	private List<DeliveryItemTO> apiDeliveryItemList(List<E1EDL24> xmlDeliveryItemList) {
		List<DeliveryItemTO> apiDeliveryItemList = new ArrayList<DeliveryItemTO>();
		
		for(E1EDL24 xmlDeliveryItem : xmlDeliveryItemList) {
			apiDeliveryItemList.add(apiDeliveryItemList(xmlDeliveryItem));
		}
		
		return apiDeliveryItemList;
	}

	private DeliveryItemTO apiDeliveryItemList(E1EDL24 xmlDeliveryItem) {
		DeliveryItemTO apiDeliveryItem = new DeliveryItemTO();
		
		apiDeliveryItem.setItemNumber(xmlDeliveryItem.getPOSNR());
		apiDeliveryItem.setArticleNumber(xmlDeliveryItem.getMATNR());
		apiDeliveryItem.setArticleEntered(xmlDeliveryItem.getMATWA());
		apiDeliveryItem.setSalesDesc(xmlDeliveryItem.getARKTX());
		apiDeliveryItem.setMaterialGroup(xmlDeliveryItem.getMATKL());
		apiDeliveryItem.setPlant(xmlDeliveryItem.getWERKS());
		apiDeliveryItem.setStorageLocation(xmlDeliveryItem.getLGORT());
		apiDeliveryItem.setActualQuantity(xmlDeliveryItem.getLFIMG());
		apiDeliveryItem.setSalesUnit(xmlDeliveryItem.getVRKME());
		apiDeliveryItem.setActualQuantityStockUnit(xmlDeliveryItem.getLGMNG());
		apiDeliveryItem.setUnitOfMeasurement(xmlDeliveryItem.getMEINS());
		apiDeliveryItem.setNetWeight(xmlDeliveryItem.getNTGEW());
		apiDeliveryItem.setGrossWeight(xmlDeliveryItem.getBRGEW());
		apiDeliveryItem.setWeightUnit(xmlDeliveryItem.getGEWEI());
		apiDeliveryItem.setVolume(xmlDeliveryItem.getVOLUM());
		apiDeliveryItem.setLoadingGroup(xmlDeliveryItem.getLADGR());
		apiDeliveryItem.setTransportationGroup(xmlDeliveryItem.getTRAGR());
		apiDeliveryItem.setDistributionChannel(xmlDeliveryItem.getVTWEG());
		apiDeliveryItem.setDivision(xmlDeliveryItem.getSPART());
		apiDeliveryItem.setDeliveryGroup(xmlDeliveryItem.getGRKOR());
		apiDeliveryItem.setInternationalArticleNumber(xmlDeliveryItem.getEAN11());
		apiDeliveryItem.setExternalItemNumber(xmlDeliveryItem.getPOSEX());
		apiDeliveryItem.setExpirationDate(getDateTime(xmlDeliveryItem.getVFDAT(), null));
		apiDeliveryItem.setSegment(xmlDeliveryItem.getSEGMENT());
		
		
		return apiDeliveryItem;
	}

	private DeliveryDeadlineTO apiDeliveryDeadlineTO(E1EDT13 xmlDeliveryDeadline) {
		DeliveryDeadlineTO apiDeliveryDeadline = new DeliveryDeadlineTO();
		
		apiDeliveryDeadline.setQualifier(xmlDeliveryDeadline.getQUALF());
		apiDeliveryDeadline.setSegment(xmlDeliveryDeadline.getSEGMENT());
		if(StringUtils.isNotBlank(xmlDeliveryDeadline.getNTANF()) && xmlDeliveryDeadline.getNTANF().length() >= 8) {
			
		}
		apiDeliveryDeadline.setActivityStartDate(getDateTime(xmlDeliveryDeadline.getNTANF(), xmlDeliveryDeadline.getNTANZ()));
		apiDeliveryDeadline.setActivityFinishDate(getDateTime(xmlDeliveryDeadline.getNTEND(), xmlDeliveryDeadline.getNTENZ()));
		apiDeliveryDeadline.setActualStartDate(getDateTime(xmlDeliveryDeadline.getISDD(), xmlDeliveryDeadline.getISDZ()));
		apiDeliveryDeadline.setActualFinishDate(getDateTime(xmlDeliveryDeadline.getIEDD(), xmlDeliveryDeadline.getIEDZ()));
		
		return apiDeliveryDeadline;
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

	private DeliveryControlTO apiDeliveryControlTO(E1EDL18 xmlDeliveryControl) {
		DeliveryControlTO apiDeliveryControl = new DeliveryControlTO();
		
		apiDeliveryControl.setQualifier(xmlDeliveryControl.getQUALF());
		apiDeliveryControl.setSegment(xmlDeliveryControl.getSEGMENT());
		
		return apiDeliveryControl;
	}

	private DeliveryAdditionalHeaderTO apiDeliveryAdditionalHeaderTO(E1EDL21 xmlDeliveryAdditionalHeader) {
		DeliveryAdditionalHeaderTO apiDeliveryAdditionalHeader = new DeliveryAdditionalHeaderTO();
		
		apiDeliveryAdditionalHeader.setDeliveryType(xmlDeliveryAdditionalHeader.getLFART());
		apiDeliveryAdditionalHeader.setDeliveryTypeDesc(xmlDeliveryAdditionalHeader.getE1EDL23().getLFARTBEZ());
		apiDeliveryAdditionalHeader.setDeliveryPriority(xmlDeliveryAdditionalHeader.getLPRIO());
		apiDeliveryAdditionalHeader.setTransportationGroup(xmlDeliveryAdditionalHeader.getTRAGR());
		apiDeliveryAdditionalHeader.setTransportationGroupDesc(xmlDeliveryAdditionalHeader.getE1EDL23().getTRAGRBEZ());
		
		return apiDeliveryAdditionalHeader;
	}

}
