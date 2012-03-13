package com.fb.api.promotion;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.promotion.to.OrderTO;
import com.fb.platform.promotion.impl.PromotionManagerImpl;
import com.fb.platform.promotion.interfaces.PromotionManager;
import com.fb.platform.promotion.to.PromotionTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.core.InjectParam;


/**
 * 
 * @author Keith Fernandez
 *
 */

@Path("/promotions/")
@Component
@Scope("request")
public class PromotionAPIResource {
 
	private static Logger logger = Logger.getLogger(PromotionAPIResource.class);
	
	 @InjectParam
	private PromotionManager promotionManager;
	 
	//JAXBContext class is thread safe and can be shared
		private static final JAXBContext context = initContext();
		
		private static JAXBContext initContext() {
			try {
				return JAXBContext.newInstance("com.fb.platform.auth._1_0");
			} catch (JAXBException e) {
				logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
				throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
			}
		}

	public void setPromotionManager(PromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	private static final JsonParser jsonParser = new JsonParser();

	@Context
    UriInfo uriInfo;

 
	@GET
	@Produces("text/xml")
	@Path("/getActivePromotions/")
	public String getActivePromotions() throws Exception {
//		APIRequest request = APIRequest.createAPIRequest(servletRequest);
//		request.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		/*APIResponse response = promotionManager.getActivePromotions(request);
		String res = response.toJsonString();
		return res;*/
		return "<promotion>" + ""+ "</promotion>";
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

	@POST
	@Consumes("application/xml")
	@Produces("application/xml")
	@Path("/getbyorderid/")
	public String getPromotionsByOrderId(String orderDetailsXml) throws Exception {
//		APIRequest request = APIRequest.createAPIRequest(servletRequest);
//		request.setHttpMethod(APIRequest.HTTP_METHOD_GET);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		OrderTO xmlLogoutReq = (OrderTO) unmarshaller.unmarshal(new StreamSource(new StringReader(orderDetailsXml)));

		/*APIResponse response = promotionManager.getPromotionsByOrderId(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
 
	@DELETE 
	@Path("/deletebyid/")
	public String deleteById() throws Exception {
//		APIRequest request = APIRequest.createAPIRequest(servletRequest);
//		request.setHttpMethod(APIRequest.HTTP_METHOD_DELETE);
		
		/*APIResponse response = promotionManager.deleteById(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}

	@PUT
	@Path("/add/")
	public String add(String postData) throws Exception{
		
//		JsonObject jsonObject = (JsonObject)jsonParser.parse(postData);
//		APIRequest request = APIRequest.createAPIRequest(servletRequest);
//		request.setPostData(jsonObject);
//		request.setHttpMethod(APIRequest.HTTP_METHOD_POST);
		
		/*APIResponse response = promotionManager.add(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}

	@POST
	@Path("/update/")
	public String update(String postData) throws Exception{
//		APIRequest request = APIRequest.createAPIRequest(servletRequest,postData);
		/*APIResponse response = promotionManager.update(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
	
	@POST
	@Path("/getApplicablePromotionsForOrder/")
	public String getApplicablePromotionsForOrder(String postData) throws Exception{
//		APIRequest request = APIRequest.createAPIRequest(servletRequest,postData);
		/*APIResponse response = promotionManager.getApplicablePromotionsForOrder(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
	
	@POST
	@Path("/applyPromotionOnOrder/")
	public String applyPromotionOnOrder(String postData) throws Exception{
//		APIRequest request = APIRequest.createAPIRequest(servletRequest,postData);
		/*APIResponse response = promotionManager.applyPromotionOnOrder(request);
		String res = response.toJsonString();
		return res;*/
		return null;
	}
	
}
