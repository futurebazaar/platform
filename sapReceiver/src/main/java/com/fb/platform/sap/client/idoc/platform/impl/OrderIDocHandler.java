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
import com.fb.commons.mom.to.OrderTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOWTOP;
import com.fb.platform.sap.idoc.generated.ztinlaIDocType.ObjectFactory;
import com.fb.platform.sap.idoc.generated.ztinlaIDocType.ZTINLAIDOCTYP;
import com.fb.platform.sap.idoc.generated.ztinlaIDocType.ZTINLASEGDLVR;

/**
 * @author nehaga
 *
 */
public class OrderIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(OrderIDocHandler.class);

	public static final String ORDER_IDOC_TYPE = "ZATGFLOW";

	private MomManager momManager = null;

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			//TODO move from default package to inventory package somehow
			return JAXBContext.newInstance(ObjectFactory.class);
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the order idoc schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the order idoc schema classes", e);
		}
	}

	@Override
	public void handle(SapMomTO sapIdoc) {
		logger.info("Begin handling order idoc message.");
		String idocXml = null;
		//convert the message xml into jaxb bean
		try {
			//the xml received from Sap is flawed. It contains ZATGFLOW as parent and child level item. We will replace the top level ZATGFLOW with ZATGFLOW_TOP
			String tempidocXml = sapIdoc.getIdoc().replaceFirst("ZATGFLOW", "ZATGFLOW_TOP");
			int index = tempidocXml.lastIndexOf("ZATGFLOW");
			StringBuffer sb = new StringBuffer();
			sb.append(tempidocXml.substring(0, index));
			sb.append("ZATGFLOW_TOP");
			sb.append(tempidocXml.substring(index + 8));
			idocXml = sb.toString();
			
			sapIdoc.setIdoc(idocXml);
			
			Unmarshaller unmarshaller = context.createUnmarshaller();

			ZATGFLOWTOP orderIdoc = (ZATGFLOWTOP)unmarshaller.unmarshal(new StreamSource(new StringReader(sapIdoc.getIdoc())));

			List<ZATGFLOW> orderList = orderIdoc.getIDOC().getZATGFLOW();
			for (ZATGFLOW sapOrderAck : orderList) {
				OrderTO order = new OrderTO();
				logger.debug("Sending OrderTO to Order destination : " + order.toString());
				momManager.send(PlatformDestinationEnum.ORDER, order);
			}
		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();
			corruptMessage.setSapIdoc(sapIdoc);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			//TODO send this to some kind of error queue
			logger.error("Unable to create order Message for order idoc :\n" + sapIdoc.getIdoc());
			logger.error("Message logged in corrupt queue.");
			//throw new PlatformException("Exception while unmarshalling the inventory idoc xml", e);
		}
	}

	@Override
	public void init(MomManager momManager) {
		this.momManager = momManager;
	}
}
