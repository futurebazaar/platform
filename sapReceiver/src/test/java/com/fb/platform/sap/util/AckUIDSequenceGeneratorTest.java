/**
 * 
 */
package com.fb.platform.sap.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * @author vinayak
 *
 */
public class AckUIDSequenceGeneratorTest extends BaseTestCase {

	@Autowired
	private AckUIDSequenceGenerator ackUIDSequenceGenerator = null;

	@Test
	public void nextInventorySequence() {
		long sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.INVENTORY);
		assertEquals(1, sequenceNumber);

		sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.INVENTORY);
		assertEquals(2, sequenceNumber);
	}

	@Test
	public void nextItemAckSequence() {
		long sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.ITEM_ACK);
		assertEquals(1, sequenceNumber);

		sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.ITEM_ACK);
		assertEquals(2, sequenceNumber);
	}

	@Test
	public void nextDeliveryDeleteSequence() {
		long sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY_DELETE);
		assertEquals(1, sequenceNumber);

		sequenceNumber = ackUIDSequenceGenerator.getNextSequenceNumber(PlatformDestinationEnum.DELIVERY_DELETE);
		assertEquals(2, sequenceNumber);
	}
}
