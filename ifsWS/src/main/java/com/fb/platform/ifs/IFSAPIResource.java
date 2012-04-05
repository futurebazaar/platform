package com.fb.platform.ifs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import com.fb.platform.ifs.manager.IFSManager;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityResponseTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.core.InjectParam;

/**
 * 
 * @author sarvesh
 */
@Path("/ifs/")
public class IFSAPIResource {
 
	@Context HttpServletRequest servletRequest;
	
	private static final JsonParser JSON_PARSER = new JsonParser();
	
	private static Logger log = Logger.getLogger(IFSAPIResource.class); 
	
	@InjectParam
	private IFSManager ifsManager;
	
	@GET
	@Path("/getFulfilmentScanner/")
	public String getFulfilmentInfo() throws Exception {
		servletRequest.getQueryString();
		/*APIRequest request = APIRequest.createAPIRequest(servletRequest);
		request.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		
		IFSTO ifsto = new IFSTO();//IFSJsonTOFactory.fromJson(null);
		APIResultTO art = ifsManager.getFulfilmentInfo(ifsto);
		APIResponse apr = new APIResponse();
		apr.setObjects(art.getObjects());
		apr.setStatusCode(art.getStatusCode());
		apr.setStatusMessage(art.getStatusMessage());
		String res = apr.toJsonString();*/
		return null;
	}
	
	//XXX: This should be a GET request. Use POST when writing to DB
	@POST
	@Path("/postFulfilmentScanner/")
	public String checkArticleServiceability(String postData) throws Exception {
		System.out.println("params :: "+postData);
		
		JsonObject jsonObject = (JsonObject) JSON_PARSER.parse(postData);
		
		//XXX: The call below should ensure that jsonObject is valid
		SingleArticleServiceabilityRequestTO serviceabilityRequestTO = IFSJsonTOFactory.fromJson(jsonObject);

		SingleArticleServiceabilityResponseTO art = ifsManager.getSingleArticleServiceabilityInfo(serviceabilityRequestTO);
		
		String res = art.toJsonString();
		log.info("response :: "+res);
		return res;
	}

}
