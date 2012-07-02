/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import generated.ObjectFactory;
import generated.ZTINLAIDOCTYP;
import generated.ZTINLASEGDLVR;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.mom.inventory.to.InventoryTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;

/**
 * @author vinayak
 *
 */
public class InventoryIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(InventoryIDocHandler.class);

	public static final String INVENTORY_IDOC_TYPE = "ZTINLA_IDOCTYP";

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

	@Override
	public void handle(String idocXml) {
		logger.info("Begin handling idoc message.");

		//convert the message xml into jaxb bean
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			ZTINLAIDOCTYP inventoryIdoc = (ZTINLAIDOCTYP)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));

			List<ZTINLASEGDLVR> sapInventoryAckList = inventoryIdoc.getIDOC().getZTINLASEGDLVR();
			for (ZTINLASEGDLVR sapInventoryAck : sapInventoryAckList) {
				InventoryTO inventoryTo = new InventoryTO();
				inventoryTo.setArticleId(sapInventoryAck.getMATNR());
				inventoryTo.setIssuingSite(sapInventoryAck.getRWERKS());
				inventoryTo.setIssuingStorageLoc(sapInventoryAck.getRLGORT());
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
			logger.error("Unable to create Inventory Message for inventory idoc :\n" + idocXml);
			//TODO send this to some kind of error queue
			throw new PlatformException("Exception while unmarshalling the inventory idoc xml", e);
		}
	}

	@Override
	public void init(MomManager momManager) {
		this.momManager = momManager;
	}
}
