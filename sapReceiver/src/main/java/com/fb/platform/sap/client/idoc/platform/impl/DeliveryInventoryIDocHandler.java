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
import com.fb.commons.mom.to.InventoryTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.ztinlaDlvry.ObjectFactory;
import com.fb.platform.sap.idoc.generated.ztinlaDlvry.ZTINLADLVRY;
import com.fb.platform.sap.idoc.generated.ztinlaDlvry.ZTINLADLVRYTOP;

/**
 * @author vinayak
 *
 */
public class DeliveryInventoryIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(DeliveryInventoryIDocHandler.class);

	public static final String DELIVERY_INVENTORY_IDOC_TYPE = "ZTINLA_DLVRY";

	private MomManager momManager = null;

	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			//TODO move from default package to inventory package somehow
			return JAXBContext.newInstance(ObjectFactory.class);
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the inventory idoc schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the inventory idoc schema classes", e);
		}
	}


	/* (non-Javadoc)
	 * @see com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler#init(com.fb.platform.mom.manager.MomManager)
	 */
	@Override
	public void init(MomManager momManager) {
		this.momManager = momManager;
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler#handle(java.lang.String)
	 */
	@Override
	public void handle(String idocXml) {
		logger.info("Begin handling Delivery Inventory idoc message.");

		//convert the message xml into jaxb bean
		try {
			//the xml received from Sap is flawed. It contains ZTINLA_DLVRY as parent and child level item. We will replace the top level ZTINLA_DLVRY with ZTINLA_DLVRY_TOP
			String tempidocXml = idocXml.replaceFirst("ZTINLA_DLVRY", "ZTINLA_DLVRY_TOP");
			int index = tempidocXml.lastIndexOf("ZTINLA_DLVRY");
			StringBuffer sb = new StringBuffer();
			sb.append(tempidocXml.substring(0, index));
			sb.append("ZTINLA_DLVRY_TOP");
			sb.append(tempidocXml.substring(index + 12));
			idocXml = sb.toString();
			
			Unmarshaller unmarshaller = context.createUnmarshaller();

			ZTINLADLVRYTOP inventoryIdoc = (ZTINLADLVRYTOP)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));

			List<ZTINLADLVRY> sapInventoryAckList = inventoryIdoc.getIDOC().getZTINLADLVRY();
			for (ZTINLADLVRY sapInventoryAck : sapInventoryAckList) {
				InventoryTO inventoryTo = new InventoryTO();
				inventoryTo.setIdocNumber(inventoryIdoc.getIDOC().getEDIDC40().getDOCNUM());
				inventoryTo.setRefUID(sapInventoryAck.getMATDOC());
				inventoryTo.setArticleId(sapInventoryAck.getMATNR());
				inventoryTo.setIssuingSite(sapInventoryAck.getIWERKS());
				inventoryTo.setIssuingStorageLoc(sapInventoryAck.getILGORT());
				inventoryTo.setMovementType(sapInventoryAck.getBWART());
				inventoryTo.setQuantity(sapInventoryAck.getTRNSFRQUAN());
				inventoryTo.setReceivingSite(sapInventoryAck.getRWERKS());
				inventoryTo.setReceivingStorageLoc(sapInventoryAck.getRLGORT());
				inventoryTo.setTransactionCode(sapInventoryAck.getTCODE());
				inventoryTo.setSellingUnit(sapInventoryAck.getMEINS());

				logger.debug("Sending InventoryTO to Inventory destination : " + inventoryTo.toString());
				momManager.send(PlatformDestinationEnum.INVENTORY, inventoryTo);
			}

		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();
			corruptMessage.setMessage(idocXml);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			
			logger.error("Unable to create Inventory Message for delivery inventory idoc :\n" + idocXml);
			logger.error("Message logged in corrupt queue.");
			//throw new PlatformException("Exception while unmarshalling the delivery inventory idoc xml", e);
		}
	}

}
