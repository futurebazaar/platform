/**
 * 
 */
package com.fb.platform.shipment.manager.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.shipment.manager.ShipmentManager;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.GatePassTO;
import com.fb.platform.shipment.to.ShipmentLSPEnum;

/**
 * @author nehaga
 *
 */
public class ShipmentManagerImplTest extends BaseTestCase {
	@Autowired
	ShipmentManager shipmentManager;
	
	private static Properties prop;
	private static Properties lspProp;
	
	@Before
	public void init() {
		prop = new Properties();
		lspProp = new Properties();
		InputStream configPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("ftp_details.properties");
		InputStream lspPropertiesStream = this.getClass().getClassLoader().getResourceAsStream("lsp_configurations.properties");
		try {
			prop.load(configPropertiesStream);
			lspProp.load(lspPropertiesStream);
			deleteFiles();
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	@Test
	public void generateAramexOutboundFile() {
		GatePassTO gatePass = new GatePassTO();
		gatePass.setGatePassId("aramexGatePass");
		gatePass.setLspcode(ShipmentLSPEnum.Aramex);
		
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setAwbNo("AR0653351");
		deliveryItem.setDeece("2786-BHIWANDI");
		deliveryItem.setDelNo("200");
		deliveryItem.setDelWt(new BigDecimal(0.500));
		deliveryItem.setItemDescription("description 0");
		deliveryItem.setOrderReferenceId("20100");
		deliveryItem.setQuantity(1);
		
		List<GatePassItem> deliveries = new ArrayList<GatePassItem>();
		deliveries.add(deliveryItem);
		
		gatePass.setGatePassItems(deliveries);
		
		shipmentManager.generateOutboundFile(gatePass);
		
		String extension = lspProp.getProperty("aramex.outbound.extension");
		String prefix = lspProp.getProperty("aramex.outbound.fileNamePrefix");
		
		File[] uploadFiles = getFiles(prop.getProperty("future.server.outbound.aramex.ftp.uploaded"));
		assertEquals(1, uploadFiles.length);
		assertTrue(uploadFiles[0].getName().contains(extension));
		assertTrue(uploadFiles[0].getName().contains(prefix));
		
		File[] mailedFiles = getFiles(prop.getProperty("future.server.outbound.aramex.mail.sent"));
		assertEquals(1, mailedFiles.length);
		assertTrue(mailedFiles[0].getName().contains(extension));
		assertTrue(mailedFiles[0].getName().contains(prefix));
		
	}
	
	@Test
	public void generateBlueDartOutboundFile() {
		GatePassTO gatePass = new GatePassTO();
		gatePass.setGatePassId("bluedartGatePass");
		gatePass.setLspcode(ShipmentLSPEnum.BlueDart);
		
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setAwbNo("BB0653351");
		deliveryItem.setDeece("2786-BHIWANDI");
		deliveryItem.setDelNo("300");
		deliveryItem.setDelWt(new BigDecimal(0.500));
		deliveryItem.setItemDescription("description 0");
		deliveryItem.setOrderReferenceId("20200");
		deliveryItem.setQuantity(1);
		
		List<GatePassItem> deliveries = new ArrayList<GatePassItem>();
		deliveries.add(deliveryItem);
		
		gatePass.setGatePassItems(deliveries);
		
		shipmentManager.generateOutboundFile(gatePass);
		
		String extension = lspProp.getProperty("blueDart.outbound.extension");
		String prefix = lspProp.getProperty("blueDart.outbound.fileNamePrefix");
		
		File[] uploadFiles = getFiles(prop.getProperty("future.server.outbound.blueDart.ftp.uploaded"));
		assertEquals(1, uploadFiles.length);
		assertTrue(uploadFiles[0].getName().contains(extension));
		assertTrue(uploadFiles[0].getName().contains(prefix));
		
		File[] mailedFiles = getFiles(prop.getProperty("future.server.outbound.blueDart.mail.sent"));
		assertEquals(1, mailedFiles.length);
		assertTrue(mailedFiles[0].getName().contains(extension));
		assertTrue(mailedFiles[0].getName().contains(prefix));
		
	}
	
	@Test
	public void generateExpressItOutboundFile() {
		GatePassTO gatePass = new GatePassTO();
		gatePass.setGatePassId("expressItGatePass");
		gatePass.setLspcode(ShipmentLSPEnum.ExpressIt);
		
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setAwbNo("EI0653351");
		deliveryItem.setDeece("2786-BHIWANDI");
		deliveryItem.setDelNo("400");
		deliveryItem.setDelWt(new BigDecimal(0.500));
		deliveryItem.setItemDescription("description 0");
		deliveryItem.setOrderReferenceId("20300");
		deliveryItem.setQuantity(1);
		
		List<GatePassItem> deliveries = new ArrayList<GatePassItem>();
		deliveries.add(deliveryItem);
		
		gatePass.setGatePassItems(deliveries);
		
		shipmentManager.generateOutboundFile(gatePass);
		
		String extension = lspProp.getProperty("expressIt.outbound.extension");
		String prefix = lspProp.getProperty("expressIt.outbound.fileNamePrefix");
		
		File[] uploadFiles = getFiles(prop.getProperty("future.server.outbound.expressIt.ftp.uploaded"));
		assertEquals(1, uploadFiles.length);
		assertTrue(uploadFiles[0].getName().contains(extension));
		assertTrue(uploadFiles[0].getName().contains(prefix));
		
		File[] mailedFiles = getFiles(prop.getProperty("future.server.outbound.expressIt.mail.sent"));
		assertEquals(1, mailedFiles.length);
		assertTrue(mailedFiles[0].getName().contains(extension));
		assertTrue(mailedFiles[0].getName().contains(prefix));
		
	}
	
	@Test
	public void generateFirstFlightOutboundFile() {
		GatePassTO gatePass = new GatePassTO();
		gatePass.setGatePassId("firstFlightGatePass");
		gatePass.setLspcode(ShipmentLSPEnum.FirstFlight);
		
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setAwbNo("FF0653351");
		deliveryItem.setDeece("2786-BHIWANDI");
		deliveryItem.setDelNo("500");
		deliveryItem.setDelWt(new BigDecimal(0.500));
		deliveryItem.setItemDescription("description 0");
		deliveryItem.setOrderReferenceId("20400");
		deliveryItem.setQuantity(1);
		
		List<GatePassItem> deliveries = new ArrayList<GatePassItem>();
		deliveries.add(deliveryItem);
		
		gatePass.setGatePassItems(deliveries);
		
		shipmentManager.generateOutboundFile(gatePass);
		
		String extension = lspProp.getProperty("firstFlight.outbound.extension");
		String prefix = lspProp.getProperty("firstFlight.outbound.fileNamePrefix");
		
		File[] uploadFiles = getFiles(prop.getProperty("future.server.outbound.firstFlight.ftp.uploaded"));
		assertEquals(1, uploadFiles.length);
		assertTrue(uploadFiles[0].getName().contains(extension));
		assertTrue(uploadFiles[0].getName().contains(prefix));
		
		File[] mailedFiles = getFiles(prop.getProperty("future.server.outbound.firstFlight.mail.sent"));
		assertEquals(1, mailedFiles.length);
		assertTrue(mailedFiles[0].getName().contains(extension));
		assertTrue(mailedFiles[0].getName().contains(prefix));
		
	}
	
	@Test
	public void generateQuantiumOutboundFile() {
		GatePassTO gatePass = new GatePassTO();
		gatePass.setGatePassId("quantiumGatePass");
		gatePass.setLspcode(ShipmentLSPEnum.Quantium);
		
		GatePassItem deliveryItem = new GatePassItem();
		deliveryItem.setAwbNo("M0653351");
		deliveryItem.setDeece("2786-BHIWANDI");
		deliveryItem.setDelNo("100");
		deliveryItem.setDelWt(new BigDecimal(0.500));
		deliveryItem.setItemDescription("description 0");
		deliveryItem.setOrderReferenceId("20000");
		deliveryItem.setQuantity(1);
		
		List<GatePassItem> deliveries = new ArrayList<GatePassItem>();
		deliveries.add(deliveryItem);
		
		gatePass.setGatePassItems(deliveries);
		
		shipmentManager.generateOutboundFile(gatePass);
		
		String extension = lspProp.getProperty("quantium.outbound.extension");
		String prefix = lspProp.getProperty("quantium.outbound.fileNamePrefix");
		
		File[] uploadFiles = getFiles(prop.getProperty("future.server.outbound.quantium.ftp.uploaded"));
		assertEquals(1, uploadFiles.length);
		assertTrue(uploadFiles[0].getName().contains(extension));
		assertTrue(uploadFiles[0].getName().contains(prefix));
		
		File[] mailedFiles = getFiles(prop.getProperty("future.server.outbound.quantium.mail.sent"));
		assertEquals(1, mailedFiles.length);
		assertTrue(mailedFiles[0].getName().contains(extension));
		assertTrue(mailedFiles[0].getName().contains(prefix));
		
	}
	
	private File[] getFiles(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
	
	@After
	public void destroy() {
		deleteFiles();
	}
	
	private void deleteFiles() {
		deleteFiles(prop.getProperty("future.server.outbound.aramex.ftp.uploaded"));
		deleteFiles(prop.getProperty("future.server.outbound.aramex.mail.sent"));
		
		deleteFiles(prop.getProperty("future.server.outbound.blueDart.ftp.uploaded"));
		deleteFiles(prop.getProperty("future.server.outbound.blueDart.mail.sent"));
		
		deleteFiles(prop.getProperty("future.server.outbound.expressIt.ftp.uploaded"));
		deleteFiles(prop.getProperty("future.server.outbound.expressIt.mail.sent"));
		
		deleteFiles(prop.getProperty("future.server.outbound.firstFlight.ftp.uploaded"));
		deleteFiles(prop.getProperty("future.server.outbound.firstFlight.mail.sent"));
		
		deleteFiles(prop.getProperty("future.server.outbound.quantium.ftp.uploaded"));
		deleteFiles(prop.getProperty("future.server.outbound.quantium.mail.sent"));
	}
	
	private void deleteFiles(String path) {
		File[] files = getFiles(path);
		for(File file : files) {
			if(file.isFile()) {
				file.delete();
			}
		}
	}
}
