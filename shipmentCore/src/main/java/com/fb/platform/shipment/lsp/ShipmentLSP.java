package com.fb.platform.shipment.lsp;

import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;


/**
 * @author nehaga
 *
 */
public interface ShipmentLSP {
	public ShipmentOutbound outboundProcessor();
}
