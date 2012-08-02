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
public class CreateGVMapper {

	public static com.fb.platform.egv.to.CreateRequest xmlToCoreRequest(String webRequestXml) throws JAXBException {
		com.fb.platform.egv.to.CreateRequest coreRequest = new com.fb.platform.egv.to.CreateRequest();
		try {
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.CreateRequest webRequest = (com.fb.platform.egv._1_0.CreateRequest) unmarshaller
					.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setAmount(webRequest.getAmount());
			coreRequest.setEmail(webRequest.getEmail());
			coreRequest.setOrderItemId(webRequest.getOrderItemId());
			coreRequest.setSenderName(webRequest.getSenderName());
			coreRequest.setReceiverName(webRequest.getReceiverName());
			coreRequest.setGiftMessage(webRequest.getGiftMessage());
			coreRequest.setMobile(webRequest.getMobile());
			coreRequest.setDeferActivation(webRequest.isIsDeferActivation());
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
			throw new JAXBException("Problem in XML Parsing");
		}
		return coreRequest;
	}

	public static String coreResponseToXml(com.fb.platform.egv.to.CreateResponse coreResponse) throws JAXBException,
			DatatypeConfigurationException {

		com.fb.platform.egv._1_0.CreateResponse webResponse = new com.fb.platform.egv._1_0.CreateResponse();
		GregorianCalendar gregCal = new GregorianCalendar();

		// Mapping Code
		webResponse.setCreateResponseStatus(com.fb.platform.egv._1_0.CreateResponseStatusEnum.fromValue(coreResponse
				.getResponseStatus().toString()));
		webResponse.setNumber(coreResponse.getGvNumber());
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
			webResponse.setValidFrom(null);
		}

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(webResponse, outStringWriter);

		return outStringWriter.toString();

	}

}
