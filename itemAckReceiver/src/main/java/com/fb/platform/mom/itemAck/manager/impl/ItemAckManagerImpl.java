/**
 * 
 */
package com.fb.platform.mom.itemAck.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.itemAck.manager.ItemAckManager;
import com.fb.platform.mom.itemAck.receiver.ItemAckMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author nehaga
 *
 */
public class ItemAckManagerImpl implements ReceiverManager, ItemAckManager {
	private static Log log = LogFactory.getLog(ItemAckManagerImpl.class);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private ItemAckMessageReceiver itemAckMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		log.info("Registering the receiver ItemAckManagerImpl.");
		momManager.registerReceiver(PlatformDestinationEnum.ITEM_ACK, itemAckMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
	
	public void setItemAckMessageReceiver(ItemAckMessageReceiver itemAckMessageReceiver) {
		this.itemAckMessageReceiver = itemAckMessageReceiver;
	}

}