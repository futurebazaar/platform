/**
 * 
 */
package com.fb.platform.mom.bigBazaar.delivery.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.bigBazaar.delivery.manager.DeliveryManager;
import com.fb.platform.mom.bigBazaar.delivery.receiver.DeliveryMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author nehaga
 *
 */
public class DeliveryManagerImpl implements ReceiverManager, DeliveryManager {
	
	private static Log infoLog = LogFactory.getLog(DeliveryManagerImpl.class);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private DeliveryMessageReceiver deliveryMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		infoLog.info("Registering the receiver DeliveryManagerImpl.");
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_BB, deliveryMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}

	public void setdeliveryMessageReceiver(DeliveryMessageReceiver deliveryMessageReceiver) {
		this.deliveryMessageReceiver = deliveryMessageReceiver;
	}
}
