/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform.bigbzaar.deliveryDelete;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.mom.bigBazaar.to.DeliveryAdditionalHeaderTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryControlTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryHeaderTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryItemTO;
import com.fb.commons.mom.bigBazaar.to.DeliveryTO;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.MomManager;
import com.fb.platform.mom.manager.PlatformDestinationEnum;
import com.fb.platform.mom.manager.PlatformMessageReceiver;
import com.fb.platform.sap.client.idoc.platform.PlatformIDocHandlerFactory;
import com.fb.platform.sap.client.idoc.platform.bigBazaar.delivery.DeliveryIdocHandler;

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

public class DeliveryIdocHandlerTest extends BaseTestCase {
	
	@Autowired
	private MomManager momManager = null;
	
	@Autowired
	private PlatformIDocHandlerFactory platformIDocHandlerFactory = null;
	
	private TestDeliveryReceiver testReceiver = null;
	
	@Before
	public void init() {
		testReceiver  = new TestDeliveryReceiver();
	}
	
	@Test
	public void processDeliveryDeleteIdoc() throws IOException ,Exception {
		momManager.registerReceiver(PlatformDestinationEnum.DELIVERY_DELETE_BB, testReceiver);
		
		InputStream deliveryStream = DeliveryIdocHandlerTest.class.getClassLoader().getResourceAsStream("delvCreateBB.xml");
		DeliveryIdocHandler deliveryIDocHandler = (DeliveryIdocHandler) platformIDocHandlerFactory.getHandler(DeliveryIdocHandler.DELIVERY_IDOC_TYPE);
		StringWriter sw = new StringWriter();
		IOUtils.copy(deliveryStream, sw);
		deliveryIDocHandler.handle(sw.toString());
	}
	
	private static class TestDeliveryReceiver implements PlatformMessageReceiver {

		private static int count = 1;

		/* (non-Javadoc)
		 * @see com.fb.platform.mom.manager.PlatformMessageReceiver#handleMessage(java.lang.Object)
		 */
		@Override
		public void handleMessage(Object message) {
			System.out.println("TestDeliveryReceiver, received the Delivery message, count is : " + count + ", message is : " + message);
			DeliveryTO deliveryTO = (DeliveryTO) message;
			if (count == 1) {
				assertEquals("0000040000687309", deliveryTO.getSapIdoc().getIdocNumber());
				
				DeliveryHeaderTO deliveryHeaderTO = deliveryTO.getDeliveryHeaderTO();
				assertEquals("0145000435", deliveryHeaderTO.getDeliveryNumber());
				assertEquals(4608, deliveryHeaderTO.getReceivingPoint());
				assertEquals(117, deliveryHeaderTO.getSalesOrganization());
				assertEquals("ZPT", deliveryHeaderTO.getWarehouseRef());
				assertEquals(1, deliveryHeaderTO.getShippingConditions());
				assertEquals(19200, deliveryHeaderTO.getTotalWeight().intValue());
				assertEquals(0, deliveryHeaderTO.getNetWeight().intValue());
				assertEquals(0, deliveryHeaderTO.getVolume().intValue());
				assertEquals(0, deliveryHeaderTO.getPackageCount());
				
				DeliveryAdditionalHeaderTO deliveryAdditionalHeaderTO = deliveryHeaderTO.getDeliveryAdditionalHeaderTO();
				assertEquals("ZFBD", deliveryAdditionalHeaderTO.getDeliveryType());
				assertEquals(0, deliveryAdditionalHeaderTO.getDeliveryPriority());
				assertEquals(1, deliveryAdditionalHeaderTO.getTransportationGroup());
				
				DeliveryControlTO deliveryControlTO = deliveryHeaderTO.getDeliveryControlTO();
				assertEquals("ORI", deliveryControlTO.getQualifier());
				
				List<DeliveryItemTO> deliveryItemList = deliveryHeaderTO.getDeliveryItemList();
				assertEquals(1, deliveryItemList.size());
				
				DeliveryItemTO deliveryItem = deliveryItemList.get(0);
				assertEquals("I000000901", deliveryItem.getOrderNumber());
				assertEquals(10, deliveryItem.getItemNumber());
				assertEquals("000000000100000002", deliveryItem.getArticleNumber());
				assertEquals("000000000100000002", deliveryItem.getArticleEntered());
				assertEquals("TEST", deliveryItem.getSalesDesc());
				
			} else if (count > 1) {
				throw new IllegalArgumentException("Invalid message");
			}
			
			count++;
			System.out.println("TestDeliveryDeleteReceiver Incremented count to : " + count);
		}
	}
}
