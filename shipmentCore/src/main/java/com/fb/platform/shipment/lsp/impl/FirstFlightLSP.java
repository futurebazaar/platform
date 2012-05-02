package com.fb.platform.shipment.lsp.impl;

import com.fb.platform.shipment.lsp.ShipmentLSP;
import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.lsp.outbound.impl.FirstFlightOutboundImpl;

/**
 * @author nehaga
 *
 */
public class FirstFlightLSP implements ShipmentLSP {

	@Override
	public ShipmentOutbound outboundProcessor() {
		return new FirstFlightOutboundImpl();
	}

}
