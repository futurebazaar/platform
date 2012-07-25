/**
 * 
 */
package com.fb.platform.mom.order.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.order.manager.OrderManager;
import com.fb.platform.mom.order.receiver.OrderMessageReceiver;

/**
 * @author nehaga
 *
 */
public class OrderManagerImpl implements ReceiverManager, OrderManager {
	private static Log log = LogFactory.getLog(OrderManagerImpl.class);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private OrderMessageReceiver orderMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		log.info("Registering the receiver OrderManagerImpl.");
		momManager.registerReceiver(PlatformDestinationEnum.ORDER, orderMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
	
	public void setOrderMessageReceiver(OrderMessageReceiver orderMessageReceiver) {
		this.orderMessageReceiver = orderMessageReceiver;
	}

}
