/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import com.fb.commons.mom.to.CancelItemTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class CancelInvoiceIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO getItemAck(ZATGFLOW sapItemAck) {
		CancelItemTO cancelItem = (CancelItemTO) new ItemAckIdocMapperImpl().getItemAck(sapItemAck, new CancelItemTO());
		cancelItem.setCancelInvoiceNumber(sapItemAck.getVBELN());
		return cancelItem;
	}

}
