/**
 * 
 */
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
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.auth._1_0.LogoutRequest;
import com.fb.platform.auth._1_0.LogoutResponse;
import com.fb.platform.egv._1_0.ApplyRequest;
import com.fb.platform.egv._1_0.ApplyResponse;
import com.fb.platform.egv._1_0.CancelRequest;
import com.fb.platform.egv._1_0.CancelResponse;
import com.fb.platform.egv._1_0.CreateRequest;
import com.fb.platform.egv._1_0.CreateResponse;
import com.fb.platform.egv._1_0.GetInfoRequest;
import com.fb.platform.egv._1_0.GetInfoResponse;
import com.fb.platform.egv._1_0.UseRequest;
import com.fb.platform.egv._1_0.UseResponse;
/**
 * @author keith
 *
 */
public class eGVRestClient {
	
	private static final String QA_URL = "http://10.0.102.21:8082/";
	
	private static final String LOCALHOST = "http://localhost:8082/";
	
	private static String URL = QA_URL;
	
	private static final String EGV_URL = URL + "eGVWS/egv/";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//test login
		String sessionToken = login();
		String gvNumber="34989761072";
		String gvPin="12345";
		String mail="keith.fernandez@futuregroup.in";
		BigDecimal amount = new BigDecimal(1000);
		int orderItemId = -1;
		int orderId=-1;
		pingGV();
		xsdDisplayGV();
		createGV(sessionToken, mail, amount, orderItemId);
//		getInfoGV(sessionToken, gvNumber);
//		applyGV(sessionToken, gvNumber, gvPin);
//		useGV(sessionToken, gvNumber,amount,orderId);
//		cancelGV(sessionToken, gvNumber);
		logout(sessionToken);
	}

	public static String login() throws Exception {
		HttpClient httpClient = new HttpClient();
		//PostMethod loginMethod = new PostMethod("http://10.0.102.12:8082/userWS/auth/login");
		PostMethod loginMethod = new PostMethod(URL + "userWS/auth/login");
		//StringRequestEntity requestEntity = new StringRequestEntity("<loginRequest><username>vinayak</username><password>password</password></loginRequest>", "application/xml", null);
		LoginRequest loginRequest = new LoginRequest();
		//loginRequest.setUsername("9920694762");
		//loginRequest.setPassword("test");
//		loginRequest.setUsername("neha.garani@gmail.com");
//		loginRequest.setPassword("testpass");
		loginRequest.setUsername("Narendra.adki@futuregroup.in");
		loginRequest.setPassword("1234");
//		loginRequest.setUsername("1010101010");
//		loginRequest.setPassword("shagunankush");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);
		
		System.out.println("\n================== Testing Login Web Service Call ============ \n" +
				"Logging In " +
				"The URL is : "+ URL + "userWS/auth/login");
		System.out.println("\n\nLoginReq : \n" + sw.toString());
		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		loginMethod.setRequestEntity(requestEntity);

		int statusCode = 2;
		
		try{
			statusCode = httpClient.executeMethod(loginMethod);
		}catch (Exception e) {
			System.out.println("login failing\n"+e);
		}
		
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\n\nunable to execute the login method : \n\n" + statusCode);
			System.exit(1);
		}
		String loginResponseStr = loginMethod.getResponseBodyAsString();
		System.out.println("Got the login Response : \n\n" + loginResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LoginResponse loginResponse = (LoginResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(loginResponseStr)));

		return loginResponse.getSessionToken();
	}

	private static void applyGV(String sessionToken, String gvNumber, String gvPin) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(EGV_URL + "apply");

		ApplyRequest applyRequest = new ApplyRequest();
		applyRequest.setGiftVoucherNumber(Long.parseLong(gvNumber));
		applyRequest.setGiftVoucherPin(gvPin);
		applyRequest.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.egv._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(applyRequest, sw);

		System.out.println("\n================== Testing eGV Apply Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL + "/apply");
		System.out.println("\n\n Request  : \n   " + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = postMethod.getResponseBodyAsString();
		System.out.println("Got the Response : \n\n   " + responseString);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ApplyResponse response = (ApplyResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseString)));
		System.out.println("Response Status " + response.getApplyResponseStatus());
		
	}


	
	private static void pingGV() throws Exception {
		HttpClient httpClient = new HttpClient();

		GetMethod getMethod = new GetMethod(EGV_URL);

		System.out.println("\n================== Testing eGV Ping Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL);

		int statusCode = httpClient.executeMethod(getMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = getMethod.getResponseBodyAsString();
		System.out.println("Got the Response : \n\n   " + responseString);
		
	}

	private static void xsdDisplayGV() throws Exception {
		HttpClient httpClient = new HttpClient();

		GetMethod getMethod = new GetMethod(EGV_URL + "xsd");

		System.out.println("\n================== Testing eGV Ping Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL + "xsd");

		int statusCode = httpClient.executeMethod(getMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = getMethod.getResponseBodyAsString();
		System.out.println("Got the Response : \n\n   " + responseString);
		
	}

	private static void createGV(String sessionToken, String mail, BigDecimal amount, int orderItemId) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(EGV_URL + "create");

		CreateRequest createRequest = new CreateRequest();
		createRequest.setEmail(mail);
		createRequest.setAmount(amount);
		createRequest.setOrderItemId(orderItemId);
		createRequest.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.egv._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(createRequest, sw);

		System.out.println("\n================== Testing eGV Create Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL + "create");
		System.out.println("\n\n Request  : \n   " + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = postMethod.getResponseBodyAsString();
		System.out.println("\n\nGot the Response : \n   " + responseString);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CreateResponse response = (CreateResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseString)));
		System.out.println("Response Status *************** " + response.getCreateResponseStatus() + " *****************");
		System.out.println("\n\n ============= Create Web Service Call Over =============== \n\n");
		
	}

	private static void getInfoGV(String sessionToken, String gvNumber) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(EGV_URL + "getInfo");

		GetInfoRequest getInfoRequest = new GetInfoRequest();
		getInfoRequest.setGiftVoucherNumber(Long.parseLong(gvNumber));
		getInfoRequest.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.egv._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(getInfoRequest, sw);

		System.out.println("\n================== Testing eGV GetInfo Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL + "getInfo");
		System.out.println("\n\n Request  : \n   " + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = postMethod.getResponseBodyAsString();
		System.out.println("\n\nGot the Response : \n   " + responseString);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		GetInfoResponse response = (GetInfoResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseString)));
		System.out.println("Response Status ********** " + response.getGetInfoResponseStatus() + " ***************");
		System.out.println("\n\n ============= getInfo Web Service Call Over =============== \n\n");
	}

	private static void cancelGV(String sessionToken, String gvNumber) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(EGV_URL + "cancel");

		CancelRequest cancelRequest = new CancelRequest();
		cancelRequest.setGiftVoucherNumber(Long.parseLong(gvNumber));
		cancelRequest.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.egv._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(cancelRequest, sw);

		System.out.println("\n================== Testing eGV Cancel Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL + "/cancel");
		System.out.println("\n\n Request  : \n   " + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = postMethod.getResponseBodyAsString();
		System.out.println("\n\nGot the Response : \n   " + responseString);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		CancelResponse response = (CancelResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseString)));
		System.out.println("Response Status " + response.getCancelResponseStatus());
		
		System.out.println("\n\n ============= Cancel Web Service Call Over =============== \n\n");
	}
	
		
	private static void useGV(String sessionToken, String gvNumber,BigDecimal amount, int orderId) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod postMethod = new PostMethod(EGV_URL + "use");

		UseRequest useRequest = new UseRequest();
		useRequest.setGiftVoucherNumber(Long.parseLong(gvNumber));
		useRequest.setSessionToken(sessionToken);
		useRequest.setAmount(amount);
		useRequest.setOrderId(orderId);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.egv._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(useRequest, sw);

		System.out.println("\n================== Testing eGV Use Web Service Call ============ \n" + 
							"The URL is : "+ EGV_URL + "use");
		System.out.println("\n\n Request  : \n   " + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		postMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("\n\nunable to execute the Web Service method : " + statusCode);
			System.exit(1);
		}
		String responseString = postMethod.getResponseBodyAsString();
		System.out.println("\n\nGot the Response : \n   " + responseString);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		UseResponse response = (UseResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseString)));
		System.out.println("Response Status " + response.getUseResponseStatus());
		
		System.out.println("\n\n ============= Use Web Service Call Over =============== \n\n");
	}
		
	private static void logout(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod logoutMethod = new PostMethod(URL + "userWS/auth/logout");
		LogoutRequest logoutReq = new LogoutRequest();
		logoutReq.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(logoutReq, sw);

		System.out.println("\n\nLogoutReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		logoutMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(logoutMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the logout method : " + statusCode);
			return;
		}
		String logoutResponseStr = logoutMethod.getResponseBodyAsString();
		System.out.println("\n\nGot the logout Response : \n" + logoutResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LogoutResponse logoutResponse = (LogoutResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(logoutResponseStr)));
		System.out.println(logoutResponse.getLogoutStatus());
		
		System.out.println("\n\n ============= Logout Web Service Call Over =============== \n\n");
	}

}
