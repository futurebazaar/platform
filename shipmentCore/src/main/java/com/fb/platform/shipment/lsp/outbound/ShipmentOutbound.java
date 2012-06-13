package com.fb.platform.shipment.lsp.outbound;

import java.util.List;

import com.fb.commons.mail.MailSender;
import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public interface ShipmentOutbound {
	
	public void generateOutboundFile(List<ParcelItem> parcelItem);
	
	public void uploadOutboundFile();
	
	public void mailOutboundFile(MailSender mailSender);
}
