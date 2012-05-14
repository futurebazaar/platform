package com.fb.platform.promotion.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.commons.to.Money;
import com.fb.platform.promotion.admin._1_0.AssignCouponToUserRequest;
import com.fb.platform.promotion.admin._1_0.AssignCouponToUserResponse;
import com.fb.platform.promotion.admin._1_0.AssignCouponToUserStatusEnum;
import com.fb.platform.promotion.admin._1_0.CreatePromotionEnum;
import com.fb.platform.promotion.admin._1_0.CreatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.CreatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.FetchRuleRequest;
import com.fb.platform.promotion.admin._1_0.FetchRuleResponse;
import com.fb.platform.promotion.admin._1_0.FetchRulesEnum;
import com.fb.platform.promotion.admin._1_0.PromotionTO;
import com.fb.platform.promotion.admin._1_0.PromotionViewTO;
import com.fb.platform.promotion.admin._1_0.RuleConfigDescriptor;
import com.fb.platform.promotion.admin._1_0.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.admin._1_0.RuleConfigDescriptorItem;
import com.fb.platform.promotion.admin._1_0.RuleConfigItemTO;
import com.fb.platform.promotion.admin._1_0.RulesEnum;
import com.fb.platform.promotion.admin._1_0.SearchPromotionEnum;
import com.fb.platform.promotion.admin._1_0.SearchPromotionRequest;
import com.fb.platform.promotion.admin._1_0.SearchPromotionResponse;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionEnum;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.ViewPromotionEnum;
import com.fb.platform.promotion.admin._1_0.ViewPromotionRequest;
import com.fb.platform.promotion.admin._1_0.ViewPromotionResponse;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderByOrder;

/**
 * @author nehaga
 *
 */

@Path("/promotionAdmin")
@Component
@Scope("request")
public class PromotionAdminResource {

	private static Log logger = LogFactory.getLog(PromotionAdminResource.class);
	
	//JAXBContext class is thread safe and can be shared
	private static final JAXBContext context = initContext();
	
