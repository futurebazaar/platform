/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.to.SapMomTO;
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
	
	private static Deque<String> sapUniqueIds = new LinkedList<String>();
	
	private static final String uidTag = "MAT_DOC";

	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private static Properties prop = initProperties();

	private static Properties initProperties() {
		Properties properties = new Properties();
		InputStream propertiesStream = SapIDocHandler.class.getClassLoader().getResourceAsStream("receivers.properties");
		try {
			properties.load(propertiesStream);
		} catch (IOException e) {
			logger.error("Error loading properties file.", e);
			throw new PlatformException("Error loading properties file.", e);
		}
		return properties;
	}

	/* (non-Javadoc)
	 * @see com.sap.conn.idoc.jco.JCoIDocHandler#handleRequest(com.sap.conn.jco.server.JCoServerContext, com.sap.conn.idoc.IDocDocumentList)
	 */
	@Override
	public void handleRequest(JCoServerContext serverContext, IDocDocumentList idocList) {
		try {
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
				try {
					SapMomTO sapIdoc = new SapMomTO();
					sapIdoc.setIdoc(idocXml);
					sapIdoc.setIdocNumber(idocNumber);
					logger.info("Sending to idoc handler : " + sapIdoc.toString());
					
					boolean isDuplicate = checkDuplicate(idocXml);
					if(!isDuplicate) {
						platformIDocHandler.handle(sapIdoc);
					}
				} catch (Exception e) {
					saveIdoc(idocNumber, idocXml, "xml");
					logger.error("Could not insert idoc into hornet Q : " + idocNumber, e);
				}
			}
		} catch (Exception e) {
			logger.error("Error processing idoc.", e);
			createIdocFiles(idocList);
		}
	}
	
	private void createIdocFiles(IDocDocumentList idocList) {
		IDocDocument[] idocs = idocList.toArray();
		for (IDocDocument idoc : idocs) {
			String idocNumber = idoc.getIDocNumber();
			String idocMessage = idoc.toString();
			saveIdoc(idocNumber, idocMessage, ".txt");
			logger.error("Error prossecing idoc : " + idocNumber);
			logger.error("File contents : " + idocMessage);
		}
	}
	
	private void saveIdoc(String idocNumber, String idocMessage, String extension) {
		String savePath = prop.getProperty("sap.mom.dir.path");
		String fileName = "IDOC_" + idocNumber + "." + extension;
		File idocFile = new File(savePath + File.separator + fileName);
		try {
			Writer writer = new BufferedWriter(new FileWriter(idocFile));
			writer.write(idocMessage);
			writer.close();
			logger.info("creating file : " + savePath + File.separator + fileName );
			logger.info("file contents : " + idocMessage);
		} catch (IOException ioException) {
			logger.error("Error writing to file : " + fileName, ioException);
			new PlatformException("Error writing to file : " + fileName, ioException);
		}
	}
	
	protected boolean checkDuplicate(String idocXml) {
		boolean isDuplicate = true;
		if(idocXml.contains(uidTag)) {
			int startIndex = idocXml.indexOf(uidTag);
			startIndex += (uidTag.length() + 1);
			int endIndex = idocXml.indexOf("/" + uidTag);
			endIndex -= 1;
			String sapId = idocXml.substring(startIndex, endIndex);
			isDuplicate = sapUniqueIds.contains(sapId);
			if(!isDuplicate) {
				if(sapUniqueIds.size() >= 1000) {
					while(sapUniqueIds.size() > 900) {
						String removedSapId = sapUniqueIds.remove();
						logger.info("Sap unique ids reached : " + sapUniqueIds.size() + " , removed : " + removedSapId);
					}
				}
				logger.info("Inserting sap id : " + sapId);
				sapUniqueIds.add(sapId);
			} else {
				logger.info("Duplicate idoc entry : " + sapId);
			}
		}
		return isDuplicate;
	}

	public void setPlatformIDocHandlerFactory(PlatformIDocHandlerFactory platformIDocHandlerFactory) {
		this.platformIDocHandlerFactory = platformIDocHandlerFactory;
	}
}
