/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.CorruptMessageCause;
import com.fb.commons.mom.to.CorruptMessageTO;
import com.fb.commons.mom.to.DeliveryDeleteTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.zatgDeld.ObjectFactory;
import com.fb.platform.sap.idoc.generated.zatgDeld.ZATGDELD;
import com.fb.platform.sap.idoc.generated.zatgDeld.ZATGSOD;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(DeliveryDeleteIDocHandler.class);

	public static final String DELIVERY_DELETE = "ZATGDELD";

	private MomManager momManager = null;

	private AckUIDSequenceGenerator ackUIDSequenceGenerator = null;

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			//TODO move from default package to inventory package somehow
			return JAXBContext.newInstance(ObjectFactory.class);
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the delete delivery idoc schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the delete delivery idoc schema classes", e);
		}
	}

	@Override
	public void handle(String idocXml) {
		logger.info("Begin handling delivery delete idoc message.");

		SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY_DELETE));

		sapIdoc.setIdoc(idocXml);
		//convert the message xml into jaxb bean
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			ZATGDELD deliveryDelIdoc = (ZATGDELD)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));
			
			sapIdoc.setIdocNumber(deliveryDelIdoc.getIDOC().getEDIDC40().getDOCNUM());
			
			List<ZATGSOD> sapDelvDelList = deliveryDelIdoc.getIDOC().getZATGSOH().getZATGSOD();
			String orderNo = deliveryDelIdoc.getIDOC().getZATGSOH().getVBELN();
			for (ZATGSOD sapDelvDel : sapDelvDelList) {
				DeliveryDeleteTO deliveryDelete = new DeliveryDeleteTO();
				deliveryDelete.setOrderNo(orderNo);
				deliveryDelete.setDate(sapDelvDel.getDELDATE());
				deliveryDelete.setDeliveryNo(sapDelvDel.getVBELN());
				deliveryDelete.setItemNo(sapDelvDel.getPOSNR());
				deliveryDelete.setTime(sapDelvDel.getDELTIME());
				deliveryDelete.setTransactionCode(sapDelvDel.getDELTCODE());
				deliveryDelete.setUser(sapDelvDel.getDELUSER());
				deliveryDelete.setSapIdoc(sapIdoc);
				logger.info("Sending delivery delete idoc to deliveryDelete destination : " + deliveryDelete.toString());
				momManager.send(PlatformDestinationEnum.DELIVERY_DELETE, deliveryDelete);
			}
		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();
			corruptMessage.setSapIdoc(sapIdoc);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			//TODO send this to some kind of error queue
			logger.error("Unable to create Delivery Delete Message for delivery delete idoc :\n" + sapIdoc.getIdoc());
			logger.error("Message logged in corrupt queue.");
			e.printStackTrace();
			//throw new PlatformException("Exception while unmarshalling the inventory idoc xml", e);
		}
	}

	@Override
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}
}
