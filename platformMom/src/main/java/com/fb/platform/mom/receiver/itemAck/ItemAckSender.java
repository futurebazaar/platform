/**
 * 
 */
package com.fb.platform.mom.receiver.itemAck;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface ItemAckSender {

	public void sendMessage(Serializable message);

}
