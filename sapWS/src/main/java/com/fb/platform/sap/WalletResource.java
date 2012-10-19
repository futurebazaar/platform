package com.fb.platform.sap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
	
	private PlatformClientHandler sapClientHandler = null;
	
	public void setSapClientHandler(PlatformClientHandler sapClientHandler) {
		this.sapClientHandler = sapClientHandler;
	}
	
	@Path("/sendToSap")
	@POST
	public String sendWalletToSap(String walletIdocXml) {
		SapResponseStatus response = sapClientHandler.sendIdoc(walletIdocXml);
		return response.toString();
	}

}
