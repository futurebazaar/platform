/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.sap.client.idoc.platform.impl.DefaultIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.DeliveryInventoryIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.InventoryIDocHandler;

/**
 * @author vinayak
 *
 */
public class PlatformIDocHandlerFactory {

	private static Log logger = LogFactory.getLog(PlatformIDocHandlerFactory.class);

	private InventoryIDocHandler inventoryIDocHandler = null;

	private DeliveryInventoryIDocHandler deliveryInventoryIDocHandler = null;
	
	private DefaultIDocHandler defaultIDocHandler = null;

	@Autowired
	private MomManager momManager = null;

	public void init() {
		inventoryIDocHandler = new InventoryIDocHandler();
		inventoryIDocHandler.init(momManager);

		deliveryInventoryIDocHandler = new DeliveryInventoryIDocHandler();
		deliveryInventoryIDocHandler.init(momManager);
		
		defaultIDocHandler = new DefaultIDocHandler();
		defaultIDocHandler.init(momManager);
	}

	public PlatformIDocHandler getHandler(String idocType) {
		if (logger.isDebugEnabled()) {
			logger.debug("Returning handler for idocType : " + idocType);
		}

		if (idocType.equals(InventoryIDocHandler.INVENTORY_IDOC_TYPE)) {
		return inventoryIDocHandler;
		}
		if (idocType.equals(DeliveryInventoryIDocHandler.DELIVERY_INVENTORY_IDOC_TYPE)) {
			return deliveryInventoryIDocHandler;
		}
		logger.error("No Handler is configured for idocType : " + idocType + ", returning default handler.");
		return defaultIDocHandler;
	}

	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
}
