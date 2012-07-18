/**
 * 
 */
package com.fb.platform.mom.receiver.corrupt;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface CorruptSender {

	public void sendMessage(Serializable message);

}
