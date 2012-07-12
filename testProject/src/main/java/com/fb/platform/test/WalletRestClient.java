package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.math.RandomUtils;

import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.auth._1_0.LogoutRequest;
import com.fb.platform.auth._1_0.LogoutResponse;
import com.fb.platform.wallet._1_0.SubWallet;
import com.fb.platform.wallet._1_0.WalletSummaryRequest;
import com.fb.platform.wallet._1_0.WalletSummaryResponse;
import com.fb.platform.wallet._1_0.WalletHistoryRequest;
import com.fb.platform.wallet._1_0.WalletHistoryResponse;
import com.fb.platform.wallet._1_0.FillWalletRequest;
import com.fb.platform.wallet._1_0.FillWalletResponse;
import com.fb.platform.wallet._1_0.FillWalletStatus;

import com.fb.platform.wallet._1_0.PayRequest;
import com.fb.platform.wallet._1_0.PayResponse;
import com.fb.platform.wallet._1_0.PayStatus;

import com.fb.platform.wallet._1_0.RefundRequest;
import com.fb.platform.wallet._1_0.RefundResponse;
import com.fb.platform.wallet._1_0.RefundStatus;

import com.fb.platform.wallet._1_0.RevertRequest;
import com.fb.platform.wallet._1_0.RevertResponse;
import com.fb.platform.wallet._1_0.RevertStatus;


public class WalletRestClient {

	public static void main(String[] args) throws Exception {
		//test login
		LoginResponse response = login();
		System.out.println("\n SessionToken: " + response.getSessionToken());
		
		String status = getSummary(response);
		System.out.println("\n Summary Status: " + status);
		
		status = getHistory(response);
		System.out.println("\n History Status: " + status);
		
		status = fillWallet(response);
		System.out.println("\n Fill status: " + status);
		
		status = getSummary(response);
		System.out.println("\n Summary Status: " + status);
		
		status = getHistory(response);
		System.out.println("\n History Status: " + status);
		
		PayResponse payResponse = payFromWallet(response);
		System.out.println("\n Pay status: " + payResponse.getPayStatus().toString());
		
		status = getSummary(response);
		System.out.println("\n Summary Status: " + status);

		status = getHistory(response);
		System.out.println("\n History Status: " + status);
		
		status = refundFromWallet(response);
		System.out.println("\n Refund status: " + status);
		
		status = getSummary(response);
		System.out.println("\n Summary Status: " + status);

		status = getHistory(response);
		System.out.println("\n History Status: " + status);
		
		status = revertWalletTransaction(response, payResponse.getTransactionId());
		System.out.println("\n Revert status: " + status);
		
		status = getSummary(response);
		System.out.println("\n Summary Status: " + status);

		status = getHistory(response);
		System.out.println("\n History Status: " + status);
		
	}

