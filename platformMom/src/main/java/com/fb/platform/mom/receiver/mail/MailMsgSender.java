/**
 * 
 */
package com.fb.platform.mom.receiver.mail;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface MailMsgSender {

	public void sendMessage(Serializable message);

}
