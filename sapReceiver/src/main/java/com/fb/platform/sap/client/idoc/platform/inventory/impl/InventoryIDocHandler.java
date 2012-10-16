/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.inventory.impl;

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
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.idoc.generated.ztinlaIDocType.ObjectFactory;
import com.fb.platform.sap.idoc.generated.ztinlaIDocType.ZTINLAIDOCTYP;
import com.fb.platform.sap.idoc.generated.ztinlaIDocType.ZTINLASEGDLVR;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author vinayak
 *
 */
public class InventoryIDocHandler implements PlatformIDocHandler {

	private static Log infoLog = LogFactory.getLog(InventoryIDocHandler.class);

	public static final String INVENTORY_IDOC_TYPE = "ZTINLA_IDOCTYP";

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

	@Override
	public void handle(String idocXml) {
		infoLog.info("Begin handling Inventory idoc message.");

		//convert the message xml into jaxb bean
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			infoLog.info("received idoc : " + idocXml);
			
			ZTINLAIDOCTYP inventoryIdoc = (ZTINLAIDOCTYP)unmarshaller.unmarshal(new StreamSource(new StringReader(idocXml)));
			
			List<ZTINLASEGDLVR> sapInventoryAckList = inventoryIdoc.getIDOC().getZTINLASEGDLVR();
			for (ZTINLASEGDLVR sapInventoryAck : sapInventoryAckList) {
				InventoryTO inventoryTo = new InventoryTO();

				SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.INVENTORY));
				sapIdoc.setIdoc(idocXml);
				sapIdoc.setIdocNumber(inventoryIdoc.getIDOC().getEDIDC40().getDOCNUM());

				sapIdoc.setRefUID(sapInventoryAck.getMATDOC());
				sapIdoc.setSegmentNumber(sapInventoryAck.getSEGNUM());
				sapIdoc.setPoNumber(sapInventoryAck.getPONUM());
				sapIdoc.setCanGr(sapInventoryAck.getCANGR());
				
				inventoryTo.setArticleId(sapInventoryAck.getMATNR());
				inventoryTo.setIssuingSite(sapInventoryAck.getIWERKS());
				inventoryTo.setIssuingStorageLoc(sapInventoryAck.getILGORT());
				inventoryTo.setMovementType(sapInventoryAck.getBWART());
				inventoryTo.setQuantity(sapInventoryAck.getTRNSFRQUAN());
				inventoryTo.setReceivingSite(sapInventoryAck.getRWERKS());
				inventoryTo.setReceivingStorageLoc(sapInventoryAck.getRLGORT());
				inventoryTo.setTransactionCode(sapInventoryAck.getTCODE());
				inventoryTo.setSellingUnit(sapInventoryAck.getMEINS());
				inventoryTo.setSapIdoc(sapIdoc);

				infoLog.info("*Sending InventoryTO to Inventory destination : " + inventoryTo.toString());
				momManager.send(PlatformDestinationEnum.INVENTORY, inventoryTo);
			}
		} catch (JAXBException e) {
			CorruptMessageTO corruptMessage = new CorruptMessageTO();

			SapMomTO sapIdoc = new SapMomTO(ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.CORRUPT_IDOCS));
			sapIdoc.setIdoc(idocXml);

			corruptMessage.setSapIdoc(sapIdoc);
			corruptMessage.setCause(CorruptMessageCause.CORRUPT_IDOC);
			momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
			infoLog.error("Unable to create Inventory Message for inventory idoc :\n" + sapIdoc.getIdoc(), e);
			infoLog.error("Message logged in corrupt queue.");
		} catch (Exception e) {
			infoLog.error("Error in processing inventory idoc", e);
			throw new PlatformException(e);
		}
	}

	@Override
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}
}
