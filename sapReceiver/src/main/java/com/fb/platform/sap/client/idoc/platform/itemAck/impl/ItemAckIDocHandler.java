/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.itemAck.impl;

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
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.ItemAckOrderItemProcessor;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.zatgflow.ObjectFactory;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOWTOP;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author nehaga
 *
 */
public class ItemAckIDocHandler implements PlatformIDocHandler {

	private static Log infoLog = LogFactory.getLog(ItemAckIDocHandler.class);

	public static final String ITEM_ACK_IDOC_TYPE = "ZATGFLOW";

	private MomManager momManager = null;

	private AckUIDSequenceGenerator ackUIDSequenceGenerator = null;
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			//TODO move from default package to inventory package somehow
			return JAXBContext.newInstance(ObjectFactory.class);
		} catch (JAXBException e) {
			infoLog.error("Error Initializing the JAXBContext to bind the order idoc schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the order idoc schema classes", e);
		}
	}

	@Override
	public void handle(String idocXml) {
		infoLog.info("Begin handling order idoc message.");

		ItemAckOrderItemProcessor orderItemProcessor = new ItemAckOrderItemProcessorImpl();
		//convert the message xml into jaxb bean
		try {
			//the xml received from Sap is flawed. It contains ZATGFLOW as parent and child level item. We will replace the top level ZATGFLOW with ZATGFLOW_TOP
			String tempidocXml = idocXml.replaceFirst("ZATGFLOW", "ZATGFLOW_TOP");
			int index = tempidocXml.lastIndexOf("ZATGFLOW");
			StringBuffer sb = new StringBuffer();
			sb.append(tempidocXml.substring(0, index));
			sb.append("ZATGFLOW_TOP");
			sb.append(tempidocXml.substring(index + 8));
			idocXml = sb.toString();
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			infoLog.info("received idoc : " + idocXml);

			ZATGFLOWTOP orderIdoc = (ZATGFLOWTOP)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));
			
			List<ZATGFLOW> ackList = orderIdoc.getIDOC().getZATGFLOW();
			List<ItemTO> orderItems = orderItemProcessor.getOrderItems(ackList);
			for (ItemTO orderItem : orderItems) {

				SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.ITEM_ACK));
				sapIdoc.setIdoc(idocXml);
				sapIdoc.setIdocNumber(orderIdoc.getIDOC().getEDIDC40().getDOCNUM());
				orderItem.setSapIdoc(sapIdoc);

				infoLog.info("Sending ItemTO to item ack destination : " + orderItem.toString());
				momManager.send(PlatformDestinationEnum.ITEM_ACK, orderItem);
			}
		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();

			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.CORRUPT_IDOCS));
			sapIdoc.setIdoc(idocXml);

			corruptMessage.setSapIdoc(sapIdoc);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			infoLog.error("Unable to create Message for item ack idoc :\n" + sapIdoc.getIdoc(), e);
			infoLog.error("Message logged in corrupt queue.", e);
		} catch (Exception e) {
			infoLog.error("Error in processing item ack idoc : " + idocXml, e);
			throw new PlatformException(e);
		}
	}
	
	@Override
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}
}
