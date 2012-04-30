package com.fb.platform.shipment.lsp.impl;

import com.fb.platform.shipment.lsp.ShipmentLSP;
import com.fb.platform.shipment.lsp.outbound.ShipmentOutbound;
import com.fb.platform.shipment.lsp.outbound.impl.AramexOutboundImpl;

/**
 * @author nehaga
 *
 */
public class AramexLSP implements ShipmentLSP {

	@Override
	public ShipmentOutbound outboundProcessor() {
		return new AramexOutboundImpl();
	}

}
