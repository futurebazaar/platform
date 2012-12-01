package com.fb.platform.sap.client.handler;

import java.util.List;

import com.fb.platform.sap.bapi.to.SapInventoryDashboardRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryDashboardResponseTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelRequestTO;
import com.fb.platform.sap.bapi.to.SapInventoryLevelResponseTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateRequestTO;
import com.fb.platform.sap.bapi.to.SapLspAwbUpdateResponseTO;
import com.fb.platform.sap.bapi.to.SapOrderRequestTO;
import com.fb.platform.sap.bapi.to.SapOrderResponseTO;
import com.fb.platform.sap.client.commons.SapResponseStatus;

public interface PlatformClientHandler {
	
	public SapOrderResponseTO processOrder(SapOrderRequestTO orderRequestTO);
	
	public List<SapInventoryDashboardResponseTO> processInventoryDashboard(SapInventoryDashboardRequestTO inventoryDashboardRequestTO);
	
	public SapInventoryLevelResponseTO processInventoryLevel(SapInventoryLevelRequestTO inventoryLevelRequestTO);
	
	public SapLspAwbUpdateResponseTO processLspAwbUpdate(SapLspAwbUpdateRequestTO lspAwbUpdateRequestTO);
	
	public SapResponseStatus sendIdoc(String idocXml);

}
