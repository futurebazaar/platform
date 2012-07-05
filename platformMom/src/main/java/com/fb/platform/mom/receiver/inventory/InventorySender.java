/**
 * 
 */
package com.fb.platform.mom.receiver.inventory;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public interface InventorySender {

	public void sendMessage(Serializable message);

}
