/**
 * 
 */
package com.fb.platform.mom.receiver.deliveryDelete;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface DeliveryDeleteSender {

	public void sendMessage(Serializable message);

}
