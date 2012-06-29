/**
 * 
 */
package com.fb.platform.mom.inventory.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.inventory.manager.InventoryManager;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class InventoryManagerImpl implements ReceiverManager, InventoryManager {
	private static Log log = LogFactory.getLog(InventoryManagerImpl.class);

	@Autowired
	MomManager momManager;
	
	@Autowired
	PlatformMessageReceiver messageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		log.info("Registering the receiver.");
		momManager.registerReceiver(PlatformDestinationEnum.INVENTORY, messageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
	
	public void setMessageReceiver(PlatformMessageReceiver messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

}
