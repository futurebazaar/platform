package com.fb.platform.shipment.manager;

import java.util.List;

import com.fb.platform.shipment.to.GatePassItem;


/**
 * @author nehaga
 * This is the manager Interface, it is the entry point in this project.
 * Any module that wants to convert the gatePass.xml to outbound files has to get an instance of this interface implementor.
 *
 */
public interface ShipmentManager {
	
	/**
	 * This function accepts a list of gate pass delivery items and processes it to create outbound files.
	 * @param gatePassString
	 */
	public boolean generateOutboundFile(List<GatePassItem> deliveryList);
}
