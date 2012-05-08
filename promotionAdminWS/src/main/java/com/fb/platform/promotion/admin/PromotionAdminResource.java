package com.fb.platform.promotion.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.admin._1_0.FetchRuleRequest;
import com.fb.platform.promotion.admin._1_0.FetchRuleResponse;
import com.fb.platform.promotion.admin._1_0.FetchRulesEnum;
import com.fb.platform.promotion.admin._1_0.RuleConfigDescriptor;
import com.fb.platform.promotion.admin._1_0.RuleConfigDescriptorEnum;
import com.fb.platform.promotion.admin._1_0.RuleConfigDescriptorItem;
import com.fb.platform.promotion.admin._1_0.RulesEnum;
import com.fb.platform.promotion.admin.service.PromotionAdminManager;

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
	@Path("/getAllPromotionRules")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String getAllPromotionRules(String fetchRulesXML) {
		logger.info("getAllPromotionRulesXML : " + fetchRulesXML);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			FetchRuleRequest fetchRuleRequest = (FetchRuleRequest) unmarshaller.unmarshal(new StreamSource(new StringReader(fetchRulesXML)));
			com.fb.platform.promotion.to.FetchRuleRequest apiFetchRuleRequest = new com.fb.platform.promotion.to.FetchRuleRequest();
			apiFetchRuleRequest.setSessionToken(fetchRuleRequest.getSessionToken());
			
			FetchRuleResponse fetchRuleResponse = new FetchRuleResponse();
			com.fb.platform.promotion.to.FetchRuleResponse apiFetchRuleResponse = promotionAdminManager.fetchRules(apiFetchRuleRequest);
			
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
			Marshaller marsheller = context.createMarshaller();
			marsheller.marshal(fetchRuleResponse, outStringWriter);

			String xmlResponse = outStringWriter.toString();
			logger.info("getAllPromotionRulesXML response :\n" + xmlResponse);
			return xmlResponse;
			
		} catch (JAXBException e) {
			logger.error("Error in the getAllPromotionRules call.", e);
			return "error"; //TODO return proper error response
		}
	}
	
	@GET
	public String ping() {
		StringBuilder sb = new StringBuilder();
		sb.append("Future Platform Promotion Admin Websevice.\n");
		sb.append("XSD : http://hostname:port/promotionAdminWS/promotionAdmin/xsd\n");
		sb.append("To get all promotion rules post to : http://hostname:port/promotionAdminWS/promotionAdmin/getAllPromotionRules\n");
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
}
