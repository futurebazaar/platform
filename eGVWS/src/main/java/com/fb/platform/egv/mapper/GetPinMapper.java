/**
 * 
 */
package com.fb.platform.egv.mapper;

import java.io.StringWriter;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import com.fb.platform.egv.resource.GiftVoucherResource;

/**
 * @author keith
 * 
 */
public class GetPinMapper {

	public static com.fb.platform.egv.to.GetPinRequest xmlToCoreRequest(String webRequestXml) throws JAXBException {
		com.fb.platform.egv.to.GetPinRequest coreRequest = new com.fb.platform.egv.to.GetPinRequest();
		try {
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			// com.fb.platform.egv._1_0.GetPinRequest webRequest =
			// (com.fb.platform.egv._1_0.GetPinRequest) unmarshaller
			// .unmarshal(new StreamSource(new StringReader(webRequestXml)));
			//
			// // Mapping Code
			// coreRequest.setSessionToken(webRequest.getSessionToken());
			// coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());

		} catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}

	public static String coreResponseToXml(com.fb.platform.egv.to.GetPinResponse coreResponse) throws JAXBException,
			DatatypeConfigurationException {

		// com.fb.platform.egv._1_0.GetPinResponse webResponse = new
		// com.fb.platform.egv._1_0.GetPinResponse();

		// Mapping Code

		// webResponse.setSendPinResponseStatus(com.fb.platform.egv._1_0.GetPinResponseStatusEnum.fromValue(coreResponse
		// .getResponseStatus().toString()));
		// webResponse.setNumber(coreResponse.getNumber());
		// webResponse.setNumber(coreResponse.getPin());
		// webResponse.setSessionToken(coreResponse.getSessionToken());

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		// marshaller.marshal(webResponse, outStringWriter);

		return outStringWriter.toString();
	}

}