	@Autowired
	private PromotionAdminManager promotionAdminManager = null;
	
	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance("com.fb.platform.promotion.admin._1_0");
		} catch (JAXBException e) {
			logger.error("Error Initializing the JAXBContext to bind the schema classes", e);
			throw new PlatformException("Error Initializing the JAXBContext to bind the schema classes", e);
		}
	}
	
	@POST
	@Path("/rules")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getAllPromotionRules(String fetchRulesXML) {
		logger.info("getAllPromotionRulesXML : " + fetchRulesXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			FetchRuleRequest fetchRuleRequest = (FetchRuleRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(fetchRulesXML)));
			com.fb.platform.promotion.admin.to.FetchRuleRequest apiFetchRuleRequest = new com.fb.platform.promotion.admin.to.FetchRuleRequest();
			apiFetchRuleRequest.setSessionToken(fetchRuleRequest.getSessionToken());
			
			FetchRuleResponse fetchRuleResponse = new FetchRuleResponse();
			com.fb.platform.promotion.admin.to.FetchRuleResponse apiFetchRuleResponse = promotionAdminManager.fetchRules(apiFetchRuleRequest);
			
			fetchRuleResponse.setSessionToken(apiFetchRuleRequest.getSessionToken());
			fetchRuleResponse.setFetchRulesEnum(FetchRulesEnum.fromValue(apiFetchRuleResponse.getFetchRulesEnum().toString()));
			
			for(com.fb.platform.promotion.rule.RuleConfigDescriptor apiRuleConfigDescriptor: apiFetchRuleResponse.getRulesList()) {
				RuleConfigDescriptor ruleConfigDescriptor = new RuleConfigDescriptor();
				ruleConfigDescriptor.setRulesEnum(RulesEnum.valueOf(apiRuleConfigDescriptor.getRulesEnum().name()));
				for(com.fb.platform.promotion.rule.RuleConfigDescriptorItem apiRuleConfigDescriptorItem : apiRuleConfigDescriptor.getRuleConfigItemsList()) {
					RuleConfigDescriptorItem ruleConfigDescriptorItem = new RuleConfigDescriptorItem();
					ruleConfigDescriptorItem.setIsMandatory(apiRuleConfigDescriptorItem.isMandatory());
					ruleConfigDescriptorItem.setRuleConfigDescriptorEnum(RuleConfigDescriptorEnum.valueOf(apiRuleConfigDescriptorItem.getRuleConfigDescriptorEnum().name()));
					ruleConfigDescriptorItem.setRuleConfigDescription(apiRuleConfigDescriptorItem.getRuleConfigDescriptorEnum().getDescription());
					ruleConfigDescriptorItem.setRuleConfigType(apiRuleConfigDescriptorItem.getRuleConfigDescriptorEnum().getType());
					ruleConfigDescriptor.getRuleConfigDescriptorItem().add(ruleConfigDescriptorItem);
				}
				fetchRuleResponse.getRuleConfigDescriptor().add(ruleConfigDescriptor);
			}
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(fetchRuleResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("getAllPromotionRulesXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the getAllPromotionRules call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/createPromotion")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String createPromotion(String createPromotionXML) {
		logger.info("createPromotionXML : " + createPromotionXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			CreatePromotionRequest createPromotionRequest = (CreatePromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(createPromotionXML)));
			com.fb.platform.promotion.admin.to.CreatePromotionRequest apiCreatePromotionRequest = new com.fb.platform.promotion.admin.to.CreatePromotionRequest();
			
			apiCreatePromotionRequest.setSessionToken(createPromotionRequest.getSessionToken());
			
			PromotionTO promotionTO = createPromotionRequest.getPromotionTO();
			com.fb.platform.promotion.admin.to.PromotionTO apiPromotionTO = new com.fb.platform.promotion.admin.to.PromotionTO();
			apiPromotionTO.setActive(promotionTO.isIsActive());
			apiPromotionTO.setDescription(promotionTO.getDescription());
			if(promotionTO.getMaxAmount() != null) {
				apiPromotionTO.setMaxAmount(new Money(promotionTO.getMaxAmount()));
			} else {
				apiPromotionTO.setMaxAmount(null);
			}
			if(promotionTO.getMaxAmountPerUser() != null) {
				apiPromotionTO.setMaxAmountPerUser(new Money(promotionTO.getMaxAmountPerUser()));
			} else {
				apiPromotionTO.setMaxAmountPerUser(null);
			}
			apiPromotionTO.setMaxUses(promotionTO.getMaxUses());
			apiPromotionTO.setMaxUsesPerUser(promotionTO.getMaxUsesPerUser());
			apiPromotionTO.setPromotionName(promotionTO.getPromotionName());
			apiPromotionTO.setRuleName(createPromotionRequest.getPromotionTO().getRuleName());
			if(promotionTO.getValidFrom() == null) {
				apiPromotionTO.setValidFrom(null);
			} else {
				apiPromotionTO.setValidFrom(new DateTime(promotionTO.getValidFrom().toGregorianCalendar()));
			}
			if(promotionTO.getValidTill() == null) {
				apiPromotionTO.setValidTill(null);
			} else {
				apiPromotionTO.setValidTill(new DateTime(promotionTO.getValidTill().toGregorianCalendar()));
			}
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getRuleConfigItemTO()) {
				com.fb.platform.promotion.admin.to.RuleConfigItemTO apiRuleConfigItemTO = new com.fb.platform.promotion.admin.to.RuleConfigItemTO();
				apiRuleConfigItemTO.setRuleConfigName(ruleConfigItemTO.getRuleConfigName());
				apiRuleConfigItemTO.setRuleConfigValue(ruleConfigItemTO.getRuleConfigValue());
				apiPromotionTO.getConfigItems().add(apiRuleConfigItemTO);
			}
			
			apiCreatePromotionRequest.setPromotion(apiPromotionTO);
			
			CreatePromotionResponse createPromotionResponse	= new CreatePromotionResponse();	
			com.fb.platform.promotion.admin.to.CreatePromotionResponse apiCreatePromotionResponse = promotionAdminManager.createPromotion(apiCreatePromotionRequest);
			
			createPromotionResponse.setSessionToken(apiCreatePromotionResponse.getSessionToken());
			createPromotionResponse.setCreatePromotionEnum(CreatePromotionEnum.fromValue(apiCreatePromotionResponse.getCreatePromotionEnum().toString()));
			createPromotionResponse.setPromotionId(apiCreatePromotionResponse.getPromotionId());
			createPromotionResponse.setErrorCause(apiCreatePromotionResponse.getErrorCause());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(createPromotionResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("createPromotionXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the createPromotion call.", e);
			return "error"; //TODO return proper error response
		}
		
	}
	
	@POST
	@Path("/search")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String searchPromotion(String searchPromotionXML) {
		logger.info("searchPromotionXML : " + searchPromotionXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GregorianCalendar gregCal = new GregorianCalendar();
			
			SearchPromotionRequest searchPromotionRequest = (SearchPromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(searchPromotionXML)));
			com.fb.platform.promotion.admin.to.SearchPromotionRequest apiSearchPromotionRequest = new com.fb.platform.promotion.admin.to.SearchPromotionRequest();
			
			apiSearchPromotionRequest.setBatchSize(searchPromotionRequest.getBatchSize());
			apiSearchPromotionRequest.setPromotionName(searchPromotionRequest.getPromotionName());
			apiSearchPromotionRequest.setSessionToken(searchPromotionRequest.getSessionToken());
			apiSearchPromotionRequest.setStartRecord(searchPromotionRequest.getStartRecord());
			apiSearchPromotionRequest.setActive(searchPromotionRequest.isIsActive());
			apiSearchPromotionRequest.setSearchPromotionOrderBy(SearchPromotionOrderBy.valueOf(searchPromotionRequest.getSearchPromotionOrderBy().toString()));
			apiSearchPromotionRequest.setSearchPromotionOrderByOrder(SearchPromotionOrderByOrder.valueOf(searchPromotionRequest.getSearchPromotionOrderByOrder().toString()));
			if(searchPromotionRequest.getValidFrom() == null) {
				apiSearchPromotionRequest.setValidFrom(null);
			} else {
				apiSearchPromotionRequest.setValidFrom(new DateTime(searchPromotionRequest.getValidFrom().toGregorianCalendar()));
			}
			if(searchPromotionRequest.getValidTill() == null) {
				apiSearchPromotionRequest.setValidTill(null);
			} else {
				apiSearchPromotionRequest.setValidTill(new DateTime(searchPromotionRequest.getValidTill().toGregorianCalendar()));
			}
			
			SearchPromotionResponse searchPromotionResponse	= new SearchPromotionResponse();
			com.fb.platform.promotion.admin.to.SearchPromotionResponse apiSearchPromotionResponse = promotionAdminManager.searchPromotion(apiSearchPromotionRequest);
			
			searchPromotionResponse.setErrorCause(apiSearchPromotionResponse.getErrorCause());
			searchPromotionResponse.setSessionToken(apiSearchPromotionResponse.getSessionToken());
			searchPromotionResponse.setSearchPromotionEnum(SearchPromotionEnum.valueOf(apiSearchPromotionResponse.getSearchPromotionEnum().toString()));
			searchPromotionResponse.setTotalCount(apiSearchPromotionResponse.getTotalCount());
			
			if(searchPromotionResponse.getSearchPromotionEnum().equals(SearchPromotionEnum.SUCCESS)) {
				for(com.fb.platform.promotion.admin.to.PromotionTO apiPromotionView : apiSearchPromotionResponse.getPromotionsList()) {
					PromotionViewTO promotionView = new PromotionViewTO();
					promotionView.setDescription(apiPromotionView.getDescription());
					promotionView.setIsActive(apiPromotionView.isActive());
					promotionView.setPromotionId(apiPromotionView.getPromotionId());
					promotionView.setPromotionName(apiPromotionView.getPromotionName());
					promotionView.setRuleName(apiPromotionView.getRuleName());
					
					gregCal.set(apiPromotionView.getValidFrom().getYear(), apiPromotionView.getValidFrom().getMonthOfYear()-1, apiPromotionView.getValidFrom().getDayOfMonth(),0,0,0);
					promotionView.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
					
					gregCal.set(apiPromotionView.getValidTill().getYear(), apiPromotionView.getValidTill().getMonthOfYear()-1, apiPromotionView.getValidTill().getDayOfMonth(),0,0,0);
					promotionView.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
					
					searchPromotionResponse.getPromotionViewTO().add(promotionView);
				}
			}
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(searchPromotionResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("searchPromotionXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the searchPromotion call.", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the searchPromotion call invalid date in database.", e);
			return "error"; //TODO return proper error response
		}
		
	}
	
	@POST
	@Path("/view")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String viewPromotion(String viewPromotionXML) {
		logger.info("viewPromotionXML : " + viewPromotionXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GregorianCalendar gregCal = new GregorianCalendar();
			
			ViewPromotionRequest viewPromotionRequest = (ViewPromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(viewPromotionXML)));
			com.fb.platform.promotion.admin.to.ViewPromotionRequest apiViewPromotionRequest = new com.fb.platform.promotion.admin.to.ViewPromotionRequest();
			
			apiViewPromotionRequest.setSessionToken(viewPromotionRequest.getSessionToken());
			apiViewPromotionRequest.setPromotionId(viewPromotionRequest.getPromotionId());
			
			ViewPromotionResponse viewPromotionResponse = new ViewPromotionResponse();
			com.fb.platform.promotion.admin.to.ViewPromotionResponse apiViewPromotionResponse = promotionAdminManager.viewPromotion(apiViewPromotionRequest);
			
			viewPromotionResponse.setSessionToken(apiViewPromotionResponse.getSessionToken());
			viewPromotionResponse.setErrorCause(apiViewPromotionResponse.getErrorCause());
			viewPromotionResponse.setViewPromotionEnum(ViewPromotionEnum.valueOf(apiViewPromotionResponse.getViewPromotionEnum().toString()));
			
			PromotionTO promotionCompleteView = new PromotionTO();
			com.fb.platform.promotion.admin.to.PromotionTO apiPromotionCompleteView = apiViewPromotionResponse.getPromotionCompleteView();
			
			if(apiPromotionCompleteView != null) {
				promotionCompleteView.setDescription(apiPromotionCompleteView.getDescription());
				promotionCompleteView.setIsActive(apiPromotionCompleteView.isActive());
				promotionCompleteView.setMaxUses(apiPromotionCompleteView.getMaxUses());
				promotionCompleteView.setMaxUsesPerUser(apiPromotionCompleteView.getMaxUsesPerUser());
				promotionCompleteView.setPromotionId(apiPromotionCompleteView.getPromotionId());
				promotionCompleteView.setPromotionName(apiPromotionCompleteView.getPromotionName());
				promotionCompleteView.setRuleId(apiPromotionCompleteView.getRuleId());
				promotionCompleteView.setRuleName(apiPromotionCompleteView.getRuleName());
				
				gregCal.set(apiPromotionCompleteView.getValidFrom().getYear(), apiPromotionCompleteView.getValidFrom().getMonthOfYear()-1, apiPromotionCompleteView.getValidFrom().getDayOfMonth(),0,0,0);
				promotionCompleteView.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
				
				gregCal.set(apiPromotionCompleteView.getValidTill().getYear(), apiPromotionCompleteView.getValidTill().getMonthOfYear()-1, apiPromotionCompleteView.getValidTill().getDayOfMonth(),0,0,0);
				promotionCompleteView.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
				
				promotionCompleteView.setMaxAmount(apiPromotionCompleteView.getMaxAmount().getAmount());
				promotionCompleteView.setMaxAmountPerUser(apiPromotionCompleteView.getMaxAmountPerUser().getAmount());
				
				for(com.fb.platform.promotion.admin.to.RuleConfigItemTO apiRuleConfigItemTO : apiPromotionCompleteView.getConfigItems()) {
					RuleConfigItemTO ruleConfigItemTO = new RuleConfigItemTO();
					
					ruleConfigItemTO.setRuleConfigName(apiRuleConfigItemTO.getRuleConfigName());
					
					ruleConfigItemTO.setRuleConfigDescription(apiRuleConfigItemTO.getRuleConfigDescription());
					
					ruleConfigItemTO.setRuleConfigValue(apiRuleConfigItemTO.getRuleConfigValue());
					
					promotionCompleteView.getRuleConfigItemTO().add(ruleConfigItemTO);
				}
				promotionCompleteView.setCouponCount(apiPromotionCompleteView.getCouponCount());
			} else {
				promotionCompleteView = null;
			}

			viewPromotionResponse.setPromotionTO(promotionCompleteView);
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(viewPromotionResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("searchPromotionXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the searchPromotion call.", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the searchPromotion call invalid date in database.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/update")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String updatePromotion(String updatePromotionXML) {
		logger.info("updatePromotionXML : " + updatePromotionXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			UpdatePromotionRequest updatePromotionRequest = (UpdatePromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(updatePromotionXML)));
			com.fb.platform.promotion.admin.to.UpdatePromotionRequest apiUpdatePromotionRequest = new com.fb.platform.promotion.admin.to.UpdatePromotionRequest();
			
			apiUpdatePromotionRequest.setSessionToken(updatePromotionRequest.getSessionToken());
			
			PromotionTO promotionTO = updatePromotionRequest.getPromotionTO();
			com.fb.platform.promotion.admin.to.PromotionTO apiPromotionTO = new com.fb.platform.promotion.admin.to.PromotionTO();
			
			apiPromotionTO.setPromotionId(promotionTO.getPromotionId());
			apiPromotionTO.setRuleName(promotionTO.getRuleName());
			apiPromotionTO.setActive(promotionTO.isIsActive());
			apiPromotionTO.setDescription(promotionTO.getDescription());
			if(promotionTO.getMaxAmount() != null) {
				apiPromotionTO.setMaxAmount(new Money(promotionTO.getMaxAmount()));
			} else {
				apiPromotionTO.setMaxAmount(null);
			}
			if(promotionTO.getMaxAmountPerUser() != null) {
				apiPromotionTO.setMaxAmountPerUser(new Money(promotionTO.getMaxAmountPerUser()));
			} else {
				apiPromotionTO.setMaxAmountPerUser(null);
			}
			apiPromotionTO.setMaxUses(promotionTO.getMaxUses());
			apiPromotionTO.setMaxUsesPerUser(promotionTO.getMaxUsesPerUser());
			apiPromotionTO.setPromotionName(promotionTO.getPromotionName());
			if(promotionTO.getValidFrom() == null) {
				apiPromotionTO.setValidFrom(null);
			} else {
				apiPromotionTO.setValidFrom(new DateTime(promotionTO.getValidFrom().toGregorianCalendar()));
			}
			if(promotionTO.getValidTill() == null) {
				apiPromotionTO.setValidTill(null);
			} else {
				apiPromotionTO.setValidTill(new DateTime(promotionTO.getValidTill().toGregorianCalendar()));
			}
			for(RuleConfigItemTO ruleConfigItemTO : promotionTO.getRuleConfigItemTO()) {
				com.fb.platform.promotion.admin.to.RuleConfigItemTO apiRuleConfigItemTO = new com.fb.platform.promotion.admin.to.RuleConfigItemTO();
				apiRuleConfigItemTO.setRuleConfigName(ruleConfigItemTO.getRuleConfigName());
				apiRuleConfigItemTO.setRuleConfigValue(ruleConfigItemTO.getRuleConfigValue());
				apiPromotionTO.getConfigItems().add(apiRuleConfigItemTO);
			}
			
			apiUpdatePromotionRequest.setPromotion(apiPromotionTO);
			
			UpdatePromotionResponse updatePromotionResponse	= new UpdatePromotionResponse();	
			com.fb.platform.promotion.admin.to.UpdatePromotionResponse apiUpdatePromotionResponse = promotionAdminManager.updatePromotion(apiUpdatePromotionRequest);
			
			updatePromotionResponse.setSessionToken(apiUpdatePromotionResponse.getSessionToken());
			updatePromotionResponse.setUpdatePromotionEnum(UpdatePromotionEnum.fromValue(apiUpdatePromotionResponse.getUpdatePromotionEnum().toString()));
			updatePromotionResponse.setErrorCause(apiUpdatePromotionResponse.getErrorCause());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(updatePromotionResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("updatePromotionXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the updatePromotion call.", e);
			return "error"; //TODO return proper error response
		}
		
	}
	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Promotion Admin Websevice.\n");
		sb.append("XSD : http://hostname:port/promotionAdminWS/promotionAdmin/xsd\n");
		sb.append("To Assign a PRE_ISSUE coupon to user post to : http://hostname:port/promotionAdminWS/promotionAdmin/assgin/user\n");
		sb.append("To get all promotion rules post to : http://localhost:8080/promotionAdminWS/promotionAdmin/rules\n");
		sb.append("To create promotion post to : http://localhost:8080/promotionAdminWS/promotionAdmin/createPromotion\n");
		sb.append("To search promotion post to : http://localhost:8080/promotionAdminWS/promotionAdmin/search\n");
		sb.append("To view complete promotion post to : http://localhost:8080/promotionAdminWS/promotionAdmin/view\n");
		sb.append("To update promotion post to : http://localhost:8080/promotionAdminWS/promotionAdmin/update\n");
		return sb.toString();
	}
	
	@GET
	@Path("/xsd")
	@Produces("application/xml")
	public String getXsd() {	
		InputStream userXsd = this.getClass().getClassLoader().getResourceAsStream("promotionAdmin.xsd");
		String userXsdString = convertInputStreamToString(userXsd);
		return userXsdString;
	}
	
	private String convertInputStreamToString(InputStream inputStream) {
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		try {
			String line = bufReader.readLine();
			while( line != null ) {
				sb.append( line + "\n" );
				line = bufReader.readLine();
			}
			inputStream.close();
		} catch(IOException exception) {
			logger.error("promotion.xsd loading error : " + exception.getMessage() );
		}
		return sb.toString();
	}

	@POST
	@Path("/assign/user")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String assignCouponToUser(String assignCouponToUserXml) {
		logger.info("assignCouponToUserXml : " + assignCouponToUserXml);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();

			AssignCouponToUserRequest xmlRequest = (AssignCouponToUserRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(assignCouponToUserXml)));

			com.fb.platform.promotion.admin.to.AssignCouponToUserRequest apiRequest = new com.fb.platform.promotion.admin.to.AssignCouponToUserRequest();
			apiRequest.setCouponCode(xmlRequest.getCouponCode());
			apiRequest.setOverrideCouponUserLimit(xmlRequest.getOverrideCouponUserLimit());
			apiRequest.setSessionToken(xmlRequest.getSessionToken());
			apiRequest.setUserId(xmlRequest.getUserId());

			AssignCouponToUserResponse xmlResponse = new AssignCouponToUserResponse();
			com.fb.platform.promotion.admin.to.AssignCouponToUserResponse apiResponse = promotionAdminManager.assignCouponToUser(apiRequest);

			xmlResponse.setSessionToken(apiResponse.getSessionToken());
			xmlResponse.setAssignCouponToUserStatusEnum(AssignCouponToUserStatusEnum.fromValue(apiResponse.getStatus().toString()));

			StringWriter outStringWriter = new StringWriter();
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(xmlResponse, outStringWriter);

			String xmlResponseStr = outStringWriter.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("assignCouponToUser response :\n" + xmlResponseStr);
			}
			return xmlResponseStr;

		} catch (JAXBException e) {
			logger.error("Error in the searchPromotion call.", e);
			return "error"; //TODO return proper error response
		}
	}
}