	public static LoginResponse login() throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod loginMethod = new PostMethod("http://localhost:8080/userWS/auth/login");
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("jasvipul@gmail.com");
		loginRequest.setPassword("testpass");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);

		System.out.println("\n\nLoginReq : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		loginMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(loginMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute the login method : \n\n" + statusCode);
			System.exit(1);
		}
		String loginResponseStr = loginMethod.getResponseBodyAsString();
		System.out.println("Got the login Response : \n\n" + loginResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LoginResponse loginResponse = (LoginResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(loginResponseStr)));

		return loginResponse;
	}

	public static String getSummary(LoginResponse response) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod summaryMethod = new PostMethod("http://localhost:8080/walletWS/wallet/summary");
		WalletSummaryRequest summaryRequest = new WalletSummaryRequest();
		summaryRequest.setUserId(response.getUserId());
		summaryRequest.setClientId(-5);
		summaryRequest.setSessionToken(response.getSessionToken());

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(summaryRequest, sw);

		System.out.println("\n\nSummaryReq : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		summaryMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(summaryMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute the wallet summary method : \n\n" + statusCode);
			System.exit(1);
		}
		String summaryResponseStr = summaryMethod.getResponseBodyAsString();
		System.out.println("Got the summary Response : \n\n" + summaryResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		WalletSummaryResponse summaryResponse = (WalletSummaryResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(summaryResponseStr)));

		return summaryResponse.getWalletSummaryStatus().toString();
	}

	public static String getHistory(LoginResponse response) throws Exception{
		HttpClient httpClient = new HttpClient();
		PostMethod historyMethod = new PostMethod("http://localhost:8080/walletWS/wallet/history");
		WalletHistoryRequest historyRequest = new WalletHistoryRequest();
		historyRequest.setWalletId(8);
		historyRequest.setSessionToken(response.getSessionToken());
		historyRequest.setFromDate(null);
		historyRequest.setToDate(null);
		historyRequest.setSubWallet(null);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(historyRequest, sw);

		System.out.println("\n\nHistoryReq : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		historyMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(historyMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute the wallet history method : \n\n" + statusCode);
			System.exit(1);
		}
		String historyResponseStr = historyMethod.getResponseBodyAsString();
		System.out.println("Got the history Response : \n\n" + historyResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		WalletHistoryResponse historyResponse = (WalletHistoryResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(historyResponseStr)));

		return historyResponse.getWalletHistoryStatus().toString();
				
	}
	
	public static String fillWallet(LoginResponse response) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod fillMethod = new PostMethod("http://localhost:8080/walletWS/wallet/fill");
		FillWalletRequest request = new FillWalletRequest();
		request.setAmount(new BigDecimal(100));
		request.setRefundId(100);
		request.setSessionToken(response.getSessionToken());
		request.setSubWallet(SubWallet.fromValue("REFUND"));
		request.setUserId(response.getUserId());
		request.setClientId(-5);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n\nFillRequest : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		fillMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(fillMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute fillWallet method : \n\n" + statusCode);
			System.exit(1);
		}
		String fillResponseStr = fillMethod.getResponseBodyAsString();
		System.out.println("Got the Fill Response : \n\n" + fillResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		FillWalletResponse fillResponse = (FillWalletResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(fillResponseStr)));

		return fillResponse.getFillWalletStatus().toString();
	}

	public static PayResponse payFromWallet(LoginResponse response) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod payMethod = new PostMethod("http://localhost:8080/walletWS/wallet/pay");
		PayRequest request = new PayRequest();
		request.setAmount(new BigDecimal(100));
		request.setSessionToken(response.getSessionToken());
		request.setOrderId(1000);
		request.setPassword("testpass");
		request.setUserId(response.getUserId());
		request.setClientId(-5);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n\nPayRequest : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		payMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(payMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute payWallet method : \n\n" + statusCode);
			System.exit(1);
		}
		String payResponseStr = payMethod.getResponseBodyAsString();
		System.out.println("Got the Pay Response : \n\n" + payResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		PayResponse payResponse = (PayResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(payResponseStr)));

		return payResponse;
	}

	public static String refundFromWallet(LoginResponse response) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod refundMethod = new PostMethod("http://localhost:8080/walletWS/wallet/refund");
		RefundRequest request = new RefundRequest();
		request.setAmount(new BigDecimal(100));
		request.setSessionToken(response.getSessionToken());
		request.setRefundId(100);
		request.setUserId(response.getUserId());
		request.setClientId(-5);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n\nRefundRequest : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		refundMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(refundMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute refund from Wallet : \n\n" + statusCode);
			System.exit(1);
		}
		String refundResponseStr = refundMethod.getResponseBodyAsString();
		System.out.println("Got the Pay Response : \n\n" + refundResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		RefundResponse refundResponse = (RefundResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(refundResponseStr)));

		return refundResponse.getRefundStatus().toString();
	}

	public static String revertWalletTransaction(LoginResponse response, String transactionId) throws Exception {
		HttpClient httpClient = new HttpClient();
		PostMethod revertMethod = new PostMethod("http://localhost:8080/walletWS/wallet/revert");
		RevertRequest request = new RevertRequest();
		request.setAmount(new BigDecimal(100));
		request.setSessionToken(response.getSessionToken());
		request.setTransactionIdToRevert(transactionId);
		request.setUserId(response.getUserId());
		request.setClientId(-5);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.wallet._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n\nRevertRequest : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		revertMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(revertMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute revert from Wallet : \n\n" + statusCode);
			System.exit(1);
		}
		String revertResponseStr = revertMethod.getResponseBodyAsString();
		System.out.println("Got the Pay Response : \n\n" + revertResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		RevertResponse revertResponse = (RevertResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(revertResponseStr)));

		return revertResponse.getRevertStatus().toString();
	}

}