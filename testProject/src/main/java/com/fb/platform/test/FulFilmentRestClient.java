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

import com.fb.platform.fulfilment._1_0.SellerByPincodeRequest;
import com.fb.platform.fulfilment._1_0.SellerByPincodeResponse;

/**
 * @author suhas
 *
 */
public class FulFilmentRestClient {

		public static void main(String[] args) throws Exception{
			getSellersByPincode();
		}
		
		private static void getSellersByPincode() throws Exception{
			HttpClient httpClient = new HttpClient();

			PostMethod sellerByPincodeMethod = new PostMethod("http://localhost:8082/fulfilmentWS/fulfilment/sellerbypincode");

			SellerByPincodeRequest request = new SellerByPincodeRequest();
			request.setPincode("400003");

			JAXBContext context = JAXBContext.newInstance("com.fb.platform.fulfilment._1_0");

			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(request, sw);

			System.out.println("\n\nsellerByPincodeRequest : \n" + sw.toString());

			StringRequestEntity requestEntity = new StringRequestEntity(sw.toString());
			sellerByPincodeMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(sellerByPincodeMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("unable to execute the applyScratchCard method : " + statusCode);
				System.exit(1);
			}
			String responseStr = sellerByPincodeMethod.getResponseBodyAsString();
			System.out.println("Got the sellerByPincode Response : \n\n" + responseStr);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			SellerByPincodeResponse response = (SellerByPincodeResponse) unmarshaller.unmarshal(new StreamSource(new StringReader(responseStr)));
			
			System.out.println("Pincode : " + response.getPincode());
			System.out.println("Sellers : " + response.getSellers());
			System.out.println("Status Code : " + response.getStatusCode());
			System.out.println("Status Message : " + response.getStatusMessage());
			
		}
}
