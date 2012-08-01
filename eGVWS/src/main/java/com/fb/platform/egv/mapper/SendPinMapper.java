/**
 * 
 */
package com.fb.platform.egv.mapper;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 * @author keith
 * 
 */
public class SendPinMapper {

	public static com.fb.platform.egv.to.SendPinRequest xmlToCoreRequest(String webRequestXml) throws JAXBException {
		// com.fb.platform.egv.to.SendPinRequest coreRequest = new
		// com.fb.platform.egv.to.SendPinRequest();
		// try {
		// Unmarshaller unmarshaller =
		// GiftVoucherResource.getContext().createUnmarshaller();
		//
		// com.fb.platform.egv._1_0.SendPinRequest webRequest =
		// (com.fb.platform.egv._1_0.SendPinRequest) unmarshaller
		// .unmarshal(new StreamSource(new StringReader(webRequestXml)));
		//
		// // Mapping Code
		// coreRequest.setSessionToken(webRequest.getSessionToken());
		// coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
		// coreRequest.setGiftVoucherPin(webRequest.getGiftVoucherPin());
		//
		// } catch (JAXBException e) {
		// // TODO: handle exception
		// }
		// return coreRequest;
		return null;
	}

	public static String coreResponseToXml(com.fb.platform.egv.to.SendPinResponse coreResponse) throws JAXBException,
			DatatypeConfigurationException {

		// com.fb.platform.egv._1_0.SendPinResponse webResponse = new
		// com.fb.platform.egv._1_0.SendPinResponse();
		//
		// // Mapping Code
		// webResponse.setAmount(coreResponse.getAmount());
		// webResponse.setSendPinResponseStatus(com.fb.platform.egv._1_0.SendPinResponseStatusEnum.fromValue(coreResponse
		// .getResponseStatus().toString()));
		// webResponse.setNumber(coreResponse.getNumber());
		// webResponse.setSessionToken(coreResponse.getSessionToken());
		//
		// StringWriter outStringWriter = new StringWriter();
		// Marshaller marshaller =
		// GiftVoucherResource.getContext().createMarshaller();
		// marshaller.marshal(webResponse, outStringWriter);
		//
		// return outStringWriter.toString();
		return null;
	}

}
