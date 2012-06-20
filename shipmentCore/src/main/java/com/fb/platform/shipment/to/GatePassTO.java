package com.fb.platform.shipment.to;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nehaga
 *
 */
public class GatePassTO {
	private ShipmentLSPEnum lspcode = null;
	private String gatePassId;
	
	private List<GatePassItem> gatePassItems = new ArrayList<GatePassItem>();
	
	public ShipmentLSPEnum getLspcode() {
		return lspcode;
	}
	public void setLspcode(ShipmentLSPEnum lspcode) {
		this.lspcode = lspcode;
	}
	public List<GatePassItem> getGatePassItems() {
		return gatePassItems;
	}
	public void setGatePassItems(List<GatePassItem> gatePassItems) {
		this.gatePassItems = gatePassItems;
	}
	public String getGatePassId() {
		return gatePassId;
	}
	public void setGatePassId(String gatePassId) {
		this.gatePassId = gatePassId;
	}
}
