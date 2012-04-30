package com.fb.platform.shipment.to;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nehaga
 * This enum is required to map the lsp name with its id.
 * This list is maintained in database, but needs to be modified manually to reflect the supported lsp and their codes. 
 *
 */
public enum ShipmentLSPEnum  implements Serializable {
	
	Quantium("325190"),
	FirstFlight("300437"),
	Aramex("305240"),
	BlueDart("300413");
	
	private String code = null;
	
	/**
	 * This static block is called before the initialization of the enum to create a map between the lsp code and name.
	 */
	private static final Map<String, ShipmentLSPEnum> lookup = new HashMap<String, ShipmentLSPEnum>();
	static {
		for(ShipmentLSPEnum shipmentLSPEnum : ShipmentLSPEnum.values()) {
			lookup.put(shipmentLSPEnum.getCode(), shipmentLSPEnum);
		}
	}
	
	private ShipmentLSPEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public static ShipmentLSPEnum getLSP(String code) {
		return lookup.get(code);
	}
	
	
}
