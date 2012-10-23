/**
 * 
 */
package com.fb.platform.sap.client.idoc.sap.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.mom.bigBazaar.to.InvoiceHeaderTO;
import com.fb.commons.mom.bigBazaar.to.InvoiceTO;
import com.fb.commons.mom.to.MailTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.bigBazaar.invoice.InvoiceIdocHandler;

/**
 * @author nehaga
 *
 */

@ContextConfiguration(
		locations={"classpath:/test-applicationContext-resources.xml",
				"classpath:/platformMom-applicationContext-resources.xml",
				"classpath:/platformMom-applicationContext-service.xml",
				"classpath:/*platformMom-applicationContext-resources.xml",
				"classpath:/*platformMom-applicationContext-service.xml",
				"classpath:/test-sapReceiver-applicationContext.xml",
				"classpath:/sapReceiver-applicationContext-service.xml",
				"classpath:/*applicationContext.xml",
				"classpath:/*applicationContext-service.xml",
				"classpath*:/*applicationContext.xml",
				"classpath*:/*applicationContext-service.xml",
				"classpath*:/*platformMom-applicationContext-resources.xml",
				"classpath*:/*platformMom-applicationContext-service.xml",
				"classpath:**/test-applicationContext*.xml"})

public class InvoiceIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestInvoiceReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver  = new TestInvoiceReceiver();
	}
	
	@Test
	public void processInvoiceIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.INVOICE_BB, testReceiver);
		
		InputStream invoiceStream = InvoiceIdocHandlerTest.class.getClassLoader().getResourceAsStream("inv_create.xml");
		InvoiceIdocHandler invoiceIDocHandler = (InvoiceIdocHandler) platformIDocHandlerFactory.getHandler(InvoiceIdocHandler.INVOICE_IDOC_TYPE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(invoiceStream, sw);
		invoiceIDocHandler.handle(sw.toString());
	}
	
	private static class TestInvoiceReceiver implements PlatformMessageReceiver {

		private static int count = 1;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestInvoiceReceiver, received the invoice message, count is : " + count + ", message is : " + message);
			InvoiceTO invoiceTO = (InvoiceTO) message;

			if (count == 1) {
				assertEquals("0000040000686409", invoiceTO.getSapIdoc().getIdocNumber());
				InvoiceHeaderTO invoiceHeader = invoiceTO.getInvoiceHeader();
				assertEquals("INR", invoiceHeader.getCurrency());
				assertEquals("L", invoiceHeader.getBillingCategory());
				assertEquals("0155000090", invoiceHeader.getDocumentNumber());
				assertEquals("INVO", invoiceHeader.getDocumentType());
				assertEquals("INR", invoiceHeader.getLocalCurrency());
				assertEquals(1, invoiceHeader.getExchangeRate().getAmount().intValue());
				assertEquals("0038", invoiceHeader.getPaymentKey());
				assertEquals("KGM", invoiceHeader.getWeightUnit());
				assertEquals("LR", invoiceHeader.getInvoiceType());
				assertEquals("0005004700", invoiceHeader.getReceiptNum());
			} else if (count == 2) {
			} else if (count > 2) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestInvoiceReceiver Incremented count to : " + count);
		}
	}
}
