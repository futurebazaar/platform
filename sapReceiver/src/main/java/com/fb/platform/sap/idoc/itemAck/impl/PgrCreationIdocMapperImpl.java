/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemTO;
import com.fb.commons.mom.to.PgrCreationItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class PgrCreationIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO getItemAck(ZATGFLOW sapItemAck) {
		PgrCreationItemTO pgrCreationAck = (PgrCreationItemTO) new ItemAckIdocMapperImpl().getItemAck(sapItemAck, new PgrCreationItemTO());
		if(sapItemAck.getERDATDEL() != null && sapItemAck.getWADATPGR() != null && sapItemAck.getWADATPGR().length() == 8) {
			int year = Integer.valueOf(sapItemAck.getWADATPGR().substring(0, 4));
			int month = Integer.valueOf(sapItemAck.getWADATPGR().substring(4, 6));
			int day = Integer.valueOf(sapItemAck.getWADATPGR().substring(6));
			pgrCreationAck.setPgrCreationDate(new DateTime(year, month, day, 0, 0));
		}
		return pgrCreationAck;
	}

}
