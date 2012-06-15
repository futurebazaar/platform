/**
 * 
 */
package com.fb.platform.egv.mapper;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;

import com.fb.platform.egv.resource.GiftVoucherResource;


/**
 * @author keith
 *
 */
public class CancelMapper {
	
	public static com.fb.platform.egv.to.CancelRequest xmlToCoreRequest(String webRequestXml)  throws JAXBException {
		com.fb.platform.egv.to.CancelRequest coreRequest = new com.fb.platform.egv.to.CancelRequest();
		try{
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.CancelRequest webRequest = (com.fb.platform.egv._1_0.CancelRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			coreRequest.setOrderItemId(webRequest.getOrderItemId());

		}catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}
			
	public static String coreResponseToXml(com.fb.platform.egv.to.CancelResponse coreResponse) throws JAXBException,DatatypeConfigurationException{
			
		com.fb.platform.egv._1_0.CancelResponse webResponse = new com.fb.platform.egv._1_0.CancelResponse();
		
		// Mapping Code
		webResponse.setCancelResponseStatus(com.fb.platform.egv._1_0.CancelResponseStatusEnum.fromValue(coreResponse.getResponseStatus().toString()));
		webResponse.setSessionToken(coreResponse.getSessionToken());

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(coreResponse, outStringWriter);

		return outStringWriter.toString();
	}
	
}

