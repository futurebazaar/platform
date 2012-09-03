/**
 * 
 */
package com.fb.platform.mom.receiver.mail;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.mom.to.MailTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author nehaga
 *
 */
public class MailTest extends BaseTestCase {

	@Autowired
	private MailMsgSender mailSender = null;

	@Autowired
	private MomManager momManager = null;

	private MailReceiver testReceiver = null;

	@Before
	public void init() {
		testReceiver = new MailReceiver();
	}

	@Test
	public void sendTwoMails() {

		momManager.registerReceiver(PlatformDestinationEnum.MAIL, testReceiver);

		String[] receivers = {"neha.garani@gmail.com",
				"karnataki.v@gmail.com"};
		MailTO mail1 = new MailTO();
		mail1.setTo(receivers);
		mail1.setFrom("neha.garani@futuregroup.in");
		mail1.setSubject("MOM mail test 1");
		mail1.setMessage("MOM mail test 1 its works!!!!!!!yehhhhhh!!!!!");
		mailSender.sendMessage(mail1);

		MailTO mail2 = new MailTO();
		mail2.setTo(receivers);
		mail2.setFrom("vinayak.karnataki@futuregroup.in");
		mail2.setSubject("MOM mail test 2");
		mail2.setMessage("MOM mail test 2 its works!!!!!!!yehhhhh!!!!!");
		mailSender.sendMessage(mail2);
	}

	@After
	public void verifyResult() {
		//System.out.println("InventoryTest, verifying the count.");
		//assertEquals(2, testReceiver.getCount());
	}

	private static class MailReceiver implements PlatformMessageReceiver {

		private int count = 0;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("MailReceiver, received the mail message, count is : " + count + ", message is : " + message);
			MailTO mailMessage = (MailTO) message;

			if (count == 0) {
				assertEquals("neha.garani@futuregroup.in", mailMessage.getFrom());
			} else if (count == 1) {
				assertEquals("vinayak.karnataki@futuregroup.in", mailMessage.getFrom());
			} else {
				throw new IllegalArgumentException("Invalid message");
			}
			count ++;
			System.out.println("MailReceiver Incremented count to : " + count);
		}

		public int getCount() {
			return count;
		}
	}
}
