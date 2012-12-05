package com.fb.platform.sap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.platform.sap.client.commons.SapResponseStatus;
import com.fb.platform.sap.client.handler.PlatformClientHandler;

/**
 * @author anubhav
 *
 */

@Path("/wallet")
@Component
@Scope("request")
public class WalletResource {
	
	@Autowired
	private PlatformClientHandler sapClientHandler = null;
	
	@Path("/sendToSap")
	@POST
	public String sendWalletToSap(String walletIdocXml) {
		SapResponseStatus response = sapClientHandler.sendIdoc(walletIdocXml);
		return response.toString();
	}

}
