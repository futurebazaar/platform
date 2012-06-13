package com.fb.platform.shipment.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.ftp.exception.FTPConnectException;
import com.fb.commons.ftp.exception.FTPDisconnectException;
import com.fb.commons.ftp.exception.FTPLoginException;
import com.fb.commons.ftp.exception.FTPLogoutException;
import com.fb.commons.ftp.exception.FTPUploadException;
import com.fb.commons.mail.MailSender;
import com.fb.platform.shipment.exception.OutboundFileCreationException;
import com.fb.platform.shipment.lsp.impl.AramexLSP;
import com.fb.platform.shipment.lsp.impl.BlueDartLSP;
import com.fb.platform.shipment.lsp.impl.FirstFlightLSP;
import com.fb.platform.shipment.lsp.impl.QuantiumLSP;
import com.fb.platform.shipment.manager.ShipmentManager;
import com.fb.platform.shipment.service.ShipmentService;
import com.fb.platform.shipment.to.GatePassItem;
import com.fb.platform.shipment.to.GatePassTO;
import com.fb.platform.shipment.to.ParcelItem;
import com.fb.platform.shipment.to.ShipmentLSPEnum;
import com.fb.platform.shipment.util.ShipmentProcessor;

/**
 * @author nehaga
 * This is the manager Interface implementor, it is the entry point in this project.
 * Any module that wants to convert the gatePass.xml to outbound files has to get an instance of this class.
 *
 */
public class ShipmentManagerImpl implements ShipmentManager{
	
	private static Log infoLog = LogFactory.getLog("LOGINFO");
	
	private static Log errorLog = LogFactory.getLog("LOGERROR");
	
	@Autowired
	private ShipmentService shipmentService = null;
	
	@Autowired
	private MailSender mailSender;
	
	/**
	 * This function accepts a list of gate pass delivery items and processes it to create outbound files.
	 * @param gatePassString
	 */
	@Override
	public void generateOutboundFile(GatePassTO gatePass) {
		if(gatePass.getLspcode() == null) {
			errorLog.error("Incorrect LSP code");
		} else {
			List<ParcelItem> parcelList = createParcelList(gatePass);
			ShipmentProcessor processor = getProcessor(gatePass.getLspcode());
			if(parcelList != null && parcelList.size() > 0) {
				processParcels(parcelList, processor);
			}
		}
		
	}
	
	/**
	 * This function converts the JAXB objects to ShipmentCore internal objects and splits the parcels items list into separate lsp lists.
	 * This function calls the service layer and fetches the required information from the database to generate ParcelItem object. 
	 * @param ordersList
	 */
	private List<ParcelItem> createParcelList(GatePassTO gatePass) {
		List<ParcelItem> parcelList = new ArrayList<ParcelItem>();
		for(GatePassItem gatePassItem : gatePass.getGatePassItems()) {
			infoLog.debug("Gate Pass item : " + gatePassItem.toString());
			ParcelItem parcelItem = shipmentService.getParcelDetails(gatePassItem);
			if(parcelItem != null) {
				infoLog.debug("Parcel Item Data : " + parcelItem.toString());
				parcelList.add(parcelItem);
			}
		}
		return parcelList;
	}
	
	/**
	 * This function creates instances for each lsp processor and passes them the list of parcels.
	 * It is the responsibility of the processor to create the outbound file in the desired format, save it in the expected extension and
	 * deliver it to the lsp.
	 */
	
	private ShipmentProcessor getProcessor(ShipmentLSPEnum lsp) {
		ShipmentProcessor processor = null;
		switch (lsp) {
		case Aramex:
			processor =  new ShipmentProcessor(new AramexLSP());
			break;
		case BlueDart:
			processor = new ShipmentProcessor(new BlueDartLSP());
			break;
		case FirstFlight:
			processor = new ShipmentProcessor(new FirstFlightLSP());
			break;
		case Quantium:
			processor = new ShipmentProcessor(new QuantiumLSP());
			break;
		default:
			break;
		}
		return processor;
	}
	
	private void processParcels(List<ParcelItem> parcelList, ShipmentProcessor processor) {
		try {
			processor.generateOutboundFile(parcelList);
			processor.uploadOutboundFile();
			processor.mailOutboundFile(mailSender);
		} catch (FTPConnectException e) {
			errorLog.error("FTP connection error", e);
		} catch (FTPLoginException e) {
			errorLog.error("FTP login error", e);
		} catch (FTPLogoutException e) {
			errorLog.error("FTP logout error", e);
		} catch (FTPDisconnectException e) {
			errorLog.error("FTP disconnect error", e);
		} catch (FTPUploadException e) {
			errorLog.error("FTP upload error", e);
		} catch (OutboundFileCreationException e) {
			errorLog.error("Outbound file creation error", e);
		}
	}
	
	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

}
