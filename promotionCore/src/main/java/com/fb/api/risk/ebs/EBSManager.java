package com.fb.api.risk.ebs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class EBSManager {


	
	private String ebsthreshold;

	

	/**
	 * getResult() method sends the data to the EBS Server it recieve a boolean
	 * result either True or False based on query() method. all the send and
	 * recieved data from EBS is saved using saveDataSendToEBS() and
	 * saveEBSResponse() method
	 * 
	 * @param cardNo
	 * @param remoteAddress
	 * @param headerVal
	 * @param userAgent
	 * @param orderId
	 * @param totalAmount
	 * @param cardType
	 * @param cardBillingName
	 * @param profileDetail
	 * @param billingAddressVal 
	 * @param shippingAddressVal 
	 * @param productDatail
	 * @throws NoSuchAlgorithmException
	 * @return
	 */
	// Tinla will send Profile detail
	// Tinla will provide Product name with Quantity like 4 x HotBag | 2 x Huggys Bag
	public HashMap getEBSResponse(String cardNo, String remoteAddress,
			String headerVal, String userAgent, String orderId,
			String totalAmount, String cardType, String cardBillingName,HashMap<String,String> profileDetail, HashMap<String, String> shippingAddressVal, HashMap<String, String> billingAddressVal, String productDetails)
			throws NoSuchAlgorithmException {
		//System.out.println("APIEBSManager.getEBSResponse()");
		String result = null;
		String consumerID = null;
		String concatResult = null;
		String billingDate = null;
		String billingCurrencyCode = null;
		String billingName = null;
		String billingAddress = null;
		String billingCity = null;
		String billingCountry = null;
		String billingState = null;
		String billingPostalCode = null;
		String billingPhone = null;
		String billingEmail = null;
		String profileEmail = null;
		
		String shippingName = null;
		String shippingAddress = null;
		String shippingCity = null;
		String shippingCountry = null;
		String shippingState = null;
		String shippingPostalCode = null;
		String shippingPhone = null;
		String shippingEmail = null;
		
		String ipAddress = remoteAddress;
		String hearedValue = headerVal;
		//String productDetails = "";
		String agent = userAgent;
		
			
			shippingName = shippingAddressVal.get("shippingName");
			shippingAddress = shippingAddressVal.get("shippingAddress");
			shippingCity = shippingAddressVal.get("shippingCity");
			shippingCountry = shippingAddressVal.get("shippingCountry");
			shippingState = shippingAddressVal.get("shippingState");
			shippingPostalCode = shippingAddressVal.get("shippingPostalCode");
			shippingPhone = shippingAddressVal.get("shippingPhone");
			shippingEmail = shippingAddressVal.get("shippingEmail");
			
			
			billingCurrencyCode = "INR";			
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			billingDate = format.format(date);
			billingName = billingAddressVal.get("billingName");
			billingAddress = billingAddressVal.get("billingAddress");
			billingCity = billingAddressVal.get("billingCity");
			billingCountry = billingAddressVal.get("billingCountry");
			billingState =  billingAddressVal.get("billingState");
			billingPostalCode = billingAddressVal.get("billingPostalCode");
			billingPhone = billingAddressVal.get("billingPhone");
			billingEmail = billingAddressVal.get("billingEmail");
		
		
		EBSBusinessObjHelper ebsBussObjHelp;

		boolean isSecure = true;

		ebsBussObjHelp = new EBSBusinessObjHelper(isSecure);
		// uncomment to turn debugging on
		ebsBussObjHelp.debug = true;
		// Set timeout to ten seconds
		ebsBussObjHelp.setTimeout(10);

		String cardNum = cardNo;
		MessageDigest m = MessageDigest.getInstance("MD5");
		byte[] data = cardNum.getBytes();
		m.update(data, 0, data.length);
		BigInteger i = new BigInteger(1, m.digest());
		Object[] str = { i };
		String cardNumHash = String.format("%1$032X", str);

		//EBSRMSConfig ebsConfig = (EBSRMSConfig) Nucleus.getGlobalNucleus().resolveName("/com/fb/commerce/ebs/EBSRMSConfig");
		EBSRMSConfig ebsConfig = new EBSRMSConfig();

		HashMap<String, String> paramsToEBS = new HashMap<String, String>();
		paramsToEBS.put("RMS_LicenseKey", ebsConfig.getRmsLicenseKey());
		paramsToEBS.put("RMS_Template", ebsConfig.getRmsTemplate());
		paramsToEBS.put("RMS_Site", ebsConfig.getRmsSite());
		//String RMS_RiskBox = (String) ServletUtil.getCurrentRequest().getSession().getAttribute("rmsID");
		String RMS_RiskBox = "";
		paramsToEBS.put("RMS_RiskBox", RMS_RiskBox);

		// If client browser does not support JavaScript,
		// request.getParameter("__RMSID_RB")); will be empty
		paramsToEBS.put("RMS_IP_Address", ipAddress);
		paramsToEBS.put("RMS_IP_Forwarded_For", hearedValue);
		paramsToEBS.put("RMS_User_Agent", agent);

		// Transaction Info
		paramsToEBS.put("Ecom_Transaction_Amount", totalAmount);
		paramsToEBS.put("Ecom_Transaction_Currency", billingCurrencyCode);
		paramsToEBS.put("Ecom_Transaction_CurrencyValue", "");
		paramsToEBS.put("Ecom_Transaction_Date", billingDate);
		paramsToEBS.put("Ecom_Payment_Card_Number_Hash", cardNumHash);

		if (cardNum.length() == 16) {
			paramsToEBS.put("Ecom_Payment_Card_Number_First_6_Digit", cardNum.substring(0, 6));
			paramsToEBS.put("Ecom_Payment_Card_Number_Last_4_Digit", cardNum.substring(12, 16));
		}
		paramsToEBS.put("Ecom_Payment_Card_Type", cardType);
		paramsToEBS.put("Ecom_Payment_Status", "YES");

		// Product Info
		paramsToEBS.put("Ecom_Product_Description", productDetails);

		// Customer Info
		paramsToEBS.put("Ecom_ConsumerID", consumerID);
		paramsToEBS.put("Ecom_ConsumerOrderID", orderId);
		paramsToEBS.put("Ecom_ConsumerIsReliable", "Yes");
		/*
		 * input.put("Ecom_User_ID_MD5", "UserName");
		 * input.put("Ecom_User_Password_MD5", "Password");
		 */

		// Customer Billing Info
		paramsToEBS.put("Ecom_BillTo_Postal_Name", cardBillingName);
		paramsToEBS.put("Ecom_BillTo_Postal_Street_Line1", billingAddress);
		paramsToEBS.put("Ecom_BillTo_Postal_City", billingCity);
		paramsToEBS.put("Ecom_BillTo_Postal_StateProv", billingState);
		paramsToEBS.put("Ecom_BillTo_Postal_PostalCode", billingPostalCode);
		paramsToEBS.put("Ecom_BillTo_Postal_CountryCode", billingCountry);
		paramsToEBS.put("Ecom_BillTo_Online_Email", billingEmail);
		/*paramsToEBS.put("Ecom_BillTo_Online_Email", profileEmail);*/
		paramsToEBS.put("Ecom_BillTo_Telecom_Phone_Number", billingPhone);

		// Customer Shipping Info
		paramsToEBS.put("Ecom_ShipTo_Postal_Name", shippingName);
		paramsToEBS.put("Ecom_ShipTo_Postal_Street_Line1", shippingAddress);
		paramsToEBS.put("Ecom_ShipTo_Postal_City", shippingCity);
		paramsToEBS.put("Ecom_ShipTo_Postal_StateProv", shippingState);
		paramsToEBS.put("Ecom_ShipTo_Postal_PostalCode", shippingPostalCode);
		paramsToEBS.put("Ecom_ShipTo_Postal_CountryCode", shippingCountry);
		paramsToEBS.put("Ecom_ShipTo_Online_Email", shippingEmail);
		paramsToEBS.put("Ecom_ShipTo_Telecom_Phone_Number", shippingPhone);
		/* input.put("Ecom_ShipTo_Period", "2"); */

		ebsBussObjHelp.input(paramsToEBS);
		HashMap outputFromEBS = null;

		boolean success = ebsBussObjHelp.query();
		if (success) {
			outputFromEBS = ebsBussObjHelp.output();
			saveDataSendToEBS(paramsToEBS, orderId);
			saveEBSResponse(outputFromEBS, orderId);
		} else {
			// handle no response or error in communication
			outputFromEBS = ebsBussObjHelp.output();
			saveDataSendToEBS(paramsToEBS, orderId);
			saveEBSResponse(outputFromEBS, orderId);
		}
		return outputFromEBS;
	}

	private boolean saveDataSendToEBS(HashMap<String, String> outputFromEBS,
			String orderId) {

		/*DataService ds = DataService.getDataService();
		Connection con = ds.getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;

			try {

				String sqlInsert = "insert into ebs_send_data(ID,TOTALAMOUNT,BILLINGCURRENCYCODE,BILLINGDATE,CARDTYPE,CONSUMERID,ORDERID,CARDBILLINGNAME,BILLINGADDRESS,BILLINGCITY,BILLINGSTATE,BILLINGPOSTALCODE,BILLINGCOUNTRY,BILLINGEMAIL,BILLINGPHONE,SHIPPINGNAME,SHIPPINGADDRESS,SHIPPINGCITY,SHIPPINGSTATE,SHIPPINGPOSTALCODE,SHIPPINGCOUNTRY,SHIPPINGEMAIL,SHIPPINGPHONE,TXNID,PRODUCTINFORM,LAST_MODIFIED_DATE) values(fb_ebs_send_data_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

				pstmt = con.prepareStatement(sqlInsert);
				pstmt
						.setString(1, outputFromEBS
								.get("Ecom_Transaction_Amount"));
				pstmt.setString(2, outputFromEBS
						.get("Ecom_Transaction_Currency"));
				pstmt.setString(3, outputFromEBS.get("Ecom_Transaction_Date"));
				pstmt.setString(4, outputFromEBS.get("Ecom_Payment_Card_Type"));
				pstmt.setString(5, outputFromEBS.get("Ecom_ConsumerID"));
				pstmt.setString(6, orderId);
				pstmt.setString(7, outputFromEBS.get("Ecom_BillTo_Postal_Name"));
				pstmt.setString(8, outputFromEBS.get("Ecom_BillTo_Postal_Street_Line1"));
				pstmt.setString(9, outputFromEBS.get("Ecom_BillTo_Postal_City"));
				pstmt.setString(10, outputFromEBS.get("Ecom_BillTo_Postal_StateProv"));
				pstmt.setString(11, outputFromEBS.get("Ecom_BillTo_Postal_PostalCode"));
				pstmt.setString(12, outputFromEBS.get("Ecom_BillTo_Postal_CountryCode"));
				pstmt.setString(13, outputFromEBS.get("Ecom_BillTo_Online_Email"));
				pstmt.setString(14, outputFromEBS.get("Ecom_BillTo_Telecom_Phone_Number"));
				pstmt.setString(15, outputFromEBS.get("Ecom_ShipTo_Postal_Name"));
				pstmt.setString(16, outputFromEBS.get("Ecom_ShipTo_Postal_Street_Line1"));
				pstmt.setString(17, outputFromEBS.get("Ecom_ShipTo_Postal_City"));
				pstmt.setString(18, outputFromEBS.get("Ecom_ShipTo_Postal_StateProv"));
				pstmt.setString(19, outputFromEBS.get("Ecom_ShipTo_Postal_PostalCode"));
				pstmt.setString(20, outputFromEBS.get("Ecom_ShipTo_Postal_CountryCode"));
				pstmt.setString(21, outputFromEBS.get("Ecom_ShipTo_Online_Email"));
				pstmt.setString(22, outputFromEBS.get("Ecom_ShipTo_Telecom_Phone_Number"));
				pstmt.setString(23, outputFromEBS.get("TxnId"));
				pstmt.setString(24, outputFromEBS.get("Ecom_Product_Description"));
				java.util.Date today = new java.util.Date();
				Timestamp sqlDate = new Timestamp(today.getTime());
				pstmt.setTimestamp(25, sqlDate);
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				ds.closeStatement(stmt);
				ds.closePreparedStatement(pstmt);
				ds.closeConnection(con);

			}*/		
		return true;
	}

	/**
	 * @param txnID
	 * @param invoiceNum
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public boolean saveEBSResponse(HashMap<String, String> responseFromEBS,
			String orderId) {

		/*DataService ds = DataService.getDataService();
		Connection con = ds.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;

		String sql = "insert into ebs_result_data(ID, TXNID, INVOICE_NUM,SCORE,STATUS,TEMPLATE_ID,CREATED_ON,AMOUNT,ERROR,LAST_MODIFIED_DATE) values(fb_ebs_res_data_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, responseFromEBS.get("TxnId"));
				pstmt.setString(2, orderId);
				pstmt.setString(3, responseFromEBS.get("Score"));
				pstmt.setString(4, responseFromEBS.get("Status"));
				pstmt.setString(5, responseFromEBS.get("TemplateID"));
				pstmt.setString(6, responseFromEBS.get("CreatedOn"));
				pstmt.setString(7, responseFromEBS.get("Amount"));
				String error_message = responseFromEBS.get("ERROR");
				String httpResponse = responseFromEBS.get("HTTP_RESPONSE");

				String ebsError = responseFromEBS.get("Error");
				String commMesssage = "EBS ERROR :" + ebsError
						+ " | Response Error :" + error_message
						+ " | HTTP Response :" + httpResponse;
				pstmt.setString(8, commMesssage);

				java.util.Date today = new java.util.Date();
				Timestamp sqlDateTwo = new Timestamp(today.getTime());
				pstmt.setTimestamp(9, sqlDateTwo);
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				ds.closeStatement(stmt);
				ds.closePreparedStatement(pstmt);
				ds.closeConnection(con);

			}
			// 5009 to block Order i.e. awaited for customer verifiction
			// 1 process Order
		*/
		return true;
	}
	/**
	 * readFromEBSURL() connect with the EBS system and will retrive the status
	 * against the orderId. The status can be Review, Reject or Approved
	 * 
	 * @param orderId
	 * @param rmsLicenseKey
	 * @return resultEbs
	 */

	public String readFromEBSURL(String orderId, String rmsLicenseKey) {
		/*EBSRMSConfig ebsConfig = (EBSRMSConfig) Nucleus.getGlobalNucleus().resolveName("/com/fb/commerce/ebs/EBSRMSConfig");
		String proxyIp = ebsConfig.getProxyIP();
		int proxyPort = Integer.parseInt(ebsConfig.getProxyPort());
		String resultEbs = null;
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp,
				proxyPort));
		URL url;
		try {
			url = new URL("https://s1.rmsid.com/riskengine/status?ReferenceNo="
					+ orderId + "&LicenseKey=" + rmsLicenseKey);

			HttpURLConnection uc = (HttpURLConnection) url
					.openConnection(proxy);
			uc.connect();

			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream()));
			String line = null;

			while ((line = in.readLine()) != null) {

				tmp.append(line + "\n");

			}
			resultEbs = tmp.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultEbs;*/
		return "";
	}

	/**
	 * errorEbsResponseMap() method will put the EBS response in Map which
	 * contains TxnId, TemplateId, CreatedOn, Status, Score, Error
	 * 
	 * @param ebsErrorResponse
	 * @return mapEBS
	 */
	public Map<String, String> errorEbsResponseMap(String ebsErrorResponse) {

		Map<String, String> mapEBS = new LinkedHashMap<String, String>();
		try {

			for (String keyValueEBS : ebsErrorResponse.split(";")) {

				String[] keyValueEbsParts = keyValueEBS.split("=", 2);

				for (int j = 0; j < keyValueEbsParts.length; j++) {

					mapEBS.put(keyValueEbsParts[0], keyValueEbsParts[1]);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return mapEBS;
	}

	/**
	 * saveErrorDataResponse() method save the EBS response data in database
	 * 
	 * @param orderId
	 * @param txnid
	 * @param templateId
	 * @param createdOn
	 * @param status
	 * @param score
	 * @param error
	 */
	public void saveErrorDataResponse(String orderId, String txnid,
			String templateId, String createdOn, String status, String score,
			String error) {
		/*DataService ds = DataService.getDataService();
		Connection con = ds.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;

		String sql = "insert into ebs_second_attempt(ID, ORDER_ID, TXN_ID, CREATED_ON, STATUS, SCORE, ERROR, LAST_MODIFIED_DATE) values(fb_ebs_sec_atm_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, orderId);
			pstmt.setString(2, txnid);
			pstmt.setString(3, createdOn);
			pstmt.setString(4, status);
			pstmt.setString(5, score);
			pstmt.setString(6, error);
			java.util.Date today = new java.util.Date();
			Timestamp sqlDateTwo = new Timestamp(today.getTime());
			pstmt.setTimestamp(7, sqlDateTwo);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ds.closeStatement(stmt);
			ds.closePreparedStatement(pstmt);
			ds.closeConnection(con);

		}*/

	}

	/**
	 * validateEbsResponse() validate the parameter value
	 * 
	 * @param txnid
	 * @param templateId
	 * @param createdOn
	 * @param status
	 * @param score
	 * @param error
	 * @return result
	 */
	public boolean validateEbsResponse(String txnid, String templateId,
			String createdOn, String status, String score, String error) {
		boolean result = true;
		result = ebsResponseCheck(txnid);
		result = ebsResponseCheck(templateId);
		result = ebsResponseCheck(createdOn);
		result = ebsResponseCheck(status);
		result = ebsResponseCheck(score);
		result = ebsErrorCheck(error);
		return result;
	}

	/**
	 * ebsErrorCheck() check the null value
	 * 
	 * @param error
	 * @return resultErrVal
	 */
	private boolean ebsErrorCheck(String error) {
		boolean resultErrVal = false;

		if (error != null) {
			resultErrVal = false;
		}
		if (error == null) {
			resultErrVal = true;
		}
		return resultErrVal;
	}

	/**
	 * ebsResponseCheck() check for null value
	 * 
	 * @param stringVal
	 * @return resultValue
	 */
	private boolean ebsResponseCheck(String stringVal) {

		boolean resultValue = true;
		if (stringVal != null) {
			resultValue = true;
		}
		if (stringVal.trim().equalsIgnoreCase("")) {
			resultValue = false;
		}
		return resultValue;
	}

	public String paymentGatewayFailUpdate(String TXNId, String statusFail) {

		EBSRMSConfig ebsConfig = new EBSRMSConfig();
		String proxyIp = ebsConfig.getProxyIP();
		String rms_Lic_Key = ebsConfig.getRmsLicenseKey();
		int proxyPort = Integer.parseInt(ebsConfig.getProxyPort());
		String resultPay_Fail_Ebs = null;
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp,
				proxyPort));
		URL url;
		try {
			url = new URL(
					"https://s1.rmsid.com/riskengine/updatestatus?RMS_TxnId="
							+ TXNId + "&RMS_LicenseKey=" + rms_Lic_Key
							+ "&RMS_Status=" + statusFail);

			HttpURLConnection uc = (HttpURLConnection) url
					.openConnection(proxy);
			uc.connect();

			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream()));
			String line = null;

			while ((line = in.readLine()) != null) {

				tmp.append(line + "\n");

			}
			resultPay_Fail_Ebs = tmp.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultPay_Fail_Ebs;
	}	

	public String getEbsthreshold() {
		return ebsthreshold;
	}

	public void setEbsthreshold(String ebsthreshold) {
		this.ebsthreshold = ebsthreshold;
	}

	

}
