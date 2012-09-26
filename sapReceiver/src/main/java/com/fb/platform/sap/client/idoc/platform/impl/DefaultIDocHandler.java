/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.util.AckUIDSequenceGenerator;

/**
 * @author vinayak
 *
 */
public class DefaultIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(DefaultIDocHandler.class);

	private MomManager momManager = null;

	@Override
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		//this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
	}

	@Override
	public void handle(String idocXml) {
		
		Date date = new Date();
		logger.error("MOM no handler message received : " + date.toString() 
				+ "\n\n object : " + idocXml );
		
		System.out.println("MOM no handler message received : " + date.toString() 
				+ "\n\n object : " + idocXml );
		
	}
}
