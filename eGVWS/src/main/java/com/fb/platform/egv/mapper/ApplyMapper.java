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
public class ApplyMapper {
	
	public static com.fb.platform.egv.to.ApplyRequest xmlToCoreRequest(String webRequestXml)  throws JAXBException {
		com.fb.platform.egv.to.ApplyRequest coreRequest = new com.fb.platform.egv.to.ApplyRequest();
		try{
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.ApplyRequest webRequest = (com.fb.platform.egv._1_0.ApplyRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			coreRequest.setGiftVoucherPin(webRequest.getGiftVoucherPin());

		}catch (JAXBException e) {
			// TODO: handle exception
		}
//		return coreRequest;
		return null;
	}
			
	public static String coreResponseToXml(com.fb.platform.egv.to.ApplyResponse coreResponse) throws JAXBException,DatatypeConfigurationException{
			
		com.fb.platform.egv._1_0.ApplyResponse webResponse = new com.fb.platform.egv._1_0.ApplyResponse();
		
		// Mapping Code
		webResponse.setAmount(coreResponse.getAmount());
		webResponse.setApplyResponseStatus(com.fb.platform.egv._1_0.ApplyResponseStatusEnum.fromValue(coreResponse.getResponseStatus().toString()));
		webResponse.setNumber(coreResponse.getNumber());
		webResponse.setSessionToken(coreResponse.getSessionToken());

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(coreResponse, outStringWriter);

		return outStringWriter.toString();
	}
	
}

