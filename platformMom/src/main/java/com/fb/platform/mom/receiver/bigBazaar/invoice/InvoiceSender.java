/**
 * 
 */
package com.fb.platform.mom.receiver.bigBazaar.invoice;

import java.io.Serializable;

/**
 * @author nehaga
 *
 */
public interface InvoiceSender {
	public void sendMessage(Serializable message);
}
