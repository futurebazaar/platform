package com.fb.platform.sap.bapi.table.mapper;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemPartnerMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, AddressTO addressTO, List<LineItemTO> lineItemTOList) {
		JCoTable orderPartner = bapiFunction.getTableParameterList().getTable(BapiTable.ORDER_PARTNERS.toString());
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiTable.ORDER_TEXT.toString());
		
		for (LineItemTO itemTO : lineItemTOList) {
			orderPartner.appendRow();
			orderText.appendRow();
			
			orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_WE);
			orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
			orderPartner.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			orderPartner.setValue( SapConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getLastName());
			orderPartner.setValue(SapConstants.LAST_NAME, addressTO.getLastName());
			orderPartner.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
			
			orderText.setValue(SapConstants.TEXT_LINE, addressTO.getAddress());
			orderPartner.setValue(SapConstants.CITY, addressTO.getCity());
			orderPartner.setValue(SapConstants.STATE, addressTO.getState());
			orderPartner.setValue(SapConstants.COUNTRY, addressTO.getCountry());
			orderPartner.setValue(SapConstants.PINCODE, addressTO.getPincode());
			orderPartner.setValue(SapConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
			orderPartner.setValue(SapConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
			
			orderText.setValue(SapConstants.TEXT_ID, SapConstants.FIRST_PARTNER_TEXT_ID);

			// Need to Check whether this condition should be set when empty?
			if (!StringUtils.isBlank(itemTO.getLspCode())) { // lsp
				orderPartner.appendRow();
				orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_LSP);
				orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, itemTO.getLspCode());
				orderPartner.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			}

			// Need to Check whether this condition should be set when empty?
			if (!StringUtils.isBlank(itemTO.getVendor())) { // vendor
				orderPartner.appendRow();
				orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_VENDOR);
				orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, itemTO.getVendor());
				orderPartner.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			}

			// Need to Check whether this condition should be set when empty?
			if (!StringUtils.isBlank(itemTO.getPayToOthers())) {
				orderPartner.setValue(SapConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
				orderText.setValue(SapConstants.TEXT_ID, SapConstants.THIRD_PARTNER_TEXT_ID);
				orderPartner.setValue( SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
				orderText.setValue(SapConstants.TEXT_LINE, itemTO.getPayToOthers());
			}
		}
	}
}
