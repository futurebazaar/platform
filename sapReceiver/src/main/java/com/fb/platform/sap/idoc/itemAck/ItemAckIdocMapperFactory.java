/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck;

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.impl.AtgDocumentIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.CancelInvoiceIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.InvoiceIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.ItemAckIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.PgiCreationIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.PgrCreationIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.ReturnDeliveryIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.ReturnInvoiceIdocMapperImpl;
import com.fb.platform.sap.idoc.itemAck.impl.ReturnOrderIdocMapperImpl;

/**
 * @author nehaga
 *
 */
public class ItemAckIdocMapperFactory {
	
	public ItemTO getItemAck(ZATGFLOW sapItemAck) {
		ItemTO itemAck = null;
		if(sapItemAck.getVBTYPN().equalsIgnoreCase("C")) {
			itemAck = new AtgDocumentIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("O") && sapItemAck.getVBTYPV().equalsIgnoreCase("T")) {
			itemAck = new PgrCreationIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("R") && sapItemAck.getVBTYPV().equalsIgnoreCase("J")) {
			itemAck = new PgiCreationIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("M")) {
			itemAck = new InvoiceIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("N")) {
			itemAck = new CancelInvoiceIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("H")) {
			itemAck = new ReturnOrderIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("T")) {
			itemAck = new ReturnDeliveryIdocMapperImpl().getItemAck(sapItemAck);
		} else if (sapItemAck.getVBTYPN().equalsIgnoreCase("O")) {
			itemAck = new ReturnInvoiceIdocMapperImpl().getItemAck(sapItemAck);
		} else {
			itemAck = new ItemAckIdocMapperImpl().getItemAck(sapItemAck, new ItemTO());
		}
		return itemAck;
	}
}
