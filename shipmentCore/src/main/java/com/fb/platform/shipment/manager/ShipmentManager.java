package com.fb.platform.shipment.manager;


/**
 * @author nehaga
 * This is the manager Interface, it is the entry point in this project.
 * Any module that wants to convert the gatePass.xml to outbound files has to get an instance of this interface implementor.
 *
 */
public interface ShipmentManager {
	
	/**
	 * This function accepts the gatePass.xml as String and initiates the process of creating the outbound files.  
	 * @param gatePassString
	 */
	public void generateOutboundFile(String gatePassString);
}
