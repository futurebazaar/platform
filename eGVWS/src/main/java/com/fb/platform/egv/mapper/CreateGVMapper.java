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
public class CreateGVMapper {
	
	public static com.fb.platform.egv.to.CreateRequest xmlToCoreRequest(String webRequestXml)  throws JAXBException {
		com.fb.platform.egv.to.CreateRequest coreRequest = new com.fb.platform.egv.to.CreateRequest();
		try{
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.CreateRequest webRequest = (com.fb.platform.egv._1_0.CreateRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setAmount(webRequest.getAmount());
			coreRequest.setEmail(webRequest.getEmail());
			coreRequest.setOrderItemId(webRequest.getOrderItemId());

		}catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}
			
	public static String coreResponseToXml(com.fb.platform.egv.to.CreateResponse coreResponse) throws JAXBException,DatatypeConfigurationException{
		
	com.fb.platform.egv._1_0.CreateResponse webResponse = new com.fb.platform.egv._1_0.CreateResponse();
	
	// Mapping Code
	webResponse.setCreateResponseStatus(com.fb.platform.egv._1_0.CreateResponseStatusEnum.fromValue(coreResponse.getResponseStatus().toString()));

	StringWriter outStringWriter = new StringWriter();
	Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
	marshaller.marshal(coreResponse, outStringWriter);

	return outStringWriter.toString();

	}

}
