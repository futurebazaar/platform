/**
 * 
 */
package com.fb.platform.mom.manager.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.mom.receiver.dlq.PreDLQMessageListener;
import com.fb.platform.mom.receiver.inventory.InventoryMessageListener;
import com.fb.platform.mom.receiver.inventory.InventorySender;
import com.fb.platform.mom.receiver.mail.MailMessageListener;
import com.fb.platform.mom.receiver.mail.MailMsgSender;

/**
 * @author vinayak
 *
 */
public class MomManagerImpl implements MomManager {

	private static Log logger = LogFactory.getLog(MomManagerImpl.class);

	@Autowired
	private InventoryMessageListener inventoryListener = null;

	@Autowired
	private InventorySender inventorySender = null;

	@Autowired
	private DefaultMessageListenerContainer inventoryContainer = null;
	
	@Autowired
	private MailMessageListener mailListener = null;

	@Autowired
	private MailMsgSender mailMsgSender = null;

	@Autowired
	private DefaultMessageListenerContainer mailContainer = null;
	
	@Autowired
	private PreDLQMessageListener preDLQListener = null;

	@Autowired
	private DefaultMessageListenerContainer preDLQContainer = null;

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#send(com.fb.platform.mom.manager.PlatformDestinationEnum, java.lang.Object)
	 */
	@Override
	public void send(PlatformDestinationEnum destination, Serializable message) {
		logger.debug("Sending message to destination : " + destination);

		switch (destination) {
		case INVENTORY:
			inventorySender.sendMessage(message);
			break;
		case MAIL:
			mailMsgSender.sendMessage(message);
			break;
		default:
			throw new IllegalStateException("No sender is configured for the destination : " + destination);
		}
	}

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#registerReceiver(com.fb.platform.mom.manager.PlatformDestinationEnum, com.fb.platform.mom.manager.PlatformMessageReceiver)
	 */
	@Override
	public void registerReceiver(PlatformDestinationEnum destination, PlatformMessageReceiver receiver) {
		logger.debug("Registering receiver : " + receiver + " for destination : " + destination);

		switch (destination) {
		case INVENTORY:
			inventoryListener.addReceiver(receiver);
			if (!inventoryContainer.isRunning()) {
				inventoryContainer.start();
			}
			break;
		case MAIL:
			mailListener.addReceiver(receiver);
			if(!mailContainer.isRunning()) {
				mailContainer.start();
			}
			break;
		case PREDLQ:
			preDLQListener.addReceiver(receiver);
			if(!preDLQContainer.isRunning()) {
				preDLQContainer.start();
			}
			break;
		default:
			throw new IllegalStateException("no Receiver is configured for the destination : " + destination);
		}
	}

	public void setInventoryListener(InventoryMessageListener inventoryListener) {
		this.inventoryListener = inventoryListener;
	}

	public void setInventorySender(InventorySender inventorySender) {
		this.inventorySender = inventorySender;
	}

	public void setInventoryContainer(DefaultMessageListenerContainer inventoryContainer) {
		this.inventoryContainer = inventoryContainer;
	}

	public void setMailListener(MailMessageListener mailListener) {
		this.mailListener = mailListener;
	}

	public void setMailMsgSender(MailMsgSender mailMsgSender) {
		this.mailMsgSender = mailMsgSender;
	}

	public void setMailContainer(DefaultMessageListenerContainer mailContainer) {
		this.mailContainer = mailContainer;
	}

	public void setPreDLQListener(PreDLQMessageListener preDLQListener) {
		this.preDLQListener = preDLQListener;
	}

	public void setPreDLQContainer(DefaultMessageListenerContainer preDLQContainer) {
		this.preDLQContainer = preDLQContainer;
	}
	
}
