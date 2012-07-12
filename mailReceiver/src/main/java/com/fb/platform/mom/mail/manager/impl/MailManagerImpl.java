/**
 * 
 */
package com.fb.platform.mom.mail.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.receiver.ReceiverManager;
import com.fb.platform.mom.mail.manager.MailManager;
import com.fb.platform.mom.mail.receiver.MailMessageReceiver;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class MailManagerImpl implements ReceiverManager, MailManager {
	private static Log log = LogFactory.getLog(MailManagerImpl.class);

	@Autowired
	private MomManager momManager;
	
	@Autowired
	private MailMessageReceiver mailMessageReceiver;
	
	/* (non-Javadoc)
	 * @see com.fb.launcher.receiver.ReceiverManager#start()
	 */
	@Override
	public void start() {
		log.info("Registering the mail receiver.");
		momManager.registerReceiver(PlatformDestinationEnum.MAIL, mailMessageReceiver);
		momManager.registerReceiver(PlatformDestinationEnum.DLQ, mailMessageReceiver);

	}
	
	public void setMomManager(MomManager momManager) {
		this.momManager = momManager;
	}
	
	public void setMailMessageReceiver(MailMessageReceiver mailMessageReceiver) {
		this.mailMessageReceiver = mailMessageReceiver;
	}

}
