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

import org.apache.commons.lang.StringUtils;
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
import com.fb.platform.promotion.admin._1_0.CodeDetails;
import com.fb.platform.promotion.admin._1_0.CouponBasicDetails;
import com.fb.platform.promotion.admin._1_0.CouponTO;
import com.fb.platform.promotion.admin._1_0.CouponType;
import com.fb.platform.promotion.admin._1_0.CreateCouponRequest;
import com.fb.platform.promotion.admin._1_0.CreateCouponResponse;
import com.fb.platform.promotion.admin._1_0.CreateCouponStatus;
import com.fb.platform.promotion.admin._1_0.CreatePromotionEnum;
import com.fb.platform.promotion.admin._1_0.CreatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.CreatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.CreatePromotionTO;
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
import com.fb.platform.promotion.admin._1_0.SearchCouponRequest;
import com.fb.platform.promotion.admin._1_0.SearchCouponResponse;
import com.fb.platform.promotion.admin._1_0.SearchCouponStatus;
import com.fb.platform.promotion.admin._1_0.SearchPromotionEnum;
import com.fb.platform.promotion.admin._1_0.SearchPromotionRequest;
import com.fb.platform.promotion.admin._1_0.SearchPromotionResponse;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionEnum;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionRequest;
import com.fb.platform.promotion.admin._1_0.UpdatePromotionResponse;
import com.fb.platform.promotion.admin._1_0.ViewCouponRequest;
import com.fb.platform.promotion.admin._1_0.ViewCouponResponse;
import com.fb.platform.promotion.admin._1_0.ViewCouponStatus;
import com.fb.platform.promotion.admin._1_0.ViewPromotionEnum;
import com.fb.platform.promotion.admin._1_0.ViewPromotionRequest;
import com.fb.platform.promotion.admin._1_0.ViewPromotionResponse;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;
import com.fb.platform.promotion.admin.to.SearchCouponOrderBy;
import com.fb.platform.promotion.admin.to.SearchPromotionOrderBy;
import com.fb.platform.promotion.admin.to.SortOrder;

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
	@Path("/promotion/create")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String createPromotion(String createPromotionXML) {
		logger.info("createPromotionXML : " + createPromotionXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			CreatePromotionRequest createPromotionRequest = (CreatePromotionRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(createPromotionXML)));
			com.fb.platform.promotion.admin.to.CreatePromotionRequest apiCreatePromotionRequest = new com.fb.platform.promotion.admin.to.CreatePromotionRequest();
			
			apiCreatePromotionRequest.setSessionToken(createPromotionRequest.getSessionToken());
			
			CreatePromotionTO promotionTO = createPromotionRequest.getCreatePromotionTO();
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
			apiPromotionTO.setRuleName(createPromotionRequest.getCreatePromotionTO().getRuleName());
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
	@Path("/promotion/search")
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
			apiSearchPromotionRequest.setSortOrder(SortOrder.valueOf(searchPromotionRequest.getSortOrder().toString()));
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
					promotionView.setPromotionId(apiPromotionView.getId());
					promotionView.setPromotionName(apiPromotionView.getPromotionName());
					promotionView.setRuleName(apiPromotionView.getRuleName());
					
					if(apiPromotionView.getValidFrom() != null) {
						gregCal.set(apiPromotionView.getValidFrom().getYear(), apiPromotionView.getValidFrom().getMonthOfYear()-1, apiPromotionView.getValidFrom().getDayOfMonth(),0,0,0);
						promotionView.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
					} else {
						promotionView.setValidFrom(null);
					}
					
					if(apiPromotionView.getValidTill() != null) {
						gregCal.set(apiPromotionView.getValidTill().getYear(), apiPromotionView.getValidTill().getMonthOfYear()-1, apiPromotionView.getValidTill().getDayOfMonth(),0,0,0);
						promotionView.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
					} else {
						promotionView.setValidTill(null);
					}
					
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
	@Path("/promotion/view")
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
				promotionCompleteView.setPromotionId(apiPromotionCompleteView.getId());
				promotionCompleteView.setPromotionName(apiPromotionCompleteView.getPromotionName());
				promotionCompleteView.setRuleId(apiPromotionCompleteView.getRuleId());
				promotionCompleteView.setRuleName(apiPromotionCompleteView.getRuleName());
				
				if(apiPromotionCompleteView.getValidFrom() != null) {
					gregCal.set(apiPromotionCompleteView.getValidFrom().getYear(), apiPromotionCompleteView.getValidFrom().getMonthOfYear()-1, apiPromotionCompleteView.getValidFrom().getDayOfMonth(),0,0,0);
					promotionCompleteView.setValidFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
				} else {
					promotionCompleteView.setValidFrom(null);
				}
				
				if(apiPromotionCompleteView.getValidTill() != null) {
					gregCal.set(apiPromotionCompleteView.getValidTill().getYear(), apiPromotionCompleteView.getValidTill().getMonthOfYear()-1, apiPromotionCompleteView.getValidTill().getDayOfMonth(),0,0,0);
					promotionCompleteView.setValidTill(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
				} else {
					promotionCompleteView.setValidTill(null);
				}
				
				promotionCompleteView.setMaxAmount(apiPromotionCompleteView.getMaxAmount().getAmount());
				promotionCompleteView.setMaxAmountPerUser(apiPromotionCompleteView.getMaxAmountPerUser().getAmount());
				
				for(com.fb.platform.promotion.admin.to.RuleConfigItemTO apiRuleConfigItemTO : apiPromotionCompleteView.getConfigItems()) {
					RuleConfigItemTO ruleConfigItemTO = new RuleConfigItemTO();
					
					ruleConfigItemTO.setRuleConfigName(apiRuleConfigItemTO.getRuleConfigName());
					
					ruleConfigItemTO.setRuleConfigDescription(com.fb.platform.promotion.rule.RuleConfigDescriptorEnum.valueOf(apiRuleConfigItemTO.getRuleConfigName()).getDescription());
					
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
			logger.info("viewPromotionXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the viewPromotion call.", e);
			return "error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the viewPromotion call invalid date in database.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/promotion/update")
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
			
			apiPromotionTO.setId(promotionTO.getPromotionId());
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
		sb.append("To Assign a PRE_ISSUE coupon to user post to : http://hostname:port/promotionAdminWS/promotionAdmin/user/assign\n");
		sb.append("To get all promotion rules post to : http://hostname:port/promotionAdminWS/promotionAdmin/rules\n");
		sb.append("To create promotion post to : http://hostname:port/promotionAdminWS/promotionAdmin/promotion/create\n");
		sb.append("To search promotion post to : http://hostname:port/promotionAdminWS/promotionAdmin/promotion/search\n");
		sb.append("To view complete promotion post to : http://hostname:port/promotionAdminWS/promotionAdmin/promotion/view\n");
		sb.append("To update promotion post to : http://hostname:port/promotionAdminWS/promotionAdmin/promotion/update\n");
		sb.append("To view coupon post to : http://hostname:port/promotionAdminWS/promotionAdmin/coupon/view\n");
		sb.append("To search coupon post to : http://hostname:port/promotionAdminWS/promotionAdmin/coupon/search\n");
		sb.append("To create coupon post to : http://hostname:port/promotionAdminWS/promotionAdmin/coupon/create\n");
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
	@Path("/user/assign")
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
			
			logger.info("assignCouponToUser response :\n" + xmlResponseStr);
			
			return xmlResponseStr;

		} catch (JAXBException e) {
			logger.error("Error in the searchPromotion call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/coupon/view")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String viewCoupon(String viewCouponXML) {
		logger.info("viewCouponXML : " + viewCouponXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GregorianCalendar gregCal = new GregorianCalendar();
			
			ViewCouponRequest viewCouponRequest = (ViewCouponRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(viewCouponXML)));
			com.fb.platform.promotion.admin.to.ViewCouponRequest apiViewCouponRequest = new com.fb.platform.promotion.admin.to.ViewCouponRequest();
			
			apiViewCouponRequest.setSessionToken(viewCouponRequest.getSessionToken());
			apiViewCouponRequest.setCouponCode(viewCouponRequest.getCouponCode());
			apiViewCouponRequest.setCouponId(viewCouponRequest.getCouponId());
			
			ViewCouponResponse viewCouponResponse = new ViewCouponResponse();
			com.fb.platform.promotion.admin.to.ViewCouponResponse apiViewCouponResponse = promotionAdminManager.viewCoupon(apiViewCouponRequest);
			
			viewCouponResponse.setSessionToken(apiViewCouponResponse.getSessionToken());
			viewCouponResponse.setErrorCause(apiViewCouponResponse.getErrorCause());
			viewCouponResponse.setViewCouponStatus(ViewCouponStatus.valueOf(apiViewCouponResponse.getStatus().toString()));
			
			CouponTO couponCompleteView = new CouponTO();
			com.fb.platform.promotion.admin.to.CouponTO apiCouponCompleteView = apiViewCouponResponse.getCouponTO();
			
			if(apiCouponCompleteView != null) {
				
				couponCompleteView.setCouponCode(apiCouponCompleteView.getCouponCode());
				couponCompleteView.setCouponId(apiCouponCompleteView.getCouponId());
				couponCompleteView.setPromotionId(apiCouponCompleteView.getPromotionId());
				couponCompleteView.setCouponType(CouponType.valueOf(apiCouponCompleteView.getCouponType().toString()));
				couponCompleteView.setMaxUsesPerUser(apiCouponCompleteView.getMaxUsesPerUser());
				couponCompleteView.setMaxUses(apiCouponCompleteView.getMaxUses());
				couponCompleteView.setMaxAmount(apiCouponCompleteView.getMaxAmount().getAmount());
				couponCompleteView.setMaxAmountPerUser(apiCouponCompleteView.getMaxAmountPerUser().getAmount());
				
				if(null != apiCouponCompleteView.getCreatedOn()){
					gregCal.set(apiCouponCompleteView.getCreatedOn().getYear(), apiCouponCompleteView.getCreatedOn().getMonthOfYear()-1, 
							apiCouponCompleteView.getCreatedOn().getDayOfMonth(),0,0,0);
					couponCompleteView.setCreatedOn(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
				}
				
				if(null != apiCouponCompleteView.getLastModifiedOn()){
					gregCal.set(apiCouponCompleteView.getLastModifiedOn().getYear(), apiCouponCompleteView.getLastModifiedOn().getMonthOfYear()-1, 
							apiCouponCompleteView.getLastModifiedOn().getDayOfMonth(),0,0,0);
					couponCompleteView.setLastModifiedOn(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
				}
				
			} else {
				couponCompleteView = null;
			}

			viewCouponResponse.setCouponTO(couponCompleteView);
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(viewCouponResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("viewCouponXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the viewCoupon call.", e);
			return "viewCoupon error"; //TODO return proper error response
		} catch (DatatypeConfigurationException e) {
			logger.error("Error in the viewCoupon call invalid date in database.", e);
			return "viewCoupon error"; //TODO return proper error response
		}
	}
	
	@POST
	@Path("/coupon/search")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String searchCoupon(String searchCouponXML) {
		logger.info("searchCouponXML : " + searchCouponXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			GregorianCalendar gregCal = new GregorianCalendar();
			
			SearchCouponRequest searchCouponRequest = (SearchCouponRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(searchCouponXML)));
			com.fb.platform.promotion.admin.to.SearchCouponRequest apiSearchCouponRequest = new com.fb.platform.promotion.admin.to.SearchCouponRequest();
			
			apiSearchCouponRequest.setBatchSize(searchCouponRequest.getBatchSize());
			apiSearchCouponRequest.setSessionToken(searchCouponRequest.getSessionToken());
			apiSearchCouponRequest.setStartRecord(searchCouponRequest.getStartRecord());
			
			String inputCouponCode = StringUtils.isBlank(searchCouponRequest.getCouponCode()) ? null : searchCouponRequest.getCouponCode().trim();
			apiSearchCouponRequest.setCouponCode(inputCouponCode);
			
			String inputUserName = StringUtils.isBlank(searchCouponRequest.getUserName()) ? null : searchCouponRequest.getUserName().trim();
			apiSearchCouponRequest.setUserName(inputUserName);
			
			com.fb.platform.promotion.admin._1_0.SearchCouponOrderBy searchOrderByInRequest = searchCouponRequest.getSearchCouponOrderBy();
			if(null==searchOrderByInRequest){
				searchOrderByInRequest = com.fb.platform.promotion.admin._1_0.SearchCouponOrderBy.COUPON_CODE;
			}
			apiSearchCouponRequest.setOrderBy(SearchCouponOrderBy.valueOf(searchOrderByInRequest.toString()));
			
			com.fb.platform.promotion.admin._1_0.SortOrder sortOrderInRequest = searchCouponRequest.getSortOrder();
			if(null==sortOrderInRequest){
				sortOrderInRequest = com.fb.platform.promotion.admin._1_0.SortOrder.ASCENDING;
			}
			apiSearchCouponRequest.setSortOrder(SortOrder.valueOf(sortOrderInRequest.toString()));
			
			SearchCouponResponse searchCouponResponse	= new SearchCouponResponse();
			com.fb.platform.promotion.admin.to.SearchCouponResponse apiSearchPromotionResponse = promotionAdminManager.searchCoupons(apiSearchCouponRequest);
			
			searchCouponResponse.setErrorCause(apiSearchPromotionResponse.getErrorCause());
			searchCouponResponse.setSessionToken(apiSearchPromotionResponse.getSessionToken());
			searchCouponResponse.setSearchCouponStatus(SearchCouponStatus.valueOf(apiSearchPromotionResponse.getStatus().toString()));
			//searchCouponResponse.setTotalCount(apiSearchPromotionResponse.getTotalCount());
			
			if(searchCouponResponse.getSearchCouponStatus().equals(SearchCouponStatus.SUCCESS)) {
				for(com.fb.platform.promotion.admin.to.CouponBasicDetails apiCouponBasicDetails : apiSearchPromotionResponse.getCouponBasicDetailsList()) {
					CouponBasicDetails couponBasicDetails = new CouponBasicDetails();
					couponBasicDetails.setCouponCode(apiCouponBasicDetails.getCouponCode());
					couponBasicDetails.setCouponId(apiCouponBasicDetails.getCouponId());
					couponBasicDetails.setCouponType(CouponType.valueOf(apiCouponBasicDetails.getCouponType().toString()));
					
					searchCouponResponse.getCouponBasicDetails().add(couponBasicDetails);
				}
			}
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(searchCouponResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("searchCouponXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the searchCoupon call.", e);
			return "searchCoupon error"; //TODO return proper error response
		} 
	}
	
	@POST
	@Path("/coupon/create")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String createCoupon(String createCouponXML) {
		logger.info("createCouponXML : " + createCouponXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			CreateCouponRequest createCouponRequest = (CreateCouponRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(createCouponXML)));
			com.fb.platform.promotion.admin.to.CreateCouponRequest apiCreateCouponRequest = new com.fb.platform.promotion.admin.to.CreateCouponRequest();
			
			apiCreateCouponRequest.setSessionToken(createCouponRequest.getSessionToken());
			apiCreateCouponRequest.setCount(createCouponRequest.getNumberOfCoupon());
			
			//get the couponTO data
			CouponTO couponTO = createCouponRequest.getCouponTO();
			if(couponTO!=null){
				apiCreateCouponRequest.setMaxUses(couponTO.getMaxUses());
				apiCreateCouponRequest.setMaxUsesPerUser(couponTO.getMaxUsesPerUser());
				apiCreateCouponRequest.setType(com.fb.platform.promotion.model.coupon.CouponType.valueOf(couponTO.getCouponType().toString()));
				apiCreateCouponRequest.setPromotionId(couponTO.getPromotionId());
				
				if(couponTO.getMaxAmount() != null) {
					apiCreateCouponRequest.setMaxAmount(couponTO.getMaxAmount());
				} else {
					apiCreateCouponRequest.setMaxAmount(null);
				}
				if(couponTO.getMaxAmountPerUser() != null) {
					apiCreateCouponRequest.setMaxAmountPerUser(couponTO.getMaxAmountPerUser());
				} else {
					apiCreateCouponRequest.setMaxAmountPerUser(null);
				}
			}
			
			//set the coupon code details
			CodeDetails codeDetails = createCouponRequest.getCodeDetails();
			
			if(codeDetails!=null){
				String startsWith = codeDetails.getStartsWith()==null ? StringUtils.EMPTY : codeDetails.getStartsWith().trim();
				String endsWith = codeDetails.getEndsWith()==null ? StringUtils.EMPTY : codeDetails.getEndsWith().trim();
				
				apiCreateCouponRequest.setStartsWith(startsWith);
				apiCreateCouponRequest.setEndsWith(endsWith);
				apiCreateCouponRequest.setLength(codeDetails.getCodeLength());
				
				// need to set the alphabetCase and coupon code alphabetCode type (currently not taken from user
			}
			
			CreateCouponResponse createCouponResponse	= new CreateCouponResponse();	
			com.fb.platform.promotion.admin.to.CreateCouponResponse apiCreateCouponResponse = promotionAdminManager.createCoupons(apiCreateCouponRequest);
			
			createCouponResponse.setSessionToken(apiCreateCouponResponse.getSessionToken());
			createCouponResponse.setCreateCouponStatus(CreateCouponStatus.fromValue(apiCreateCouponResponse.getStatus().toString()));
			createCouponResponse.setNumberOfCouponsCreated(apiCreateCouponResponse.getNumberOfCouponsCreated());
			createCouponResponse.setCommaSeparatedCouponCodes(apiCreateCouponResponse.getCommaSeparatedCouponCodes());
			
			StringWriter outStringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(createCouponResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("createCouponXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the createCoupon call.", e);
			return "createCoupon error"; //TODO return proper error response
		}
		
	}

}
