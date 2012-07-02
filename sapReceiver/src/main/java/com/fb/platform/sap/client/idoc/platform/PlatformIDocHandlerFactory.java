/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.sap.client.idoc.platform.impl.DefaultIDocHandler;
import com.fb.platform.sap.client.idoc.platform.impl.InventoryIDocHandler;

/**
 * @author vinayak
 *
 */
public class PlatformIDocHandlerFactory {

	private static Log logger = LogFactory.getLog(PlatformIDocHandlerFactory.class);

	private InventoryIDocHandler inventoryIDocHandler = null;

	@Autowired
	private MomManager momManager = null;

	public void init() {
		inventoryIDocHandler = new InventoryIDocHandler();
		inventoryIDocHandler.init(momManager);
	}

	public PlatformIDocHandler getHandler(String idocType) {
		if (logger.isDebugEnabled()) {
			logger.debug("Returning handler for idocType : " + idocType);
		}

		if (idocType.equals(InventoryIDocHandler.INVENTORY_IDOC_TYPE)) {
			return inventoryIDocHandler;
		}
		logger.error("No Handler is configured for idocType : " + idocType + ", returning null");
		return new DefaultIDocHandler();
	}

	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
}
