package com.fb.api.risk.ebs;

import java.util.*;
import java.security.*;

public class EBSBusinessObjHelper extends EBSBusinessObj {	
	static String[] allowedfields = {
		"RMS_RiskBox" ,
		"RMS_LicenseKey" ,
		"RMS_Template" ,
		"RMS_Site" ,
		"RMS_IP_Address" ,
		"RMS_IP_Forwarded_For" ,
		"RMS_User_Agent" ,
		"RMS_Extra_Field_1" ,
		"RMS_Extra_Field_2" ,
		"RMS_Extra_Field_3" ,
		"RMS_Extra_Field_4" ,
		"RMS_Extra_Field_5" ,
		"Ecom_ConsumerID" ,
		"Ecom_ConsumerOrderID", 
		"Ecom_ConsumerIsReliable" ,
		"Ecom_Product_Description" ,
		"Ecom_Transaction_Amount" ,
		"Ecom_Transaction_Currency", 
		"Ecom_Transaction_CurrencyValue" ,
		"Ecom_Transaction_Date" ,
		"Ecom_Payment_Card_Number_Hash" ,
		"Ecom_Payment_Card_Number_First_6_Digit" ,
		"Ecom_Payment_Card_Number_Last_4_Digit" ,
		"Ecom_Payment_Card_Type" ,
		"Ecom_Payment_Card_Name" ,
		"Ecom_Payment_Status" ,
		"Ecom_BillTo_Postal_Name" ,
		"Ecom_BillTo_Postal_Street_Line1" ,
		"Ecom_BillTo_Postal_City" ,
		"Ecom_BillTo_Postal_StateProv" ,
		"Ecom_BillTo_Postal_PostalCode" ,
		"Ecom_BillTo_Postal_CountryCode" ,
		"Ecom_BillTo_Online_Email" ,
		"Ecom_BillTo_Telecom_Phone_Number" ,
		"Ecom_ShipTo_Postal_Name" ,
		"Ecom_ShipTo_Postal_Street_Line1" ,
		"Ecom_ShipTo_Postal_City" ,
		"Ecom_ShipTo_Postal_StateProv" ,
		"Ecom_ShipTo_Postal_PostalCode" ,
		"Ecom_ShipTo_Postal_CountryCode" ,
		"Ecom_ShipTo_Period" ,
		"Ecom_ShipTo_Online_Email" ,
		"Ecom_ShipTo_Telecom_Phone_Number" ,
		"Ecom_User_ID_MD5" ,
		"Ecom_User_Password_MD5" ,
		"Ecom_Product_Description"
	};
	public EBSBusinessObjHelper() {
		
		url = "riskengine/request";
	    check_field = "TxnId";
	    allowed_fields = new HashMap();
	    
	    for (int i = 0; i < allowedfields.length; i++) {
	    	
	      allowed_fields.put(allowedfields[i], new Integer(1));
	      
	    }
	    setIsSecure(true);
	}
	public EBSBusinessObjHelper(boolean s) {
		
		url = "riskengine/request";
	    check_field = "TxnId";
	    allowed_fields = new HashMap();
	    for (int i = 0; i < allowedfields.length; i++) {
	    	
	      allowed_fields.put(allowedfields[i],new Integer(1));
	      
	    }
	    setIsSecure(s);
	}

}