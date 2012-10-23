/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.sap.client.idoc.platform.bigBazaar.delivery.DeliveryIdocHandler;
import com.fb.platform.sap.client.idoc.platform.bigBazaar.deliveryDelete.DeliveryDeleteBBIdocHandler;
import com.fb.platform.sap.client.idoc.platform.bigBazaar.invoice.InvoiceIdocHandler;
import com.fb.platform.sap.client.idoc.platform.deliveryDelete.impl.DeliveryDeleteIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.DefaultIDocHandler;
import com.fb.platform.sap.client.idoc.platform.inventory.impl.DeliveryInventoryIDocHandler;
import com.fb.platform.sap.client.idoc.platform.inventory.impl.InventoryIDocHandler;
import com.fb.platform.sap.client.idoc.platform.itemAck.impl.ItemAckIDocHandler;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author vinayak
 *
 */
public class PlatformIDocHandlerFactory {

	private static Log logger = LogFactory.getLog(PlatformIDocHandlerFactory.class);

	private InventoryIDocHandler inventoryIDocHandler = null;

	private DeliveryInventoryIDocHandler deliveryInventoryIDocHandler = null;
	
	private DeliveryDeleteIDocHandler deliveryDeleteIDocHandler = null;
	
	private ItemAckIDocHandler orderIDocHandler = null;
	
	private InvoiceIdocHandler invoiceIDocHandler = null;
	
	private DeliveryIdocHandler deliveryIDocHandler = null;
	
	private DeliveryDeleteBBIdocHandler deliveryDeleteBBIdocHandler = null;

	private DefaultIDocHandler defaultIDocHandler = null;

	private AckUIDSequenceGenerator ackUIDSequenceGenerator = null;

	@Autowired
	private MomManager momManager = null;

	public void init() {
		inventoryIDocHandler = new InventoryIDocHandler();
		inventoryIDocHandler.init(momManager, ackUIDSequenceGenerator);

		deliveryInventoryIDocHandler = new DeliveryInventoryIDocHandler();
		deliveryInventoryIDocHandler.init(momManager, ackUIDSequenceGenerator);
		
		deliveryDeleteIDocHandler = new DeliveryDeleteIDocHandler();
		deliveryDeleteIDocHandler.init(momManager, ackUIDSequenceGenerator);
		
		orderIDocHandler = new ItemAckIDocHandler();
		orderIDocHandler.init(momManager, ackUIDSequenceGenerator);
		
		invoiceIDocHandler = new InvoiceIdocHandler();
		invoiceIDocHandler.init(momManager, ackUIDSequenceGenerator);
		
		deliveryIDocHandler = new DeliveryIdocHandler();
		deliveryIDocHandler.init(momManager, ackUIDSequenceGenerator);
		
		deliveryDeleteBBIdocHandler = new DeliveryDeleteBBIdocHandler();
		deliveryDeleteBBIdocHandler.init(momManager, ackUIDSequenceGenerator);

		defaultIDocHandler = new DefaultIDocHandler();
		defaultIDocHandler.init(momManager, ackUIDSequenceGenerator);
	}

	public PlatformIDocHandler getHandler(String idocType) {
		logger.info("Returning handler for idocType : " + idocType);
		
		if (idocType.equals(InventoryIDocHandler.INVENTORY_IDOC_TYPE)) {
		return inventoryIDocHandler;
		} else if (idocType.equals(DeliveryInventoryIDocHandler.DELIVERY_INVENTORY_IDOC_TYPE)) {
			return deliveryInventoryIDocHandler;
		} else if (idocType.equals(DeliveryDeleteIDocHandler.DELIVERY_DELETE)) {
			return deliveryDeleteIDocHandler;
		} else if (idocType.equals(ItemAckIDocHandler.ITEM_ACK_IDOC_TYPE)) {
			return orderIDocHandler;
		} else if (idocType.equals(InvoiceIdocHandler.INVOICE_IDOC_TYPE)) {
			return invoiceIDocHandler;
		} else if (idocType.equals(DeliveryIdocHandler.DELIVERY_IDOC_TYPE)) {
			return deliveryIDocHandler;
		} else if (idocType.equals(DeliveryDeleteBBIdocHandler.DELIVERY_DELETE_BB_IDOC_TYPE)) {
			return deliveryDeleteBBIdocHandler;
		}
		logger.error("No Handler is configured for idocType : " + idocType + ", returning default handler.");
		return defaultIDocHandler;
	}

	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}

	public void setAckUIDSequenceGenerator(AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}

}
