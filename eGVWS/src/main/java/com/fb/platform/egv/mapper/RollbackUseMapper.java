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
public class RollbackUseMapper {
	
	public static com.fb.platform.egv.to.RollbackUseRequest xmlToCoreRequest(String webRequestXml)  throws JAXBException {
		com.fb.platform.egv.to.RollbackUseRequest coreRequest = new com.fb.platform.egv.to.RollbackUseRequest();
		try{
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.RollbackUseRequest webRequest = (com.fb.platform.egv._1_0.RollbackUseRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			coreRequest.setOrderId(webRequest.getOrderId());

		}catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}
			
	public static String coreResponseToXml(com.fb.platform.egv.to.RollbackUseResponse coreResponse) throws JAXBException,DatatypeConfigurationException{
			
		com.fb.platform.egv._1_0.RollbackUseResponse webResponse = new com.fb.platform.egv._1_0.RollbackUseResponse();
		
		// Mapping Code
		webResponse.setRollbackUseResponseStatus(com.fb.platform.egv._1_0.RollbackUseResponseStatusEnum.fromValue(coreResponse.getResponseStatus().toString()));
		webResponse.setSessionToken(coreResponse.getSessionToken());
		webResponse.setGiftVoucherNumber(coreResponse.getNumber());

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(webResponse, outStringWriter);

		return outStringWriter.toString();
	}
	
}

