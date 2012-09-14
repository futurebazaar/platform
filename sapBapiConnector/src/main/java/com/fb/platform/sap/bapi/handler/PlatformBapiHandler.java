package com.fb.platform.sap.bapi.handler;

import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;

public interface PlatformBapiHandler {
	
	public SapOrderResponseTO processOrder(String environment, SapOrderRequestTO orderRequestTO);
	
	public SapInventoryDashboardResponseTO processInventoryDashboard(String environment, SapInventoryDashboardRequestTO inventoryDashboardRequestTO);
	
	public SapInventoryLevelResponseTO processInventoryLevel(String environment, SapInventoryLevelRequestTO inventoryLevelRequestTO);
	
	public SapLspAwbUpdateResponseTO processLspAwbUpdate(String environment, SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO);

}
