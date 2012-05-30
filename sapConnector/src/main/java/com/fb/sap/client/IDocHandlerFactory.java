package com.fb.sap.client;

import com.sap.conn.idoc.jco.JCoIDocHandler;
import com.sap.conn.idoc.jco.JCoIDocHandlerFactory;
import com.sap.conn.idoc.jco.JCoIDocServerContext;

public class IDocHandlerFactory implements JCoIDocHandlerFactory {
	private JCoIDocHandler handler = new IDocHandler();

	public JCoIDocHandler getIDocHandler(JCoIDocServerContext serverCtx) {
		return handler;
	}
}
