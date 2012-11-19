package com.fb.platform.sap.client.commons;

public class SapOrderConstants {

	
	//Common Constants
	public static final String OPERATION_FLAG = "UPDATEFLAG";
	public static final String DESCRIPTION = "SHORT_TEXT";
	
	//6 Digitis
	public static final String REFERENCE_FIELD = "REF_1";
	public static final String HEADER_LSP = "REF_1_S";
	public static final String DEFAULT_HEADER_LSP = "FLSL";
	
	//Character Constants
	public static final String CHAR_A = "A";
	public static final String CANCEL_FLAG = "C";
	public static final String CHAR_D = "D";
	public static final String REFERENCE_DOCUMENT_FLAG = "H";
	public static final String INSERT_FLAG = "I";
	public static final String CHAR_R = "R";
	public static final String SUCCESS_FLAG ="S";
	public static final String UPDATE_FLAG ="U";
	public static final String WARNING_FLAG = "W";
	public static final String COMMIT_FLAG  = "X";
	public static final String ERROR_FLAG = "E";
	public static final String CHAR_Z = "Z";
	public static final String CHANGE_FLAG = "L";
	

	//Language
	public static final String LANGUAGE = "LANGU";
	public static final String DEFAULT_LANGUAGE = "EN";
	
	//Numeric Constants
	public static final String  FBIL_SALES_ORGANIZATION = "0103";
	public static final String  BBIL_SALES_ORGANIZATION = "0117";
	public static final String  DEFAULT_DISTRIBUTION_CHANNEL = "01";
	public static final String  DEFAULT_DIVISION = "00";
	
	// Item Level Constants
	public static final String DEFAULT_ITEM_NUMER = "000000";
	public static final String ITEM_NUMBER = "ITM_NUMBER";
	public static final String ARTICLE_ID = "MATERIAL";
	public static final String ITEM_CATEGORY = "ITEM_CATEG";
	public static final String DEFAULT_FB_ITEM_CATEGORY = "ZATX";
	public static final String DEFAULT_BB_ITEM_CATEGORY = "ZATN";
	public static final String WARRANTY_ITEM_CATEGORY = "ZFBE";
	public static final String GV_ITEM_CATEGORY = "ZFBS";
	public static final String RELATIONSHIP_ARTICLE_ID = "PURCH_NO_S";
	public static final String REJECTION_REASON = "REASON_REJ";
	public static final String CHANGE_TYPE = "CHANGE_TYPE";
	public static final String QUANTITY = "TARGET_QTY";
	public static final String REQUIRED_QUANTITY = "REQ_QTY";
	public static final String UPDATE_INDICATOR = "UPD_INDICATOR";
	
	//Item Schedule Constants
	public static final String SCHEDULE_LINE = "SCHED_LINE";
	public static final String DEFAULT_SCHEDULE_LINE = "0001";
	
	// Customer Account Constants
	public static final String TEXT_ID = "TEXT_ID";
	public static final String TEXT_LINE = "TEXT_LINE";
	public static final String PAYBACK_TEXT_ID = "Z006";
	public static final String CHEQUE_TEXT_ID = "Z004";
	public static final String FIRST_PARTNER_TEXT_ID = "Z001";
	public static final String SECOND_PARTNER_TEXT_ID = "Z002";
	public static final String THIRD_PARTNER_TEXT_ID = "Z003";
	public static final String BUNDLE_TEXT_ID = "Z004";
	public static final String ACCOUNT_NUMBER = "PARTN_NUMB";
	public static final String PARTNER_ROLE = "PARTN_ROLE";
	public static final String DEFAULT_ACCOUNT_NUMBER = "21000000103";
	public static final String COD_ACCOUNT_NUMBER = "21000000104";
	public static final String CUSTOMER_GROUP = "CUST_GROUP";
	
	public static final String FB_CUSTOMER_GROUP = "Z3";	
	public static final String BB_CUSTOMER_GROUP = "";
	public static final String PARTNER_ROLE_WE = "WE";
	public static final String PARTNER_ROLE_AG = "AG";
	public static final String PARTNER_ROLE_RE = "RE";
	public static final String PARTNER_ROLE_RG = "RG";
	public static final String PARTNER_ROLE_LSP = "FA";
	public static final String PARTNER_ROLE_VENDOR = "LF";
	
	//Payments Constants
	public static final String COD_PAYMENT_TERM = "0038";
	public static final String DEFAULT_PAYMENT_TERM = "0001";
	public static final String PAYMENT_TERM = "PMNTTRMS";
	public static final String CURRENCY = "CURRENCY";
	public static final String DEFAULT_CURRENCY = "INR";
	public static final String INSTRUMENT_NO = "CC_NUMBER";
	public static final String PAYMENT_DATE = "AUTH_DATE";
	public static final String PAYMENT_TIME = "AUTH_TIME";
	public static final String VALID_TILL = "CC_VALID_T";
	public static final String GATEWAY = "CC_TYPE";
	public static final String AMOUNT = "BILLAMOUNT";
	public static final String PG_TRANSACTION_ID = "AUTH_CC_NO";
	public static final String AUTHORIZED_AMOUNT = "AUTHAMOUNT";
	public static final String BILLING_AMOUNT = "BILLAMOUNT";
	public static final String CARD_HOLDER_NAME = "CC_NAME";
	public static final String BILLING_DATE = "BILL_DATE";
	public static final String AUTH_REFERENCE_NO = "AUTH_REFNO";
	
