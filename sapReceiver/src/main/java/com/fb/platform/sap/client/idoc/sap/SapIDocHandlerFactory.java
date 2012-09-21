/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap;

import org.springframework.beans.factory.annotation.Autowired;

import com.sap.conn.idoc.jco.JCoIDocHandler;
import com.sap.conn.idoc.jco.JCoIDocHandlerFactory;
import com.sap.conn.idoc.jco.JCoIDocServerContext;

/**
 * @author vinayak
 *
 */
public class SapIDocHandlerFactory implements JCoIDocHandlerFactory {

	@Autowired
	private SapIDocHandler sapIDocHandler = null;

	@Override
	public JCoIDocHandler getIDocHandler(JCoIDocServerContext arg0) {
		return sapIDocHandler;
	}

	public void setSapIDocHandler(SapIDocHandler sapIDocHandler) {
		this.sapIDocHandler = sapIDocHandler;
	}
}
