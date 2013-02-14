/**
 * 
 */
package com.fb.platform.sap.idoc.itemAck.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.fb.commons.mom.to.ItemTO;
import com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW;
import com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper;

/**
 * @author nehaga
 *
 */
public class ItemAckIdocMapperImpl implements ItemAckIdocMapper {

	/* (non-Javadoc)
	 * @see com.fb.platform.sap.idoc.itemAck.ItemAckIdocMapper#getItemAck(com.fb.platform.sap.idoc.generated.zatgflow.ZATGFLOW)
	 */
	public ItemTO updateItemAck(ZATGFLOW sapItemAck, ItemTO itemAck) {
		int year;
		int month;
		int day;
		
		if(hasValueChanged(itemAck.getSapDocumentId(), sapItemAck.getSOPOSNR())) {
			itemAck.setSapDocumentId(sapItemAck.getSOPOSNR());
		}
		
		if(hasValueChanged(itemAck.getAtgDocumentId(), sapItemAck.getPOSNN())) {
			itemAck.setAtgDocumentId(sapItemAck.getPOSNN());
		}
		
		if(hasValueChanged(itemAck.getUnitOfMeasurement(), sapItemAck.getMEINS())) {
			itemAck.setUnitOfMeasurement(sapItemAck.getMEINS());
		}
		
		if(hasValueChanged(itemAck.getOrderType(), sapItemAck.getAUART())) {
			itemAck.setOrderType(sapItemAck.getAUART());
		}
		
		List<String> shipComments = new ArrayList<String>();
		if(StringUtils.isNotBlank(sapItemAck.getGBSTK())) {
			shipComments.add(sapItemAck.getGBSTK());
		}
		if(StringUtils.isNotBlank(sapItemAck.getABSTK())) {
			shipComments.add(sapItemAck.getABSTK());
		}
		if(StringUtils.isNotBlank(sapItemAck.getLFGSK())) {
			shipComments.add(sapItemAck.getLFGSK());
		}
		if(StringUtils.isNotBlank(sapItemAck.getWBSTK())) {
			shipComments.add(sapItemAck.getWBSTK());
		}
		if(StringUtils.isNotBlank(sapItemAck.getFKSTK())) {
			shipComments.add(sapItemAck.getFKSTK());
		}
		
		if(hasValueChanged(itemAck.getShipmentComments(), StringUtils.join(shipComments , ","))) {
			itemAck.setShipmentComments(StringUtils.join(shipComments , ","));
		}
		
		itemAck.setHeader("ITEM_ACK");
		
		if(hasValueChanged(itemAck.getDeliveryNumber(), sapItemAck.getVBELN())) {
			itemAck.setDeliveryNumber(sapItemAck.getVBELN());
		}
		
		if(hasValueChanged(itemAck.getItemCategory(), sapItemAck.getPSTYV())) {
			itemAck.setItemCategory(sapItemAck.getPSTYV());
		}
		
		if(hasValueChanged(itemAck.getOrderId(), sapItemAck.getSOVBELN())) {
			itemAck.setOrderId(sapItemAck.getSOVBELN());
		}
		
		if(hasValueChanged(itemAck.getDeliveryType(), sapItemAck.getLFART())) {
			itemAck.setDeliveryType(sapItemAck.getLFART());
		}
		
		if(hasValueChanged(itemAck.getLspName(), sapItemAck.getLSPNAME())) {
			itemAck.setLspName(sapItemAck.getLSPNAME());
		}
		
		if(hasValueChanged(itemAck.getAwbNumber(), sapItemAck.getLSPNOR())) {
			itemAck.setAwbNumber(sapItemAck.getLSPNOR());
		}
		
		if(hasValueChanged(itemAck.getCreatedBy(), sapItemAck.getERNAMDEL())) {
			itemAck.setCreatedBy(sapItemAck.getERNAMDEL());
		}
		
		if(sapItemAck.getERDATDEL() != null && sapItemAck.getERDAT() != null && sapItemAck.getERDATDEL().length() == 8) {
			year = Integer.parseInt(sapItemAck.getERDATDEL().substring(0, 4));
			month = Integer.parseInt(sapItemAck.getERDATDEL().substring(4, 6));
			day = Integer.parseInt(sapItemAck.getERDATDEL().substring(6));
			itemAck.setCreatedDate(new DateTime(year, month, day, 0, 0));
		}
		
		if(hasValueChanged(itemAck.getSkuID(), Integer.toString(sapItemAck.getMATERIAL()))) {
			itemAck.setSkuID(Integer.toString(sapItemAck.getMATERIAL()));
		}
		
		if(hasValueChanged(itemAck.getLspUpdateDesc(), sapItemAck.getLSPUPDDESCR())) {
			itemAck.setLspUpdateDesc(sapItemAck.getLSPUPDDESCR());
		}
		
		if(hasValueChanged(itemAck.getPlantId(), sapItemAck.getSITE())) {
			itemAck.setPlantId(sapItemAck.getSITE());
		}
		
		if(hasValueChanged(itemAck.getItemState(), sapItemAck.getORDSTAT())) {
			itemAck.setItemState(sapItemAck.getORDSTAT());
		}
		
		if(hasValueChanged(itemAck.getQuantity(), sapItemAck.getRFMNG())) {
			itemAck.setQuantity(sapItemAck.getRFMNG());
		}
		
		if(hasValueChanged(itemAck.getOrderState(), sapItemAck.getVBTYPN())) {
			itemAck.setOrderState(sapItemAck.getVBTYPN());
		}
		
		if(sapItemAck.getERDAT() != null && sapItemAck.getERDAT() != null && sapItemAck.getERDAT().length() == 8) {
			year = Integer.parseInt(sapItemAck.getERDAT().substring(0, 4));
			month = Integer.parseInt(sapItemAck.getERDAT().substring(4, 6));
			day = Integer.parseInt(sapItemAck.getERDAT().substring(6));
			itemAck.setOrderDate(new DateTime(year, month, day, 0, 0));
			itemAck.setDeliveryDate(new DateTime(year, month, day, 0, 0));
		}
		return itemAck;
	}
	
	private boolean hasValueChanged(int oldValue, int newValue) {
		boolean hasChanged = true;
		if(newValue == oldValue) {
			hasChanged = false;
		}
		return hasChanged;
	}
	
	private boolean hasValueChanged(BigDecimal oldValue, BigDecimal newValue) {
		boolean hasChanged = true;
		if(newValue == null) {
			hasChanged = false;
		}
		return hasChanged;
	}
	
	private boolean hasValueChanged(String oldValue, String newValue) {
		boolean hasChanged = true;
		if(StringUtils.isBlank(newValue)) {
			hasChanged = false;
		}
		return hasChanged;
	}
}
