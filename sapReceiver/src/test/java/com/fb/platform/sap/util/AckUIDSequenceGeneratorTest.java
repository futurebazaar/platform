/**
 * 
 */
package com.fb.platform.sap.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author vinayak
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
public class AckUIDSequenceGeneratorTest extends BaseTestCase {

	@Autowired
	private AckUIDSequenceGenerator ackUIDSequenceGenerator = null;

	@Test
	public void nextInventorySequence() {
		long sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.INVENTORY);
		assertTrue(sequenceNumber > 0);

		long newSequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.INVENTORY);
		assertEquals(newSequenceNumber, sequenceNumber + 1);
	}

	@Test
	public void nextItemAckSequence() {
		long sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.ITEM_ACK);
		assertTrue(sequenceNumber > 0);

		long newSequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.ITEM_ACK);
		assertEquals(newSequenceNumber, sequenceNumber + 1);
	}

	@Test
	public void nextDeliveryDeleteSequence() {
		long sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY_DELETE);
		assertTrue(sequenceNumber > 0);

		long newSequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY_DELETE);
		assertEquals(newSequenceNumber, sequenceNumber + 1);
	}
}
