/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.mom.manager.MomManager;
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
	public void handle(String idocXml) {
		// TODO Auto-generated method stub
	}
}
