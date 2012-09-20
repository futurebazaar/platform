/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.ReturnDeliveryTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class ReturnDeliveryIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO updateItemAck(ZATGFLOW sapItemAck, ItemTO itemAck) {
		ReturnDeliveryTO returnDelivery = new ReturnDeliveryTO();
		returnDelivery.setItemTO(itemAck);
		if(sapItemAck.getERDATDEL() != null && sapItemAck.getERDATDEL().length() == 8) {
			int year = Integer.valueOf(sapItemAck.getERDATDEL().substring(0, 4));
			int month = Integer.valueOf(sapItemAck.getERDATDEL().substring(4, 6));
			int day = Integer.valueOf(sapItemAck.getERDATDEL().substring(6));
			returnDelivery.setReturnCreatedDate(new DateTime(year, month, day, 0, 0));
		}
		returnDelivery.setNumber(sapItemAck.getVBELN());
		returnDelivery.setReturnId(sapItemAck.getREVBELN());
		returnDelivery.setType(sapItemAck.getLFART());
		returnDelivery.setReturnCreatedBy(sapItemAck.getERNAMDEL());
		return returnDelivery;
	}

}
