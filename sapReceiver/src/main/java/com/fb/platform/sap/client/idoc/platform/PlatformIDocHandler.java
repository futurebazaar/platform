/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import com.fb.platform.mom.manager.MomManager;
<<<<<<< HEAD
import com.fb.platform.sap.util.AckUIDSequenceGenerator;
=======
>>>>>>> sapConnector

/**
 * @author vinayak
 *
 */
public interface PlatformIDocHandler {

<<<<<<< HEAD
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator);
=======
	public void init(MomManager momManager);
>>>>>>> sapConnector

	public void handle(String idocXml);
}
