/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.ReturnOrderTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class ReturnOrderIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO getItemAck(ZATGFLOW sapItemAck) {
		ReturnOrderTO returnOrder = (ReturnOrderTO) new ItemAckIdocMapperImpl().getItemAck(sapItemAck, new ReturnOrderTO());
		returnOrder.setReturnOrderId(sapItemAck.getVBELN());
		returnOrder.setReturnQuantity(sapItemAck.getRFMNG());
		returnOrder.setStorageLocation(sapItemAck.getLGORT());
		returnOrder.setCategory(sapItemAck.getPSTYV());
		return returnOrder;
	}

}
