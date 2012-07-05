/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandler;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.sap.conn.idoc.IDocDocument;
import com.sap.conn.idoc.IDocDocumentIterator;
import com.sap.conn.idoc.IDocDocumentList;
import com.sap.conn.idoc.IDocXMLProcessor;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocHandler;
import com.sap.conn.jco.server.JCoServerContext;

/**
 * @author vinayak
 *
 */
public class SapIDocHandler implements JCoIDocHandler {

	private static Log logger = LogFactory.getLog(SapIDocHandler.class);

	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;

	/* (non-Javadoc)
	 * @see com.sap.conn.idoc.jco.JCoIDocHandler#handleRequest(com.sap.conn.jco.server.JCoServerContext, com.sap.conn.idoc.IDocDocumentList)
	 */
	@Override
	public void handleRequest(JCoServerContext serverContext, IDocDocumentList idocList) {
		IDocXMLProcessor xmlProcessor = JCoIDoc.getIDocFactory().getIDocXMLProcessor();
		IDocDocumentIterator iterator = idocList.iterator();

		while (iterator.hasNext()) {
			IDocDocument idoc = iterator.next();
			String idocNumber = idoc.getIDocNumber();
			String idocType = idoc.getIDocType();

			logger.info("Received IDoc. idocNumber : " + idocNumber + ". idocType : " + idocType);

			String idocXml = xmlProcessor.render(idoc, IDocXMLProcessor.RENDER_WITH_TABS_AND_CRLF);
			logger.info("idoc xml is \n:" + idocXml);

			PlatformIDocHandler platformIDocHandler = platformIDocHandlerFactory.getHandler(idocType);
			platformIDocHandler.handle(idocXml);
		}
	}

	public void setPlatformIDocHandlerFactory(PlatformIDocHandlerFactory platformIDocHandlerFactory) {
		this.platformIDocHandlerFactory = platformIDocHandlerFactory;
	}
}
