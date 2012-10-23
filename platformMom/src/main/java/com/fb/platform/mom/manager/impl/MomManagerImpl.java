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
import com.fb.platform.mom.receiver.bigBazaar.delivery.DeliveryMessageListener;
import com.fb.platform.mom.receiver.bigBazaar.delivery.DeliverySender;
import com.fb.platform.mom.receiver.bigBazaar.invoice.InvoiceMessageListener;
import com.fb.platform.mom.receiver.bigBazaar.invoice.InvoiceSender;
import com.fb.platform.mom.receiver.corrupt.CorruptMessageListener;
import com.fb.platform.mom.receiver.corrupt.CorruptSender;
import com.fb.platform.mom.receiver.deliveryDelete.DeliveryDeleteMessageListener;
import com.fb.platform.mom.receiver.deliveryDelete.DeliveryDeleteSender;
import com.fb.platform.mom.receiver.dlq.PreDLQMessageListener;
import com.fb.platform.mom.receiver.inventory.InventoryMessageListener;
import com.fb.platform.mom.receiver.inventory.InventorySender;
import com.fb.platform.mom.receiver.itemAck.ItemAckMessageListener;
import com.fb.platform.mom.receiver.itemAck.ItemAckSender;
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
	
	@Autowired
	private CorruptMessageListener corruptListener = null;

	@Autowired
	private CorruptSender corruptSender = null;

	@Autowired
	private DefaultMessageListenerContainer corruptContainer = null;
	
	@Autowired
	private DeliveryDeleteMessageListener deliveryDeleteListener = null;

	@Autowired
	private DeliveryDeleteSender deliveryDeleteSender = null;

	@Autowired
	private DefaultMessageListenerContainer deliveryDeleteContainer = null;
	
	@Autowired
	private ItemAckMessageListener itemAckMessageListener = null;

	@Autowired
	private ItemAckSender itemAckSender = null;

	@Autowired
	private DefaultMessageListenerContainer itemAckContainer = null;
	
	@Autowired
	private InvoiceMessageListener invoiceMessageListener = null;

	@Autowired
	private InvoiceSender invoiceSender = null;

	@Autowired
	private DefaultMessageListenerContainer invoiceContainer = null;
	
	@Autowired
	private DeliveryMessageListener deliveryMessageListener = null;

	@Autowired
	private DeliverySender deliverySender = null;

	@Autowired
	private DefaultMessageListenerContainer deliveryContainer = null;
	
	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.MomManager#send(com.fb.platform.mom.manager.PlatformDestinationEnum, java.lang.Object)
	 */
	@Override
	public void send(PlatformDestinationEnum destination, Serializable message) {
		logger.info("Sending message to destination : " + destination);

		switch (destination) {
		case INVENTORY:
			inventorySender.sendMessage(message);
			break;
		case MAIL:
			mailMsgSender.sendMessage(message);
			break;
		case CORRUPT_IDOCS:
			corruptSender.sendMessage(message);
			break;
		case DELIVERY_DELETE:
			deliveryDeleteSender.sendMessage(message);
			break;
		case ITEM_ACK:
			itemAckSender.sendMessage(message);
			break;
		case INVOICE:
			invoiceSender.sendMessage(message);
			break;
		case DELIVERY:
			deliverySender.sendMessage(message);
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
		logger.info("Registering receiver : " + receiver + " for destination : " + destination);

		switch (destination) {
		case INVENTORY:
			inventoryListener.addReceiver(receiver, destination);
			if (!inventoryContainer.isRunning()) {
				inventoryContainer.start();
			}
			break;
		case MAIL:
			mailListener.addReceiver(receiver, destination);
			if(!mailContainer.isRunning()) {
				mailContainer.start();
			}
			break;
		case PREDLQ:
			preDLQListener.addReceiver(receiver, destination);
			if(!preDLQContainer.isRunning()) {
				preDLQContainer.start();
			}
			break;
		case CORRUPT_IDOCS:
			corruptListener.addReceiver(receiver, destination);
			if(!corruptContainer.isRunning()) {
				corruptContainer.start();
			}
			break;
		case DELIVERY_DELETE:
			deliveryDeleteListener.addReceiver(receiver, destination);
			if(!deliveryDeleteContainer.isRunning()) {
				deliveryDeleteContainer.start();
			}
			break;
		case ITEM_ACK:
			itemAckMessageListener.addReceiver(receiver, destination);
			if(!itemAckContainer.isRunning()) {
				itemAckContainer.start();
			}
			break;
		case INVOICE:
			invoiceMessageListener.addReceiver(receiver, destination);
			if(!invoiceContainer.isRunning()) {
				invoiceContainer.start();
			}
			break;
		case DELIVERY:
			deliveryMessageListener.addReceiver(receiver, destination);
			if(!deliveryContainer.isRunning()) {
				deliveryContainer.start();
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

	public void setCorruptListener(CorruptMessageListener corruptListener) {
		this.corruptListener = corruptListener;
	}

	public void setCorruptSender(CorruptSender corruptSender) {
		this.corruptSender = corruptSender;
	}

	public void setCorruptContainer(DefaultMessageListenerContainer corruptContainer) {
		this.corruptContainer = corruptContainer;
	}

	public void setDeliveryDeleteListener(DeliveryDeleteMessageListener deliveryDeleteListener) {
		this.deliveryDeleteListener = deliveryDeleteListener;
	}

	public void setDeliveryDeleteSender(DeliveryDeleteSender deliveryDeleteSender) {
		this.deliveryDeleteSender = deliveryDeleteSender;
	}

	public void setDeliveryDeleteContainer(DefaultMessageListenerContainer deliveryDeleteContainer) {
		this.deliveryDeleteContainer = deliveryDeleteContainer;
	}

	public void setItemAckMessageListener(ItemAckMessageListener itemAckMessageListener) {
		this.itemAckMessageListener = itemAckMessageListener;
	}

	public void setItemAckSender(ItemAckSender itemAckSender) {
		this.itemAckSender = itemAckSender;
	}

	public void setItemAckContainer(DefaultMessageListenerContainer itemAckContainer) {
		this.itemAckContainer = itemAckContainer;
	}

	public void setInvoiceMessageListener(InvoiceMessageListener invoiceMessageListener) {
		this.invoiceMessageListener = invoiceMessageListener;
	}

	public void setInvoiceSender(InvoiceSender invoiceSender) {
		this.invoiceSender = invoiceSender;
	}

	public void setInvoiceContainer(DefaultMessageListenerContainer invoiceContainer) {
		this.invoiceContainer = invoiceContainer;
	}

	public void setDeliveryMessageListener(DeliveryMessageListener deliveryMessageListener) {
		this.deliveryMessageListener = deliveryMessageListener;
	}

	public void setDeliverySender(DeliverySender deliverySender) {
		this.deliverySender = deliverySender;
	}

	public void setDeliveryContainer(DefaultMessageListenerContainer deliveryContainer) {
		this.deliveryContainer = deliveryContainer;
	}
}
