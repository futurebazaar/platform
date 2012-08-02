/**
 * 
 */
package com.fb.platform.egv.mapper;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;

import com.fb.platform.egv.resource.GiftVoucherResource;

/**
 * @author keith
 * 
 */
public class ActivateMapper {

	public static com.fb.platform.egv.to.ActivateRequest xmlToCoreRequest(String webRequestXml) throws JAXBException {
		com.fb.platform.egv.to.ActivateRequest coreRequest = new com.fb.platform.egv.to.ActivateRequest();
		GregorianCalendar gregCal = new GregorianCalendar();
		try {
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.ActivateRequest webRequest = (com.fb.platform.egv._1_0.ActivateRequest) unmarshaller
					.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			coreRequest.setAmount(webRequest.getAmount());
			if (webRequest.getValidFrom() == null) {
				coreRequest.setValidFrom(null);
			} else {
				coreRequest.setValidFrom(new DateTime(webRequest.getValidFrom().toGregorianCalendar()));
			}
			if (webRequest.getValidTill() == null) {
				coreRequest.setValidTill(null);
			} else {
				coreRequest.setValidTill(new DateTime(webRequest.getValidTill().toGregorianCalendar()));
			}

		} catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}

	public static String coreResponseToXml(com.fb.platform.egv.to.ActivateResponse coreResponse) throws JAXBException,
			DatatypeConfigurationException {

		com.fb.platform.egv._1_0.ActivateResponse webResponse = new com.fb.platform.egv._1_0.ActivateResponse();

		GregorianCalendar gregCal = new GregorianCalendar();

		// Mapping Code
		webResponse.setAmount(coreResponse.getAmount());
		webResponse.setActivateResponseStatus(com.fb.platform.egv._1_0.ActivateResponseStatusEnum
				.fromValue(coreResponse.getResponseStatus().toString()));
		webResponse.setNumber(coreResponse.getNumber());
		webResponse.setSessionToken(coreResponse.getSessionToken());
		if (coreResponse.getValidFrom() != null) {
			gregCal.set(coreResponse.getValidFrom().getYear(), coreResponse.getValidFrom().getMonthOfYear() - 1,
					coreResponse.getValidFrom().getDayOfMonth(), 0, 0, 0);
			webResponse.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		} else {
			webResponse.setValidFrom(null);
		}
		if (coreResponse.getValidTill() != null) {
			gregCal.set(coreResponse.getValidTill().getYear(), coreResponse.getValidTill().getMonthOfYear() - 1,
					coreResponse.getValidTill().getDayOfMonth(), 0, 0, 0);
			webResponse.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		} else {
			webResponse.setValidTill(null);
		}

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(webResponse, outStringWriter);

		return outStringWriter.toString();
	}

}
