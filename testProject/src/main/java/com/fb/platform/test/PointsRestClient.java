package com.fb.platform.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.joda.time.DateTime;

import com.fb.platform.payback._1_0.ActionCode;
import com.fb.platform.payback._1_0.PointsRequest;
import com.fb.platform.payback._1_0.PointsResponse;


public class PointsRestClient {

	private static String API_URL = "http://localhost:8080/";
	
	public static void main(String[] args) throws Exception {
		HttpClient httpClient = new HttpClient();

		PostMethod pointsMethod = new PostMethod(API_URL + "paybackWS/points/store");

		JAXBContext context = JAXBContext.newInstance("com.fb.platform.payback._1_0");
		
		PointsRequest request = setPointsRequest();
		
		
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);

		System.out.println("storePointsReq : \n" + sw.toString());

		StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
		pointsMethod.setRequestEntity(requestEntity);

		int statusCode = httpClient.executeMethod(pointsMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("unable to execute the points method : " + statusCode);
			System.exit(1);
		}
		String responseStr = pointsMethod.getResponseBodyAsString();
		System.out.println("Got the applyScratchCard Response : \n\n" + responseStr);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		PointsResponse response = (PointsResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
		System.out.println(response.getPointsStatus());

		
	}

	private static PointsRequest setPointsRequest() {
		PointsRequest request = new PointsRequest();
		request.setActionCode(ActionCode.PREALLOC_EARN);
		request.setClientName("Future Bazaar");
		
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setAmount(new BigDecimal(4000));
		orderRequest.setLoyaltyCard("1234567812345678");
		orderRequest.setOrderId(3);
		orderRequest.setReason("REST CLIENT");
		orderRequest.setReferenceId("5051");
		DateTime datetime = DateTime.now();
		GregorianCalendar greg = datetime.toGregorianCalendar();
		orderRequest.set
		
		return request;
	}
	
}
