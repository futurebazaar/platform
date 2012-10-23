/**
 * 
 */
package com.fb.platform.sap.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.fb.platform.mom.manager.PlatformDestinationEnum;

/**
 * Creates a unique incremental ID for each idoc type sent by sapReceiver to launcher's message listeners.
 * 
 * @author vinayak
 *
 */
public class AckUIDSequenceGenerator {

	private Map<PlatformDestinationEnum, AtomicLong> sequencerMap = new HashMap<PlatformDestinationEnum, AtomicLong>();

	/**
	 * Init method called by spring container
	 */
	public void init() {
		sequencerMap.put(PlatformDestinationEnum.INVENTORY, new AtomicLong());
		sequencerMap.put(PlatformDestinationEnum.ITEM_ACK, new AtomicLong());
		sequencerMap.put(PlatformDestinationEnum.DELIVERY_DELETE, new AtomicLong());
		sequencerMap.put(PlatformDestinationEnum.CORRUPT_IDOCS, new AtomicLong());
		sequencerMap.put(PlatformDestinationEnum.INVOICE_BB, new AtomicLong());
		sequencerMap.put(PlatformDestinationEnum.DELIVERY_BB, new AtomicLong());
		sequencerMap.put(PlatformDestinationEnum.DELIVERY_DELETE_BB, new AtomicLong());
	}

	public long getNextSequenceNumber(PlatformDestinationEnum destination) {
		AtomicLong sequence = sequencerMap.get(destination);
		return sequence.incrementAndGet();
	}
}
