/**
 * 
 */
package com.fb.platform.mom.receiver.bigBazaar.deliveryDelete;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface DeliveryDeleteBBSender {
	public void sendMessage(Serializable message);
}
