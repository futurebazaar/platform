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
import com.fb.platform.wallet._1_0.WalletSummaryRequest;
import com.fb.platform.wallet._1_0.WalletSummaryResponse;
import com.fb.platform.wallet._1_0.WalletHistoryRequest;
import com.fb.platform.wallet._1_0.WalletHistoryResponse;

public class WalletRestClient {

	public static void main(String[] args) throws Exception {
		//test login
		LoginResponse response = login();
		System.out.println("\n SessionToken: " + response.getSessionToken());
		String status = getSummary(response);
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
}
