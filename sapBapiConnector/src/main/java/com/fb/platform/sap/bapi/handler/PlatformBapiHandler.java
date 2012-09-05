package com.fb.platform.sap.bapi.handler;

import com.fb.platform.sap.bapi.to.BapiTO;
import com.fb.platform.sap.bapi.to.ResponseTO;

public interface PlatformBapiHandler {
	
	public ResponseTO execute(String environment, BapiTO bapiTO);

}
