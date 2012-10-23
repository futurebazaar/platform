/**
 * 
 */
package com.fb.platform.mom.bigBazaar.invoice.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.bigBazaar.invoice.manager.InvoiceManager;
import com.fb.platform.mom.bigBazaar.invoice.receiver.InvoiceMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author nehaga
 *
 */
public class InvoiceManagerImpl implements ReceiverManager, InvoiceManager {
	
	private static Log infoLog = LogFactory.getLog(InvoiceManagerImpl.class);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private InvoiceMessageReceiver invoiceMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		infoLog.info("Registering the receiver InvoiceManagerImpl.");
		momManager.registerReceiver(PlatformDestinationEnum.INVOICE_BB, invoiceMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}

	public void setInvoiceMessageReceiver(InvoiceMessageReceiver invoiceMessageReceiver) {
		this.invoiceMessageReceiver = invoiceMessageReceiver;
	}
}
