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
public class SendPinMapper {

	public static com.fb.platform.egv.to.SendPinRequest xmlToCoreRequest(String webRequestXml) throws JAXBException {
		com.fb.platform.egv.to.SendPinRequest coreRequest = new com.fb.platform.egv.to.SendPinRequest();
		try {
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.SendPinRequest webRequest = (com.fb.platform.egv._1_0.SendPinRequest) unmarshaller
					.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			coreRequest.setMobile(webRequest.getMobile());
			coreRequest.setEmail(webRequest.getEmail());
			coreRequest.setReceiverName(webRequest.getReceiverName());
			coreRequest.setSenderName(webRequest.getSenderName());
			coreRequest.setGiftMessage(webRequest.getGiftMessage());

		} catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}

	public static String coreResponseToXml(com.fb.platform.egv.to.SendPinResponse coreResponse) throws JAXBException,
			DatatypeConfigurationException {

		com.fb.platform.egv._1_0.SendPinResponse webResponse = new com.fb.platform.egv._1_0.SendPinResponse();

		// Mapping Code

		webResponse.setSendPinResponseStatus(com.fb.platform.egv._1_0.SendPinResponseStatusEnum.fromValue(coreResponse
				.getResponseStatus().toString()));
		webResponse.setNumber(coreResponse.getNumber());
		webResponse.setSessionToken(coreResponse.getSessionToken());

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(webResponse, outStringWriter);

		return outStringWriter.toString();
	}

}
