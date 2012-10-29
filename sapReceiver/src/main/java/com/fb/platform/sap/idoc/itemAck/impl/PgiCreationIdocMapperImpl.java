/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgiCreationItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class PgiCreationIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO updateItemAck(ZATGFLOW sapItemAck, ItemTO itemAck) {
		PgiCreationItemTO pgiCreationAck = new PgiCreationItemTO();
		pgiCreationAck.setItemTO(itemAck);
		if(sapItemAck.getERDATDEL() != null && sapItemAck.getWADATPGI() != null && sapItemAck.getWADATPGI().length() == 8) {
			int year = Integer.valueOf(sapItemAck.getWADATPGI().substring(0, 4));
			int month = Integer.valueOf(sapItemAck.getWADATPGI().substring(4, 6));
			int day = Integer.valueOf(sapItemAck.getWADATPGI().substring(6));
			pgiCreationAck.setPgiCreationDate(new DateTime(year, month, day, 0, 0));
		}
		return pgiCreationAck;
	}

}