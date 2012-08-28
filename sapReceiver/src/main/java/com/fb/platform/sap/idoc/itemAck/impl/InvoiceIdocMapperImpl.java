/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemInvoiceTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class InvoiceIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO updateItemAck(ZATGFLOW sapItemAck, ItemTO itemAck) {
		ItemInvoiceTO itemInvoice = new ItemInvoiceTO();
		itemInvoice.setItemTO(itemAck);
		if(sapItemAck.getERDAT()!= null && sapItemAck.getERDAT().length() == 8) {
			int year = Integer.valueOf(sapItemAck.getERDAT().substring(0, 4));
			int month = Integer.valueOf(sapItemAck.getERDAT().substring(4, 6));
			int day = Integer.valueOf(sapItemAck.getERDAT().substring(6));
			itemInvoice.setInvoiceDate(new DateTime(year, month, day, 0, 0));
		}
		itemInvoice.setInvoiceNumber(sapItemAck.getVBELN());
		if(sapItemAck.getFKDAT() != null) {
			itemInvoice.setBillingType(sapItemAck.getFKART());
		}
		return itemInvoice;
	}

}
