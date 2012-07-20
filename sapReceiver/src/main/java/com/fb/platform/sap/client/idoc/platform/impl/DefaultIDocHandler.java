/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.CorruptMessageCause;
import com.fb.commons.mom.to.CorruptMessageTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;

/**
 * @author vinayak
 *
 */
public class DefaultIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(DefaultIDocHandler.class);

	private MomManager momManager = null;

	@Override
	public void init(MomManager momManager) {
		this.momManager = momManager;
	}

	@Override
	public void handle(SapMomTO sapIdoc) {
		
		CorruptMessageTO corruptMessage = new CorruptMessageTO();
		corruptMessage.setCause(CorruptMessageCause.NO_HANDLER);
		corruptMessage.setSapIdoc(sapIdoc);
		momManager.send(PlatformDestinationEnum.CORRUPT_IDOCS, corruptMessage);
		
		logger.error("Unable to process idoc :\n" + sapIdoc.toString());
		logger.error("Message logged in corrupt queue.");
	}
}
