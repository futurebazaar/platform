/**
 * 
 */
package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fb.platform.auth._1_0.LoginRequest;
import com.fb.platform.auth._1_0.LoginResponse;
import com.fb.platform.auth._1_0.LogoutRequest;
import com.fb.platform.auth._1_0.LogoutResponse;
import com.fb.platform.auth._1_0.AddUserRequest;
import com.fb.platform.auth._1_0.AddUserResponse;
import com.fb.platform.auth._1_0.GetUserRequest;
import com.fb.platform.auth._1_0.GetUserResponse; 
/**
 * @author vinayak
 *
 */
public class RestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//test login
		String sessionToken = login();
		//test logout
		logout(sessionToken);
	}

	private static String login() throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod loginMethod = new PostMethod("http://localhost:8080/userWS/auth/login");
		//StringRequestEntity requestEntity = new StringRequestEntity("<loginRequest><username>vinayak</username><password>password</password></loginRequest>", "application/xml", null);
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("jasvipul@gmail.com");
		loginRequest.setPassword("testpass");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(loginRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		loginMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(loginMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the login method : " + statusCode);
			return null;
		}
		String loginResponseStr = loginMethod.getResponseBodyAsString();
		System.out.println("Got the login Response : \n" + loginResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LoginResponse loginResponse = (LoginResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(loginResponseStr)));

		return loginResponse.getSessionToken();
	}

	private static void logout(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod logoutMethod = new PostMethod("http://localhost:8080/userWS/auth/logout");

		LogoutRequest logoutReq = new LogoutRequest();
		logoutReq.setSessionToken(sessionToken);
		
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(logoutReq, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		logoutMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(logoutMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the logout method : " + statusCode);
			return;
		}
		String logoutResponseStr = logoutMethod.getResponseBodyAsString();
		System.out.println("Got the logout Response : \n" + logoutResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		LogoutResponse logoutResponse = (LogoutResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(logoutResponseStr)));
		System.out.println(logoutResponse.getLogoutStatus());
	}
	
	private static String getUser() throws Exception{
		
		HttpClient httpClient = new HttpClient();

		PostMethod getUserMethod = new PostMethod("http://localhost:8080/userWS/user/get");
		GetUserRequest getUserRequest = new GetUserRequest();
		getUserRequest.setKey("jasvipul@gmail.com");
		getUserRequest.setSessionToken("");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(getUserRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		getUserMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(getUserMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the get user method : " + statusCode);
			return null;
		}
		String getUserResponseStr = getUserMethod.getResponseBodyAsString();
		System.out.println("Got the get user Response : \n" + getUserResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		GetUserResponse getUserResponse = (GetUserResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(getUserResponseStr)));
		return getUserResponse.getUserName();		
	}
	
	private static String addUser() throws Exception{
		HttpClient httpClient = new HttpClient();

		PostMethod addUserMethod = new PostMethod("http://localhost:8080/userWS/user/add");
		AddUserRequest addUserRequest = new AddUserRequest();
		addUserRequest.setUserName("newtestuserviaclient@test.com");
		addUserRequest.setPassword("testpass");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.auth._1_0");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(addUserRequest, sw);

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		addUserMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(addUserMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the add user method : " + statusCode);
			return null;
		}
		String addUserResponseStr = addUserMethod.getResponseBodyAsString();
		System.out.println("Got the add user Response : \n" + addUserResponseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		AddUserResponse addUserResponse = (AddUserResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(addUserResponseStr)));
		return addUserResponse.getSessionToken();	
	}
	
	}
