package com.fb.platform.sap.bapi.order.table.mapper;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.LineItemTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ItemPartnerMapper {
	
	private static Log logger = LogFactory.getLog(ItemPartnerMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, List<LineItemTO> lineItemTOList) {
		// No item partner level details required in SAP for Big Bazaar and hence skipping
		if (TinlaClient.valueOf(orderHeaderTO.getClient()).equals(TinlaClient.BIGBAZAAR)) {
			return;
		}
		logger.info("Setting Item Condition details for : " + orderHeaderTO.getReferenceID());
		JCoTable orderPartner = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_PARTNERS.toString());
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		for (LineItemTO itemTO : lineItemTOList) {
			orderPartner.appendRow();
			orderText.appendRow();
			AddressTO addressTO = itemTO.getAddressTO();
			orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_WE);
			orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
			orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			orderPartner.setValue( SapOrderConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getLastName());
			orderPartner.setValue(SapOrderConstants.LAST_NAME, addressTO.getLastName());
			orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
			orderText.setValue(SapOrderConstants.TEXT_LINE, addressTO.getAddress());
			orderPartner.setValue(SapOrderConstants.CITY, addressTO.getCity());
			orderPartner.setValue(SapOrderConstants.STATE, addressTO.getState());
			orderPartner.setValue(SapOrderConstants.COUNTRY, addressTO.getCountry());
			orderPartner.setValue(SapOrderConstants.PINCODE, addressTO.getPincode());
			orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
			orderPartner.setValue(SapOrderConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
			orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.FIRST_PARTNER_TEXT_ID);
			if (!StringUtils.isBlank(itemTO.getLspCode())) { // lsp
				logger.info("Setting LSP code: " + itemTO.getLspCode() + " for : " + orderHeaderTO.getReferenceID());
				orderPartner.appendRow();
				orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_LSP);
				orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, itemTO.getLspCode());
				orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			}
			if (!StringUtils.isBlank(itemTO.getVendor())) { // vendor
				logger.info("Setting Vendor: " + itemTO.getVendor() + " for : " + orderHeaderTO.getReferenceID());
				orderPartner.appendRow();
				orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_VENDOR);
				orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, itemTO.getVendor());
				orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
			}
			if (!StringUtils.isBlank(itemTO.getPayToOthers())) {
				logger.info("Setting pay to others: " + itemTO.getPayToOthers() + " for : " + orderHeaderTO.getReferenceID());
				orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, itemTO.getSapDocumentId());
				orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.THIRD_PARTNER_TEXT_ID);
				orderPartner.setValue( SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
				orderText.setValue(SapOrderConstants.TEXT_LINE, itemTO.getPayToOthers());
			}
		}
	}

}
