/**
 * 
 */
package com.fb.platform.mom.receiver.order;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface OrderSender {

	public void sendMessage(Serializable message);

}
