package com.fb.platform.sap.test;

import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.joda.time.DateTime;

import com.fb.platform.sap._1_0.AwbUpdateRequest;
import com.fb.platform.sap._1_0.InventoryDashboardRequest;
import com.fb.platform.sap._1_0.InventoryLevelRequest;

public class TestSapResource {
	
	public String getInventoryLevelXml() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.sap._1_0");
		InventoryLevelRequest request = new InventoryLevelRequest();
		request.setMaterial("300000560");
		request.setStorageLocation(10);
		request.setPlant("2786");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
	}
	
	public String getInventoryDashboardXml() throws JAXBException, DatatypeConfigurationException {
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.sap._1_0");
		InventoryDashboardRequest request = new InventoryDashboardRequest();
		request.setArticle("000000000300000560");
		GregorianCalendar gregCal = new DateTime(2012, 9, 4, 0, 0, 0).toGregorianCalendar();
		request.setFromDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		//request.setToDateTime(new DateTime(2012, 9, 13, 23, 59, 59));
		gregCal = DateTime.now().toGregorianCalendar();
		request.setToDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		request.setPlant("");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
	}
	
	public String getLspAwbUpdateXml() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("com.fb.platform.sap._1_0");
		AwbUpdateRequest request = new AwbUpdateRequest();
		request.setAwb("1234");
		request.setDeliveryNumber("8000018246");
		request.setLspCode("1234");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
		
	}

}
