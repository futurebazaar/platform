/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
<<<<<<< HEAD
import com.fb.platform.sap.util.AckUIDSequenceGenerator;
=======
>>>>>>> sapConnector

/**
 * @author vinayak
 *
 */
public class DefaultIDocHandler implements PlatformIDocHandler {

	private static Log logger = LogFactory.getLog(DefaultIDocHandler.class);

	private MomManager momManager = null;

	@Override
<<<<<<< HEAD
	public void init(MomManager momManager, AckUIDSequenceGenerator ackUIDSequenceGenerator) {
		this.momManager = momManager;
		//this.ackUIDSequenceGenerator = ackUIDSequenceGenerator;
=======
	public void init(MomManager momManager) {
		this.momManager = momManager;
>>>>>>> sapConnector
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
