/**
 * 
 */
package com.fb.platform.mom.deliveryDelete.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.deliveryDelete.manager.DeliveryDeleteManager;
import com.fb.platform.mom.deliveryDelete.receiver.DeliveryDeleteMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author nehaga
 *
 */
public class DeliveryDeleteManagerImpl implements ReceiverManager, DeliveryDeleteManager {
<<<<<<< HEAD
	
	private static Log infoLog = LogFactory.getLog(DeliveryDeleteManagerImpl.class);
=======
	private static Log log = LogFactory.getLog(DeliveryDeleteManagerImpl.class);
>>>>>>> sapConnector

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private DeliveryDeleteMessageReceiver deliveryDeleteMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
<<<<<<< HEAD
		infoLog.info("Registering the receiver DeliveryDeleteMessageReceiver.");
=======
		log.info("Registering the receiver DeliveryDeleteMessageReceiver.");
>>>>>>> sapConnector
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_DELETE, deliveryDeleteMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
	
	public void setDeliveryDeleteMessageReceiver(DeliveryDeleteMessageReceiver deliveryDeleteMessageReceiver) {
		this.deliveryDeleteMessageReceiver = deliveryDeleteMessageReceiver;
	}

}
