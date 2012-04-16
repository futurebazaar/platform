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

import com.fb.platform.promotion._1_0.ApplyScratchCardRequest;
import com.fb.platform.promotion._1_0.ApplyScratchCardResponse;
import com.fb.platform.promotion._1_0.CommitCouponRequest;
import com.fb.platform.promotion._1_0.CommitCouponResponse;

/**
 * @author vinayak
 *
 */
public class ScratchCardRestClient {

	public static void main(String[] args) throws Exception {
		String sessionToken = RestClient.login();
		applyScratchCard(sessionToken);
	}

	private static void applyScratchCard(String sessionToken) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod applyScratchCardMethod = new PostMethod("http://localhost:8080/promotionWS/scratchCard/apply");

		ApplyScratchCardRequest request = new ApplyScratchCardRequest();
		request.setCardNumber("BB0003TLK");
		request.setSessionToken(sessionToken);

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.promotion._1_0");

		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("\n\napplyScratchCardReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		applyScratchCardMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(applyScratchCardMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the applyScratchCard method : " + statusCode);
			System.exit(1);
		}
		String responseStr = applyScratchCardMethod.getResponseBodyAsString();
		System.out.println("Got the applyScratchCard Response : \n\n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ApplyScratchCardResponse response = (ApplyScratchCardResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getApplyScratchCardStatus().toString());

		
	}
}
