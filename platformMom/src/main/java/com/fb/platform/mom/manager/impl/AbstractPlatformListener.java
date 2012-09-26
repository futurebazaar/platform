/**
 * 
 */
package com.fb.platform.mom.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.mom.receiver.delivery.DeliveryDeleteMessageListener;
import com.fb.platform.mom.receiver.inventory.InventoryMessageListener;
import com.fb.platform.mom.receiver.itemAck.ItemAckMessageListener;

/**
 * @author vinayak
 *
 */
public abstract class AbstractPlatformListener {
	
	private static Log logger = LogFactory.getLog(AbstractPlatformListener.class);
	
	private static Log itemAckLog = LogFactory.getLog(ItemAckMessageListener.class);
	
	private static Log deliveryDeleteLog = LogFactory.getLog(DeliveryDeleteMessageListener.class);
	
	private static Log inventoryLog = LogFactory.getLog(InventoryMessageListener.class);
	
	private volatile List<PlatformMessageReceiver> receivers = new ArrayList<PlatformMessageReceiver>();

	public void addReceiver(PlatformMessageReceiver receiver, PlatformDestinationEnum destination) {
		switch(destination) {

		case ITEM_ACK:
			itemAckLog.info(receivers + " ,receiver count : " + receivers.size());
			itemAckLog.info("registering receiver : " + receiver);
			this.receivers.add(receiver);
			itemAckLog.info(receivers + " ,receiver count : " + receivers.size());
			break;
		case DELIVERY_DELETE:
			deliveryDeleteLog.info(receivers + " ,receiver count : " + receivers.size());
			deliveryDeleteLog.info("registering receiver : " + receiver);
			this.receivers.add(receiver);
			deliveryDeleteLog.info(receivers + " ,receiver count : " + receivers.size());
			break;
		case INVENTORY:
			inventoryLog.info(receivers + " ,receiver count : " + receivers.size());
			inventoryLog.info("registering receiver : " + receiver);
			this.receivers.add(receiver);
			inventoryLog.info(receivers + " ,receiver count : " + receivers.size());
			break;
		default:
			logger.info(receivers + " ,receiver count : " + receivers.size());
			logger.info("registering receiver : " + receiver + " , destination : " + destination);
			this.receivers.add(receiver);
			logger.info(receivers + " ,receiver count : " + receivers.size());
			break;
		}
	}

	public void notify(Object message, PlatformDestinationEnum destination) {
		try {
			if(receivers.size() <= 0) {
				switch(destination) {
					case ITEM_ACK:
						itemAckLog.error("No receiver found for item ack : " + message.toString());
						throw new PlatformException("No receiver registered for the destination : " + destination);
					case DELIVERY_DELETE:
						deliveryDeleteLog.error("No receiver found for delivery delete : " + message.toString());
						throw new PlatformException("No receiver registered for the destination : " + destination);
					case INVENTORY:
						inventoryLog.error("No receiver found for inventory : " + message.toString());
						throw new PlatformException("No receiver registered for the destination : " + destination);
				}
			}
			for (PlatformMessageReceiver receiver : receivers) {
				receiver.handleMessage(message);
			}
		} catch (PlatformException e) {
			logger.error("Exception while notifying receivers on destination : " + destination, e);
			throw e;
		}
	}
}
