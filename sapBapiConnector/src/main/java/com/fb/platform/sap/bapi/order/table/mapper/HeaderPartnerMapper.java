package com.fb.platform.sap.bapi.order.table.mapper;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.commons.SapOrderConstants;
import com.fb.platform.sap.bapi.commons.TinlaClient;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.fb.platform.sap.bapi.order.table.TinlaOrderType;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderPartnerMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, AddressTO addressTO, TinlaOrderType orderType) {
		JCoTable orderPartner = bapiFunction.getTableParameterList().getTable(BapiTableFactory.getPartnersTable(orderType, TinlaClient.valueOf(orderHeaderTO.getClient())).toString());
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		setFirstPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
		setSecondPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
		setThirdPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
		setFourthPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
	}

	private static void setFirstPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_WE);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, addressTO.getCity());
		orderPartner.setValue( SapOrderConstants.STATE, addressTO.getState());
		orderPartner.setValue(SapOrderConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue(SapOrderConstants.SECONDARY_PHONE, addressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.FIRST_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, addressTO.getAddress());
	}
	
	private static void setSecondPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_AG);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, addressTO.getCity());
		orderPartner.setValue(SapOrderConstants.STATE, addressTO.getState());
		orderPartner.setValue(SapOrderConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue( SapOrderConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.SECOND_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, addressTO.getAddress());
	}
	private static void setThirdPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_RE);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, addressTO.getCity());
		orderPartner.setValue(SapOrderConstants.STATE, addressTO.getState());
		orderPartner.setValue(SapOrderConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue(SapOrderConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.THIRD_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, addressTO.getAddress());
	}
	
	private static void setFourthPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_RG);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, addressTO.getCity());
		orderPartner.setValue(SapOrderConstants.STATE, addressTO.getState());
		orderPartner.setValue( SapOrderConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue(SapOrderConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.FIRST_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, addressTO.getAddress());
	}
}
