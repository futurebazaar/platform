package com.fb.platform.shipment.lsp.outbound;

import java.util.List;

import com.fb.platform.shipment.to.ParcelItem;

/**
 * @author nehaga
 *
 */
public interface ShipmentOutbound {
	public boolean generateOutboundFile(List<ParcelItem> parcelItem);
}
