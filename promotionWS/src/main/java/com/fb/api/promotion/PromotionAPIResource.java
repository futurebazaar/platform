package com.fb.api.promotion;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * 
 * @author Keith Fernandez
 *
 */

@Path("/promotion/")
@Component
@Scope("request")
public class PromotionAPIResource {
 
	private static Logger logger = Logger.getLogger(PromotionAPIResource.class);

	public String applyCoupon() {
		return null;
	}
}
