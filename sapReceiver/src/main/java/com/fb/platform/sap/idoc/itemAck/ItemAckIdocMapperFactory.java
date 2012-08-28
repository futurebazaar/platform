/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.OrderStateEnum;
import com.fb.commons.mom.to.PrecedingOrderStateEnum;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
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
	
	public ItemTO getItemAck(ZATGFLOW sapItemAck, ItemTO orderItem) {
		OrderStateEnum orderState = OrderStateEnum.getInstance(sapItemAck.getVBTYPN());
		ItemTO itemAck = null;
		PrecedingOrderStateEnum precedingOrderState = null;
		if(StringUtils.isNotBlank(sapItemAck.getVBTYPV())) {
			precedingOrderState = PrecedingOrderStateEnum.valueOf(sapItemAck.getVBTYPV());
		}
		
		switch(orderState) {
			case H:
				itemAck = new ReturnOrderIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			case M:
				itemAck = new InvoiceIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			case N:
				itemAck = new CancelInvoiceIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			case O:
				if(precedingOrderState == PrecedingOrderStateEnum.T) {
					itemAck = new PgrCreationIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				} else {
					itemAck = new ReturnInvoiceIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				}
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			case R:
				if(precedingOrderState == PrecedingOrderStateEnum.J) {
					itemAck = new PgiCreationIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				} else if(precedingOrderState == PrecedingOrderStateEnum.T) {
					itemAck = new PgrCreationIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				} 
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			case T:
				itemAck = new ReturnDeliveryIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			case C:
				itemAck = new ItemAckIdocMapperImpl().updateItemAck(sapItemAck, orderItem);
				break;
			case J:
				/*
				 * delivery number comes in J state hence updating it in the map 
				 * so that all the future items receive C state data along with the delivery number.
				 */
				updateDeliveryNumber(sapItemAck, orderItem);
				itemAck = new ItemTO();
				itemAck.setItemTO(orderItem);
				itemAck = new ItemAckIdocMapperImpl().updateItemAck(sapItemAck, itemAck);
				itemAck.setOrderState(sapItemAck.getVBTYPN());
				break;
			default:
				itemAck = null;
				break;
		}
		return itemAck;
	}

	private void updateDeliveryNumber(ZATGFLOW sapItemAck, ItemTO orderItem) {
		orderItem.setDeliveryNumber(sapItemAck.getVBELN());
	}
}
