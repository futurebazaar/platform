/**
 * 
 */
package com.fb.platform.mom.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author vinayak
 *
 */
public abstract class AbstractPlatformListener {
	
	private static Log logger = LogFactory.getLog(AbstractPlatformListener.class);

	private List<PlatformMessageReceiver> receivers = new ArrayList<PlatformMessageReceiver>();

	public void addReceiver(PlatformMessageReceiver receiver) {
		logger.info(receivers + " ,receiver count : " + receivers.size());
		logger.info("registering receiver : " + receiver);
		this.receivers.add(receiver);
		logger.info(receivers + " ,receiver count : " + receivers.size());
	}

	public void notify(Object message) {
		logger.info(receivers + " ,receiver count : " + receivers.size());
		for (PlatformMessageReceiver receiver : receivers) {
			logger.info("sending ack to receiver : " + receiver);
			receiver.handleMessage(message);
		}
	}
}
