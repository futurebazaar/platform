package com.fb.platform.shipment.lsp.impl;

import com.fb.platform.shipment.lsp.ShipmentLSP;
import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.lsp.outbound.impl.ExpressItOutboundImpl;

/**
 * @author nehaga
 *
 */
public class ExpressItLSP implements ShipmentLSP {

	@Override
	public ShipmentOutbound outboundProcessor() {
		return new ExpressItOutboundImpl();
	}

}
