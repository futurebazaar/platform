/**
 * 
 */
package com.fb.platform.mom.manager.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.mom.receiver.inventory.InventoryMessageListener;
import com.fb.platform.mom.receiver.inventory.InventorySender;

/**
 * @author vinayak
 *
 */
public class MomManagerImpl implements MomManager {

	private static Log logger = LogFactory.getLog(MomManagerImpl.class);

	@Autowired
	private InventoryMessageListener inventoryListener = null;

	@Autowired
	private InventorySender inventorySender = null;

	@Autowired
	private DefaultMessageListenerContainer inventoryContainer = null;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#send(com.fb.platform.mom.manager.PlatformDestinationEnum, java.lang.Object)
	 */
	@Override
	public void send(PlatformDestinationEnum destination, Serializable message) {
		logger.debug("Sending message to destination : " + destination);

		if (PlatformDestinationEnum.INVENTORY == destination) {
			inventorySender.sendMessage(message);
		} else {
			throw new IllegalStateException("No sender is configured for the destination : " + destination);
		}
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#registerReceiver(com.fb.platform.mom.manager.PlatformDestinationEnum, com.fb.platform.mom.manager.PlatformMessageReceiver)
	 */
	@Override
	public void registerReceiver(PlatformDestinationEnum destination, PlatformMessageReceiver receiver) {
		logger.debug("Registering receiver : " + receiver + " for destination : " + destination);

		inventoryListener.addReceiver(receiver);
		if (PlatformDestinationEnum.INVENTORY == destination) {
			if (!inventoryContainer.isRunning()) {
				inventoryContainer.start();
			}
		} else {
			throw new IllegalStateException("no Receiver is configured for the destination : " + destination);
		}
	}

	public void setInventoryListener(InventoryMessageListener inventoryListener) {
		this.inventoryListener = inventoryListener;
	}

	public void setInventorySender(InventorySender inventorySender) {
		this.inventorySender = inventorySender;
	}

	public void setInventoryContainer(DefaultMessageListenerContainer inventoryContainer) {
		this.inventoryContainer = inventoryContainer;
	}
}
