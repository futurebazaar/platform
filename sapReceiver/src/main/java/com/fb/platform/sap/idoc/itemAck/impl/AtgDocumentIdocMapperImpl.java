/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import com.fb.commons.mom.to.AtgDocumentItemTO;
import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class AtgDocumentIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	@Override
	public ItemTO getItemAck(ZATGFLOW sapItemAck) {
		AtgDocumentItemTO atgDocument = (AtgDocumentItemTO) new ItemAckIdocMapperImpl().getItemAck(sapItemAck, new AtgDocumentItemTO());
		atgDocument.setAtgDocumentId(sapItemAck.getPOSNN());
		return atgDocument;
	}

}
