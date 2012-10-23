/**
 * 
 */
package com.fb.platform.mom.receiver.bigBazaar.delivery;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface DeliverySender {
	public void sendMessage(Serializable message);
}
