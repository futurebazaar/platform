/**
 * 
 */
package com.fb.platform.egv.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.fb.platform.egv._1_0.GetInfoRequest;
import com.fb.platform.egv.resource.GiftVoucherResource;
import com.fb.platform.egv.to.GetInfoResponseStatusEnum;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


/**
 * @author keith
 *
 */
public class GetInfoMapper {
	
	public static com.fb.platform.egv.to.GetInfoRequest xmlToCoreRequest(String webRequestXml)  throws JAXBException {
		com.fb.platform.egv.to.GetInfoRequest coreRequest = new com.fb.platform.egv.to.GetInfoRequest();
		try{
			Unmarshaller unmarshaller = GiftVoucherResource.getContext().createUnmarshaller();

			com.fb.platform.egv._1_0.GetInfoRequest webRequest = (com.fb.platform.egv._1_0.GetInfoRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(webRequestXml)));

			// Mapping Code
			coreRequest.setSessionToken(webRequest.getSessionToken());
			coreRequest.setGiftVoucherNumber(webRequest.getGiftVoucherNumber());
			coreRequest.setGiftVoucherPin(webRequest.getGiftVoucherPin());

		}catch (JAXBException e) {
			// TODO: handle exception
		}
		return coreRequest;
	}
			
	public static String CoreResponseToXml(com.fb.platform.egv.to.GetInfoResponse coreResponse) throws JAXBException,DatatypeConfigurationException{
			
		com.fb.platform.egv._1_0.GetInfoResponse webResponse = new com.fb.platform.egv._1_0.GetInfoResponse();
		GregorianCalendar gregCal = new GregorianCalendar();
		
		// Mapping Code
		webResponse.setAmount(coreResponse.getAmount());
		webResponse.setEmail(coreResponse.getEmail());
		webResponse.setGetInfoResponseStatus(com.fb.platform.egv._1_0.GetInfoResponseStatusEnum.fromValue(coreResponse.getResponseStatus().toString()));
		webResponse.setGvStatus(coreResponse.getStatus().toString());
		webResponse.setNumber(coreResponse.getNumber());
		webResponse.setOrderItemId(coreResponse.getOrderItemId());
		webResponse.setSessionToken(coreResponse.getSessionToken());
		webResponse.setUserId(coreResponse.getUserId());
		if(coreResponse.getValidFrom() != null) {
			gregCal.set(coreResponse.getValidFrom().getYear(), coreResponse.getValidFrom().getMonthOfYear()-1, coreResponse.getValidFrom().getDayOfMonth(),0,0,0);
			webResponse.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		} else {
			webResponse.setValidFrom(null);
		}
		if(coreResponse.getValidTill() != null) {
			gregCal.set(coreResponse.getValidTill().getYear(), coreResponse.getValidTill().getMonthOfYear()-1, coreResponse.getValidTill().getDayOfMonth(),0,0,0);
			webResponse.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		} else {
			webResponse.setValidFrom(null);
		}

		StringWriter outStringWriter = new StringWriter();
		Marshaller marshaller = GiftVoucherResource.getContext().createMarshaller();
		marshaller.marshal(coreResponse, outStringWriter);

		return outStringWriter.toString();
	}
	
}

