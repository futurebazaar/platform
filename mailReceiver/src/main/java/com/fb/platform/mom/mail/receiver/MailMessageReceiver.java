/**
 * 
 */
package com.fb.platform.mom.mail.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mail.MailSender;
import com.fb.commons.mom.to.MailTO;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class MailMessageReceiver implements PlatformMessageReceiver {

	/* (non-Javadoc)
	 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
	 */
	
	@Autowired
	private MailSender mailSender;
	
	private static Log log = LogFactory.getLog(MailMessageReceiver.class);

	@Override
	public void handleMessage(Object message) {
		log.info("Mail receiver got the mail message : " + message);

		MailTO mailTO = (MailTO) message;
		sendMail(mailTO);
	}

	private void sendMail(MailTO mailTO) {
		mailSender.send(mailTO);
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
}
