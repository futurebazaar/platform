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

/**
 * @author vinayak
 *
 */
public abstract class AbstractPlatformListener {
	
	private static Log logger = LogFactory.getLog(AbstractPlatformListener.class);
	
	private static Log itemAckLog = LogFactory.getLog("ITEM_ACK_LOG");
	
	private static Log itemAckError = LogFactory.getLog("ITEM_ACK_ERROR");
	
	private static Log deliveryDeleteLog = LogFactory.getLog("DELIVERY_DELETE_LOG");
	
	private static Log deliveryDeleteError = LogFactory.getLog("DELIVERY_DELETE_ERROR");
	
	private static Log inventoryLog = LogFactory.getLog("INVENTORY_LOG");
	
	private static Log inventoryError = LogFactory.getLog("INVENTORY_ERROR");
	
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
						itemAckError.error("No receiver found for item ack : " + message.toString());
						break;
					case DELIVERY_DELETE:
						deliveryDeleteError.error("No receiver found for delivery delete : " + message.toString());
						break;
					case INVENTORY:
						inventoryError.error("No receiver found for inventory : " + message.toString());
						break;
				}
			}
			for (PlatformMessageReceiver receiver : receivers) {
				receiver.handleMessage(message);
			}
		} catch (PlatformException e) {
			throw e;
		}
	}
}
