package com.fb.platform.sap.bapi.order.table.mapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.mom.to.AddressTO;
import com.fb.commons.mom.to.OrderHeaderTO;
import com.fb.platform.sap.client.commons.SapOrderConstants;
import com.fb.platform.sap.client.commons.TinlaClient;
import com.fb.platform.sap.bapi.factory.BapiTableFactory;
import com.fb.platform.sap.bapi.order.TinlaOrderType;
import com.fb.platform.sap.bapi.order.table.BapiOrderTable;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class HeaderPartnerMapper {
	
	private static Log logger = LogFactory.getLog(HeaderPartnerMapper.class);
	
	public static void setDetails(JCoFunction bapiFunction, OrderHeaderTO orderHeaderTO, AddressTO billingAddressTO, TinlaOrderType orderType) {
		logger.info("Setting Header Partner details for : " + orderType + " " + orderHeaderTO.getReferenceID());
		logger.info("partner Address details are : " + billingAddressTO);
		JCoTable orderPartner = bapiFunction.getTableParameterList().getTable(BapiTableFactory.getPartnersTable(orderType, TinlaClient.valueOf(orderHeaderTO.getClient())).toString());
		JCoTable orderText = bapiFunction.getTableParameterList().getTable(BapiOrderTable.ORDER_TEXT.toString());
		setFirstPartnerDetails(orderPartner, orderText, orderHeaderTO, billingAddressTO);
		setSecondPartnerDetails(orderPartner, orderText, orderHeaderTO, billingAddressTO);
		setThirdPartnerDetails(orderPartner, orderText, orderHeaderTO, billingAddressTO);
		setFourthPartnerDetails(orderPartner, orderText, orderHeaderTO, billingAddressTO);
	}

	private static void setFirstPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO billingAddressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_WE);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, billingAddressTO.getFirstName() + " " + billingAddressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, billingAddressTO.getCity());
		orderPartner.setValue( SapOrderConstants.STATE, billingAddressTO.getState());
		orderPartner.setValue(SapOrderConstants.COUNTRY, billingAddressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, billingAddressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, billingAddressTO.getPrimaryTelephone());
		orderPartner.setValue(SapOrderConstants.SECONDARY_PHONE, billingAddressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.FIRST_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, billingAddressTO.getAddress());
	}
	
	private static void setSecondPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO billingAddressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_AG);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, billingAddressTO.getFirstName() + " " + billingAddressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, billingAddressTO.getCity());
		orderPartner.setValue(SapOrderConstants.STATE, billingAddressTO.getState());
		orderPartner.setValue(SapOrderConstants.COUNTRY, billingAddressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, billingAddressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, billingAddressTO.getPrimaryTelephone());
		orderPartner.setValue( SapOrderConstants.FAX_NUMBER, billingAddressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.SECOND_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, billingAddressTO.getAddress());
	}
	private static void setThirdPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO billingAddressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_RE);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, billingAddressTO.getFirstName() + " " + billingAddressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, billingAddressTO.getCity());
		orderPartner.setValue(SapOrderConstants.STATE, billingAddressTO.getState());
		orderPartner.setValue(SapOrderConstants.COUNTRY, billingAddressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, billingAddressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, billingAddressTO.getPrimaryTelephone());
		orderPartner.setValue(SapOrderConstants.FAX_NUMBER, billingAddressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.THIRD_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, billingAddressTO.getAddress());
	}
	
	private static void setFourthPartnerDetails(JCoTable orderPartner,JCoTable orderText,  OrderHeaderTO orderHeaderTO, AddressTO billingAddressTO) {
		orderPartner.appendRow();
		orderText.appendRow();
		orderPartner.setValue(SapOrderConstants.PARTNER_ROLE, SapOrderConstants.PARTNER_ROLE_RG);
		orderPartner.setValue(SapOrderConstants.ACCOUNT_NUMBER, orderHeaderTO.getAccountNumber());
		orderPartner.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderPartner.setValue(SapOrderConstants.FULL_NAME, billingAddressTO.getFirstName() + " " + billingAddressTO.getLastName());
		orderPartner.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderPartner.setValue(SapOrderConstants.CITY, billingAddressTO.getCity());
		orderPartner.setValue(SapOrderConstants.STATE, billingAddressTO.getState());
		orderPartner.setValue( SapOrderConstants.COUNTRY, billingAddressTO.getCountry());
		orderPartner.setValue(SapOrderConstants.PINCODE, billingAddressTO.getPincode());
		orderPartner.setValue(SapOrderConstants.PRIMARY_PHONE, billingAddressTO.getPrimaryTelephone());
		orderPartner.setValue(SapOrderConstants.FAX_NUMBER, billingAddressTO.getSecondaryTelephone());
		orderText.setValue(SapOrderConstants.ITEM_NUMBER, SapOrderConstants.DEFAULT_ITEM_NUMER);
		orderText.setValue(SapOrderConstants.TEXT_ID, SapOrderConstants.FIRST_PARTNER_TEXT_ID);
		orderText.setValue(SapOrderConstants.LANGUAGE, SapOrderConstants.DEFAULT_LANGUAGE);
		orderText.setValue(SapOrderConstants.TEXT_LINE, billingAddressTO.getAddress());
	}
}
