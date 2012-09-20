/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.ReturnInvoiceTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class ReturnInvoiceIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO updateItemAck(ZATGFLOW sapItemAck, ItemTO itemAck) {
		ReturnInvoiceTO returnInvoice = new ReturnInvoiceTO();
		returnInvoice.setItemTO(itemAck);
		if(sapItemAck.getERDATDEL() != null && sapItemAck.getFKDAT() != null && sapItemAck.getFKDAT().length() == 8) {
			int year = Integer.valueOf(sapItemAck.getFKDAT().substring(0, 4));
			int month = Integer.valueOf(sapItemAck.getFKDAT().substring(4, 6));
			int day = Integer.valueOf(sapItemAck.getFKDAT().substring(6));
			returnInvoice.setInvoiceDate(new DateTime(year, month, day, 0, 0));
		}
		returnInvoice.setNumber(sapItemAck.getVBELN());
		returnInvoice.setReturnId(sapItemAck.getREVBELN());
		returnInvoice.setType(sapItemAck.getLFART());
		returnInvoice.setInvoiceNet(sapItemAck.getNETWR());
		return returnInvoice;
	}

}
