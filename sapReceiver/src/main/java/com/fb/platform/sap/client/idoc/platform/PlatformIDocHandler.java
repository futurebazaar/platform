/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author vinayak
 *
 */
public interface PlatformIDocHandler {

	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator);

	public void handle(String idocXml);
}
