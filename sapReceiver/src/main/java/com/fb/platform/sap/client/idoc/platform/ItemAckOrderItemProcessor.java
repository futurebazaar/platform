/**
 * 
 */
package com.fb.platform.sap.client.idoc.platform;

import java.util.List;

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;

/**
 * @author nehaga
 *
 */
public interface ItemAckOrderItemProcessor {

	public List<ItemTO> getOrderItems(List<ZATGFLOW> ackList);
}
