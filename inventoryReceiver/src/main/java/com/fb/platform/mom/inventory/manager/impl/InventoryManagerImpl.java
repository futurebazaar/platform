/**
 * 
 */
package com.fb.platform.mom.inventory.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.inventory.manager.InventoryManager;
import com.fb.platform.mom.inventory.receiver.InventoryMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class InventoryManagerImpl implements ReceiverManager, InventoryManager {
	
	private static Log infoLog = LogFactory.getLog(LoggerConstants.INVENTORY_LOG);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private InventoryMessageReceiver inventoryMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		infoLog.info("Registering the receiver InventoryManagerImpl.");
		momManager.registerReceiver(PlatformDestinationEnum.INVENTORY, inventoryMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
	
	public void setInventoryMessageReceiver(InventoryMessageReceiver inventoryMessageReceiver) {
		this.inventoryMessageReceiver = inventoryMessageReceiver;
	}

}
