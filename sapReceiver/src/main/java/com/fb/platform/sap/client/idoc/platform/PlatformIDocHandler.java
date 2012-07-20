/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;

/**
 * @author vinayak
 *
 */
public interface PlatformIDocHandler {

	public void init(MomManager momManager);

	public void handle(SapMomTO sapIdoc);
}
