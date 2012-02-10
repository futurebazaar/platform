package com.fb.api.risk.ebs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fb.api.APIRequest;
import com.fb.api.APIResponse;
import com.fb.api.promotion.impl.PromotionManagerImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/ebs/")
public class EBSAPIResource {
	
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	private static final PromotionManagerImpl promotionManager = (PromotionManagerImpl) context.getBean("promotionManager");
	private static final JsonParser jsonParser = new JsonParser();
	
	@GET
	@Path("/callEBS/")
	public String getActivePromotions() throws Exception {
		
		String res = "THIS IS EBS EXAMPLE";
		System.out.println(res);
		return res;
	}

}
