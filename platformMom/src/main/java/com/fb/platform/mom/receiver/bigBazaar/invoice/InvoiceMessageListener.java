/**
 * 
 */
package com.fb.platform.mom.receiver.bigBazaar.invoice;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.support.JmsUtils;

import com.fb.commons.PlatformException;
import com.fb.commons.mom.bigBazaar.to.InvoiceTO;
import com.fb.commons.mom.to.SapMomTO;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.impl.AbstractPlatformListener;
import com.fb.platform.mom.util.LoggerConstants;

/**
 * @author nehaga
 *
 */
public class InvoiceMessageListener extends AbstractPlatformListener implements MessageListener {
	
	private static Log infoLog = LogFactory.getLog(InvoiceMessageListener.class);
	
	private static Log auditLog = LogFactory.getLog(LoggerConstants.INVOICE_BB_AUDIT_LOG);
	
	@Override
	public void onMessage(Message message) {
		infoLog.info("Received the message for the Invoice destination.");

		try {
			long uid = message.getLongProperty(LoggerConstants.UID);
			String idocNumber = message.getStringProperty(LoggerConstants.IDOC_NO);
			String timestamp = message.getStringProperty(LoggerConstants.TIMESTAMP);

			auditLog.info(uid + "," + idocNumber + "," + timestamp + ",false");

			ObjectMessage objectMessage = (ObjectMessage) message;
			InvoiceTO invoice = (InvoiceTO) objectMessage.getObject();

			infoLog.info("Received the Invoice Message from SAP. \n" + invoice.toString());

			super.notify(invoice , PlatformDestinationEnum.INVOICE_BB);
			
			SapMomTO sapIdoc = invoice.getSapIdoc();
			auditLog.info(sapIdoc.getAckUID() + "," + sapIdoc.getIdocNumber() + "," + sapIdoc.getTimestamp() + ",true");
		} catch (JMSException e) {
			infoLog.error("Error in processing hornetQ invoice message.", e);
			throw JmsUtils.convertJmsAccessException(e);
		} catch (Exception e) {
			infoLog.error("Error in processing hornetQ invoice message.", e);
			throw new PlatformException(e);
			
		}
	}
}
