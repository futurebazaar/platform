package com.fb.api.promotion;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.api.APIRequest;
import com.fb.api.APIResponse;
import com.fb.api.promotion.impl.PromotionManagerImpl;
import com.fb.commons.promotion.to.PromotionTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * 
 * @author Keith Fernandez
 *
 */

@Path("/promotions/")
public class PromotionAPIResource {
 
	@Context HttpServletRequest servletRequest;
	
	private PromotionManager promotionManager;

	public void setPromotionManager(PromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	private static final JsonParser jsonParser = new JsonParser();

	@GET
	@Path("/getActivePromotions/")
	public String getActivePromotions() throws Exception {
		APIRequest request = APIRequest.createAPIRequest(servletRequest);
		request.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		/*APIResponse response = promotionManager.getActivePromotions(request);
		String res = response.toJsonString();
		return res;*/
		return "{key=vaue}";
		//return null;
	}
	
	/*@GET
	@Path("/getbyid/")
	public String getPromotionsById() throws Exception {
		APIRequest request = APIRequest.createAPIRequest(servletRequest);
		request.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		APIResponse response = promotionManager.getPromotionsById(request);
		String res = response.toJsonString();
		return res;
	}*/

	@GET
	@Path("/getbyid/{promotionId}")
	public String getPromotionById(@PathParam("promotionId") String promotionId) throws Exception {
		PromotionTO promotion = promotionManager.getPromotion(Integer.parseInt(promotionId));
		System.out.println(promotion.toString());
		return promotion.toString();
	}

	@GET
	@Path("/getbyorderid/")
	public String getPromotionsByOrderId() throws Exception {
		APIRequest request = APIRequest.createAPIRequest(servletRequest);
		request.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		/*APIResponse response = promotionManager.getPromotionsByOrderId(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
 
	@DELETE 
	@Path("/deletebyid/")
	public String deleteById() throws Exception {
		APIRequest request = APIRequest.createAPIRequest(servletRequest);
		request.setHttpMethod(APIRequest.HTTP_METHOD_DELETE);
		
		/*APIResponse response = promotionManager.deleteById(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}

	@PUT
	@Path("/add/")
	public String add(String postData) throws Exception{
		
		JsonObject jsonObject = (JsonObject)jsonParser.parse(postData);
		APIRequest request = APIRequest.createAPIRequest(servletRequest);
		request.setPostData(jsonObject);
		request.setHttpMethod(APIRequest.HTTP_METHOD_POST);
		
		/*APIResponse response = promotionManager.add(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}

	@POST
	@Path("/update/")
	public String update(String postData) throws Exception{
		APIRequest request = APIRequest.createAPIRequest(servletRequest,postData);
		/*APIResponse response = promotionManager.update(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
	
	@POST
	@Path("/getApplicablePromotionsForOrder/")
	public String getApplicablePromotionsForOrder(String postData) throws Exception{
		APIRequest request = APIRequest.createAPIRequest(servletRequest,postData);
		/*APIResponse response = promotionManager.getApplicablePromotionsForOrder(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
	
	@POST
	@Path("/applyPromotionOnOrder/")
	public String applyPromotionOnOrder(String postData) throws Exception{
		APIRequest request = APIRequest.createAPIRequest(servletRequest,postData);
		/*APIResponse response = promotionManager.applyPromotionOnOrder(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
	
}
