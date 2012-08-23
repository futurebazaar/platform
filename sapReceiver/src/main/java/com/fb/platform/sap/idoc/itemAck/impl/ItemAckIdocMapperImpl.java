/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;

/**
 * @author nehaga
 *
 */
public class ItemAckIdocMapperImpl {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	public ItemTO getItemAck(ZATGFLOW sapItemAck, ItemTO itemAck) {
		int year;
		int month;
		int day;
		itemAck.setSapDocumentId(sapItemAck.getSOPOSNR());
		itemAck.setUnitOfMeasurement(sapItemAck.getMEINS());
		itemAck.setOrderType(sapItemAck.getAUART());
		itemAck.setHeader("ITEM_ACK");
		itemAck.setDeliveryNumber(sapItemAck.getVBELN());
		itemAck.setItemCategory(sapItemAck.getPSTYV());
		itemAck.setOrderId(sapItemAck.getSOVBELN());
		itemAck.setDeliveryType(sapItemAck.getLFART());
		itemAck.setLspName(sapItemAck.getLSPNAME());
		itemAck.setAwbNumber(sapItemAck.getLSPNOR());
		itemAck.setCreatedBy(sapItemAck.getERNAMDEL());
		if(sapItemAck.getERDATDEL() != null && sapItemAck.getERDAT() != null && sapItemAck.getERDATDEL().length() == 8) {
			year = Integer.parseInt(sapItemAck.getERDATDEL().substring(0, 4));
			month = Integer.parseInt(sapItemAck.getERDATDEL().substring(4, 6));
			day = Integer.parseInt(sapItemAck.getERDATDEL().substring(6));
			itemAck.setCreatedDate(new DateTime(year, month, day, 0, 0));
		}
		itemAck.setSkuID(sapItemAck.getMATNR());
		itemAck.setLspUpdateDesc(sapItemAck.getLSPUPDDESCR());
		itemAck.setPlantId(sapItemAck.getSITE());
		itemAck.setItemState(sapItemAck.getORDSTAT());
		itemAck.setQuantity(sapItemAck.getRFMNG());
		itemAck.setOrderState(sapItemAck.getVBTYPN());
		if(sapItemAck.getERDAT() != null && sapItemAck.getERDAT() != null && sapItemAck.getERDAT().length() == 8) {
			year = Integer.parseInt(sapItemAck.getERDAT().substring(0, 4));
			month = Integer.parseInt(sapItemAck.getERDAT().substring(4, 6));
			day = Integer.parseInt(sapItemAck.getERDAT().substring(6));
			itemAck.setOrderDate(new DateTime(year, month, day, 0, 0));
			itemAck.setDeliveryDate(new DateTime(year, month, day, 0, 0));
		}
		return itemAck;
	}

}
