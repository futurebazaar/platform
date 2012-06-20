/**
 * 
 */
package com.fb.platform.mom.manager.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

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

	@Autowired
	private InventoryMessageListener inventoryListener = null;

	@Autowired
	private InventorySender inventorySender = null;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#send(com.fb.platform.mom.manager.PlatformDestinationEnum, java.lang.Object)
	 */
	@Override
	public void send(PlatformDestinationEnum destination, Serializable message) {
		if (PlatformDestinationEnum.INVENTORY == destination) {
			inventorySender.sendMessage(message);
		}
		throw new IllegalStateException("No sender is configured for the destination : " + destination);
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#registerReceiver(com.fb.platform.mom.manager.PlatformDestinationEnum, com.fb.platform.mom.manager.PlatformMessageReceiver)
	 */
	@Override
	public void registerReceiver(PlatformDestinationEnum destination, PlatformMessageReceiver receiver) {
		if (PlatformDestinationEnum.INVENTORY == destination) {
			inventoryListener.addReceiver(receiver);
		}
		throw new IllegalStateException("no Receiver is configured for the destination : " + destination);
	}
}
