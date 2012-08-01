package com.fb.platform.sap.idoc.itemAck;

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;

/**
 * @author nehaga
 *
 */
public interface ItemAckIdocMapper {
	public ItemTO getItemAck(ZATGFLOW sapItemAck);
}
