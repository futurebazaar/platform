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

import com.fb.platform.egv._1_0.GiftVoucherDetails;
import com.fb.platform.egv.resource.GiftVoucherResource;

/**
 * @author keith
 * 
 */
public class UseMapper {

	public static com.fb.platform.egv.to.UseRequest xmlToCoreRequest(String webRequestXml) throws JAXBException {
		com.fb.platform.egv.to.UseRequest coreRequest = new com.fb.platform.egv.to.UseRequest();
		try {
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.UseRequest webRequest = (com.fb.platform.egv._1_0.UseRequest) unmarshaller
					.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			// coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			// coreRequest.setAmount(webRequest.getAmount());
			for (GiftVoucherDetails gv : webRequest.getGiftVoucherDetails()) {
				coreRequest.addGiftVoucherDetails(gv.getGiftVoucherNumber(), gv.getAmount());
			}
			coreRequest.setOrderId(webRequest.getOrderId());

		} catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}
	public static String coreResponseToXml(com.fb.platform.egv.to.UseResponse coreResponse) throws JAXBException,
			DatatypeConfigurationException {

		com.fb.platform.egv._1_0.UseResponse webResponse = new com.fb.platform.egv._1_0.UseResponse();

		// Mapping Code
		webResponse.setUseResponseStatus(com.fb.platform.egv._1_0.UseResponseStatusEnum.fromValue(coreResponse
				.getResponseStatus().toString()));
		webResponse.setSessionToken(coreResponse.getSessionToken());

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(webResponse, outStringWriter);

		return outStringWriter.toString();
	}

}
