/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.mom.to.OrderStateEnum;
import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PrecedingOrderStateEnum;
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
 * J
 * Q
 *
 */
public class ItemAckIdocMapperFactory {
	
	public ItemTO getItemAck(ZATGFLOW sapItemAck) {
		ItemTO itemAck = null;
		OrderStateEnum orderState = null;
		if(StringUtils.isNotBlank(sapItemAck.getVBTYPN()) && !sapItemAck.getVBTYPN().trim().equals("+")) {
			orderState = OrderStateEnum.valueOf(sapItemAck.getVBTYPN());
		}
		
		PrecedingOrderStateEnum precedingOrderState = null;
		if(StringUtils.isNotBlank(sapItemAck.getVBTYPV())) {
			precedingOrderState = PrecedingOrderStateEnum.valueOf(sapItemAck.getVBTYPV());
		}
		
		switch(orderState) {
			case C:
				itemAck = new AtgDocumentIdocMapperImpl().getItemAck(sapItemAck);
				break;
			case O:
				if(precedingOrderState == PrecedingOrderStateEnum.T) {
					itemAck = new PgrCreationIdocMapperImpl().getItemAck(sapItemAck);
				} else {
					itemAck = new ReturnInvoiceIdocMapperImpl().getItemAck(sapItemAck);
				}
				break;
			case R:
				if(precedingOrderState == PrecedingOrderStateEnum.J) {
					itemAck = new PgiCreationIdocMapperImpl().getItemAck(sapItemAck);
				} else if(precedingOrderState == PrecedingOrderStateEnum.T) {
					itemAck = new PgrCreationIdocMapperImpl().getItemAck(sapItemAck);
				} 
				break;
			case M:
				itemAck = new InvoiceIdocMapperImpl().getItemAck(sapItemAck);
				break;
			case N:
				itemAck = new CancelInvoiceIdocMapperImpl().getItemAck(sapItemAck);
				break;
			case H:
				itemAck = new ReturnOrderIdocMapperImpl().getItemAck(sapItemAck);
				break;
			case T:
				itemAck = new ReturnDeliveryIdocMapperImpl().getItemAck(sapItemAck);
				break;
			default :
				itemAck = new ItemAckIdocMapperImpl().getItemAck(sapItemAck, new ItemTO());
				break;
		}
			
		return itemAck;
	}
}