	/******************** NEED TO ASK FOR THESE ********************************/
	public static final String AUTH_FLAG = "AUTH_FLAG";
	public static final String CC_REACT = "CC_REACT";
	public static final String CC_STAT_EX = "CC_STAT_EX";
	public static final String CC_REACT_T = "CC_REACT_T";
	public static final String MERCHANT_ID = "MERCHIDCL";
	public static final String RADRCHECK1 = "RADRCHECK1";
	public static final String RADRCHECK2 = "RADRCHECK2";
	public static final String RADRCHECK3 = "RADRCHECK3";
	public static final String RCARDCHECK = "RCARDCHECK";
	
	
	//Header Constants
	public static final String INPUT_SALES_DOCUMENT = "SALESDOCUMENTIN";	
	public static final String SALES_DOCUMENT = "SALESDOCUMENT";
	public static final String RETURN_REFERENCE= "REFERENCE";
	public static final String RETURN_ORDER= "RETURNORDER";
	public static final String CHANNEL_TYPE = "NAME";
	public static final String CLIENT_NAME = "NAME";
	public static final String THIRD_PARTY_ORDER = "PURCH_NO_C";
	public static final String DOCUMENT_DATE = "DOC_DATE";
	public static final String DISTRIBUTION_CHANNEL = "DISTR_CHAN";
	public static final String SALES_ORGANIZATION = "SALES_ORG";
	public static final String DIVISION = "DIVISION";
	public static final String REFERENCE_DOCUMENT_CATEGORY = "REFDOC_CAT";
	public static final String REFERENCE_DOCUMENT = "REF_DOC";
	public static final String ORDER_REASON = "ORD_REASON";
	public static final String HEADER_PLANT = "SALES_OFF";
	
	//Sap Condition Types
	public static final String CONDITION_TYPE = "COND_TYPE";
	public static final String CONDITION_VALUE = "COND_VALUE";
	public static final String DISCOUNT_CONDITION_TYPE = "YB01";
	public static final String ITEM_DISCOUNT_CONDITION_TYPE = "YB02";
	public static final String COUPON_CONDITION_TYPE = "YB03";
	public static final String SHIPPING_CONDITION_TYPE = "YF02";
	public static final String WARRANTY_CONDITION_TYPE = "ZWAF";
	public static final String ITZ_CONDITION_TYPE = "YITZ";
	public static final String MRP_CONDITION_TYPE = "YRMP";
	public static final String OFFER_PRICE_CONDITION_TYPE = "YFBI";
	public static final String SALES_PRICE_CONDITION_TYPE = "YR00";
	public static final String GV_CONDITION_TYPE = "YFGV";
	public static final String NLC_CONDITION_TYPE = "YF05";
	public static final String CONDITION_STEP_NUMBER = "COND_ST_NO";
	public static final String CONDITION_COUNTER = "COND_COUNT";
	public static final String DEFAULT_CONDITION_COUNTER = "01";
	
	public static final String BB_SHIPPING_CONDITION_TYPE = "ZFSH";
	public static final String BB_SHIPPING_STEP_NUMBER = "133";
	
	//Condition Counter
	public static final String MRP_STEP_NUMBER = "620";
	public static final String LIST_STEP_NUMBER = "20";
	public static final String SALES_STEP_NUMBER = "30";
	public static final String ITZ_STEP_NUMBER = "750";
	public static final String SHIPPING_STEP_NUMBER = "133";
	public static final String COUPON_STEP_NUMBER = "770";
	public static final String ITEM_DISCOUNT_STEP_NUMBER = "140";
	public static final String NLC_STEP_NUMBER = "421";
	public static final String WARRANTY_STEP_NUMBER = "810";
	
	//Sap Document Types
	public static final String DOCUMENT_TYPE = "DOC_TYPE"; 
	public static final String SALES_ORDER_TYPE = "ZATG";
	public static final String RETURN_ORDER_TYPE = "ZRAT"; 
	
	//Sap Address Constants
	public static final String FULL_NAME = "NAME";
	public static final String LAST_NAME = "NAME_2";
	public static final String CITY = "CITY";
	public static final String STATE = "REGION";
	public static final String PINCODE = "POSTL_CODE";
	public static final String COUNTRY = "COUNTRY";
	public static final String PRIMARY_PHONE = "TELEPHONE";
	public static final String SECONDARY_PHONE = "TELEPHONE2";
	public static final String FAX_NUMBER = "FAX_NUMBER";
	
	//Sap Delivery Constants
	public static final String STORAGE_LOCATION = "STORE_LOC";
	public static final String SALES_UNIT = "SALES_UNIT";
	public static final String RETURN_UNIT = "UNIT";
	public static final String SHIPMENT_TYPE = "SHIP_TYPE";
	public static final String BLOCK_DELIVERY = "DELV_BLOCK";
	public static final String DELIVERY_PRIORITY = "DLV_PRIO";
	public static final String COMP_QUANT = "COMP_QUANT";
	public static final String SHIPPING_MODE = "POITM_NO_S";
	
	//Sap Dates Constants
	public static final String REQ_DATE = "REQ_DATE";
	public static final String GI_DATE = "GI_DATE";
	public static final String MS_DATE = "MS_DATE";
	public static final String LOAD_DATE = "LOAD_DATE";
	public static final String TP_DATE = "TP_DATE";
	public static final String REQUIRED_DATE = "REQ_DATE_H";
	
	//Response Constants
}
