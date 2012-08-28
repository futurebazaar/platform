package com.fb.platform.sap.bapi.table.mapper;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.bapi.handler.PlatformBapiHandlerFactory;
import com.fb.platform.sap.bapi.table.BapiTable;
import com.fb.platform.sap.bapi.table.TinlaOrderType;
import com.fb.platform.sap.bapi.utils.SapConstants;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderPartnerMapper {
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, AddressTO addressTO, TinlaOrderType orderType) {
		
		JCoTable orderPartner = bapiFunction.getTableParameterList().getTable(PlatformBapiHandlerFactory.getPartnersTable(orderType).toString());
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiTable.ORDER_TEXT.toString());
		
		setFirstPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
		setSecondPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
		setThirdPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
		setFourthPartnerDetails(orderPartner, orderText, orderHeaderTO, addressTO);
	}

	private static void setFirstPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		
		orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_WE);
		orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getMiddleName()	+ " " + addressTO.getLastName());
		orderPartner.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapConstants.CITY, addressTO.getCity());
		orderPartner.setValue( SapConstants.STATE, addressTO.getState());
		orderPartner.setValue(SapConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue(SapConstants.SECONDARY_PHONE, addressTO.getSecondaryTelephone());
	
		orderText.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapConstants.TEXT_ID, SapConstants.FIRST_PARTNER_TEXT_ID);
		orderText.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapConstants.TEXT_LINE, addressTO.getAddress());
	
	}
	
	private static void setSecondPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		
		orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_AG);
		orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getMiddleName()	+ " " + addressTO.getLastName());
		orderPartner.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapConstants.CITY, addressTO.getCity());
		orderPartner.setValue(SapConstants.STATE, addressTO.getState());
		orderPartner.setValue(SapConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue( SapConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
		
		orderText.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapConstants.TEXT_ID, SapConstants.SECOND_PARTNER_TEXT_ID);
		orderText.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapConstants.TEXT_LINE, addressTO.getAddress());
	
	}
	private static void setThirdPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		
		orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_RE);
		orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getMiddleName()	+ " " + addressTO.getLastName());
		orderPartner.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapConstants.CITY, addressTO.getCity());
		orderPartner.setValue(SapConstants.STATE, addressTO.getState());
		orderPartner.setValue(SapConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue(SapConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
		
		orderText.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapConstants.TEXT_ID, SapConstants.THIRD_PARTNER_TEXT_ID);
		orderText.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapConstants.TEXT_LINE, addressTO.getAddress());
	}
	
	private static void setFourthPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO addressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		
		orderPartner.setValue(SapConstants.PARTNER_ROLE, SapConstants.PARTNER_ROLE_RG);
		orderPartner.setValue(SapConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapConstants.FULL_NAME, addressTO.getFirstName() + " " + addressTO.getMiddleName()	+ " " + addressTO.getLastName());
		orderPartner.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapConstants.CITY, addressTO.getCity());
		orderPartner.setValue(SapConstants.STATE, addressTO.getState());
		orderPartner.setValue( SapConstants.COUNTRY, addressTO.getCountry());
		orderPartner.setValue(SapConstants.PINCODE, addressTO.getPincode());
		orderPartner.setValue(SapConstants.PRIMARY_PHONE, addressTO.getPrimaryTelephone());
		orderPartner.setValue(SapConstants.FAX_NUMBER, addressTO.getSecondaryTelephone());
		
		orderText.setValue(SapConstants.ITEM_NUMBER, SapConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapConstants.TEXT_ID, SapConstants.FIRST_PARTNER_TEXT_ID);
		orderText.setValue(SapConstants.LANGUAGE, SapConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapConstants.TEXT_LINE, addressTO.getAddress());

	}
}
