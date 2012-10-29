/**
 * 
 */
package com.fb.platform.mom.bigBazaar.deliveryDelete.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.bigBazaar.deliveryDelete.manager.DeliveryDeleteBBManager;
import com.fb.platform.mom.bigBazaar.deliveryDelete.receiver.DeliveryDeleteBBMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteBBManagerImpl implements ReceiverManager, DeliveryDeleteBBManager {
	
	private static Log infoLog = LogFactory.getLog(DeliveryDeleteBBManagerImpl.class);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private DeliveryDeleteBBMessageReceiver deliveryDeleteMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		infoLog.info("Registering the receiver DeliveryManagerImpl.");
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_BB, deliveryDeleteMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}

	public void setDeliveryDeleteMessageReceiver(DeliveryDeleteBBMessageReceiver deliveryDeleteMessageReceiver) {
		this.deliveryDeleteMessageReceiver = deliveryDeleteMessageReceiver;
	}
}
