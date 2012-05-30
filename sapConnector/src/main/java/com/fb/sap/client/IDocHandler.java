package com.fb.sap.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import com.sap.conn.idoc.IDocDocument;
import com.sap.conn.idoc.IDocDocumentIterator;
import com.sap.conn.idoc.IDocDocumentList;
import com.sap.conn.idoc.IDocXMLProcessor;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocHandler;
import com.sap.conn.jco.server.JCoServerContext;

public class IDocHandler implements JCoIDocHandler {

	static Logger logger = Logger.getLogger("auris");

	public void handleRequest(JCoServerContext serverCtx,
			IDocDocumentList idocList) {
		try {
			NDC.push(serverCtx.getTID());
			IDocXMLProcessor xmlProcessor = JCoIDoc.getIDocFactory()
					.getIDocXMLProcessor();
			IDocDocumentIterator iterator = idocList.iterator();
			while (iterator.hasNext()) {
				IDocDocument idoc = iterator.next();
				String idocNumber = idoc.getIDocNumber();
				String idocType = idoc.getIDocType();
				logger.debug(String.format("Got %s idoc with number %s",
						idocType, idocNumber));
				String path = idocType + "-" + idocNumber + "-idoc.xml";
				FileOutputStream fos = null;
				OutputStreamWriter osw = null;
				try {
					fos = new FileOutputStream(path);
					osw = new OutputStreamWriter(fos, "UTF8");
					xmlProcessor.render(idoc, osw,
							IDocXMLProcessor.RENDER_WITH_TABS_AND_CRLF);
					osw.flush();
					logger.debug(String.format("Saved %s idoc with number %s",
							idocType, idocNumber));
					ISAPEvent event = SAPEventFactory.getEventForIDoc(idocType);
					if (event != null) {
						logger.debug(String.format(
								"Dispatching %s idoc with number %s", idocType,
								idocNumber));
						event.dispatch(xmlProcessor.render(idoc));
						logger.debug(String.format(
								"Dispatched %s idoc with number %s", idocType,
								idocNumber));
					}
				} catch (FileNotFoundException e) {
					logger.error(String.format(
							"File %s not found to save idoc %s", path,
							idocNumber));
				} catch (UnsupportedEncodingException e) {
					logger.error(String.format(
							"Unable to encode to save idoc %s", idocNumber));
				} catch (IOException e) {
					logger.error(String.format("IOError saving save idoc %s",
							idocNumber));
				} finally {
					try {
						if (osw != null)
							osw.close();
						if (fos != null)
							fos.close();
					} catch (IOException e) {
						logger.error(String.format(
								"IOError processing idoc %s", idocNumber));
					}
				}
			}
		} finally {
			NDC.remove();
		}
	}
}
