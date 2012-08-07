/**
 * 
 */
package com.fb.platform.promotion.product.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.fb.commons.to.Money;
import com.fb.platform.promotion.exception.PromotionNotFoundException;
import com.fb.platform.promotion.product.dao.PromotionConfigDao;
import com.fb.platform.promotion.product.model.Condition;
import com.fb.platform.promotion.product.model.Conditions;
import com.fb.platform.promotion.product.model.ConfigModule;
import com.fb.platform.promotion.product.model.ModuleJoin;
import com.fb.platform.promotion.product.model.OfferType;
import com.fb.platform.promotion.product.model.PromotionConfig;
import com.fb.platform.promotion.product.model.Result;
import com.fb.platform.promotion.product.model.Results;
import com.fb.platform.promotion.product.model.condition.BrandCondition;
import com.fb.platform.promotion.product.model.condition.CategoryCondition;
import com.fb.platform.promotion.product.model.condition.OrderCondition;
import com.fb.platform.promotion.product.model.condition.ProductCondition;
import com.fb.platform.promotion.product.model.result.BrandResult;
import com.fb.platform.promotion.product.model.result.CategoryResult;
import com.fb.platform.promotion.product.model.result.ProductResult;
import com.fb.platform.promotion.product.model.result.ValueChangeResult;

/**
 * @author vinayak
 *
 */
public class PromotionConfigDaoJdbcImpl implements PromotionConfigDao {

	private JdbcTemplate jdbcTemplate;

	private Log log = LogFactory.getLog(PromotionConfigDaoJdbcImpl.class);
	
	/**
	 * SELECT pcm.id FROM promotion_config_module pcm WHERE pcm.promotion_id=100 ORDER BY pcm.id ASC
	 * fetch all config ids for the given promotion id.
	 */
	private static final String GET_PROMOTION_CONFIG_IDS = 
			"SELECT " +
			"	pcm.id " +
			"FROM " +
			"	promotion_config_module pcm " +
			"WHERE " +
			"	pcm.promotion_id=? " +
			"ORDER BY " +
			"	pcm.id ASC";
	
	/**
	 * SELECT cc.condition_type,cc.quantity,cc.include,cc.product_ids,cc.brand_ids,cc.category_ids,cc.min_order_value,cc.max_order_value,cc.join_type FROM condition_config cc WHERE cc.promotion_config_module_id=100 ORDER BY cc.id
	 * fetch the condition for the given promotion config id
	 */
	private static final String GET_CONDITION_FOR_CONFIG_ID =
			"SELECT " +
			"	cc.condition_type as condition_type, " +
			"	cc.quantity as quantity, " +
			"	cc.include as include, " +
			"	cc.product_ids as product_ids, " +
			"	cc.brand_ids as brand_ids, " +
			"	cc.category_ids as category_ids, " +
			"	cc.min_order_value as min_order_value, " +
			"	cc.max_order_value as max_order_value, " +
			"	cc.join_type as join_type " +
			"FROM " +
			"	condition_config cc " +
			"WHERE " +
			"	cc.promotion_config_module_id=? " +
			"ORDER BY " +
			"	cc.id ASC";

	/**
	 * SELECT rc.result_type,rc.quantity,rc.product_ids,rc.brand_ids,rc.category_ids,rc.join_type,rc.offer_type,rc.offer_value FROM results_config rc WHERE rc.promotion_config_module_id=100 ORDER BY rc.id ASC
	 * fetch the result for the given promotion config id
	 */
	private static final String GET_RESULT_FOR_CONFIG_ID =
			"SELECT " +
			"	rc.result_type as result_type, " +
			"	rc.quantity as quantity, " +
			"	rc.product_ids as product_ids, " +
			"	rc.brand_ids as brand_ids, " +
			"	rc.category_ids as category_ids, " +
			"	rc.join_type as join_type, " +
			"	rc.offer_type as offer_type, " +
			"	rc.offer_value as offer_value " +
			"FROM " +
			"	results_config rc " +
			"WHERE " +
			"	rc.promotion_config_module_id=? " +
			"ORDER BY " +
			"	rc.id ASC";	
	
	@Override
	public PromotionConfig load(int promotionId) {
		log.info("Get promotion config for promotion id : " + promotionId);
		PromotionConfig promotionConfig = new PromotionConfig();
		List<Integer> configModuleIds = getPromotionConfigIds(promotionId);
		ConfigModule configModule = null;
		for(Integer configId : configModuleIds) {
			configModule = new ConfigModule();
			log.info("Get conditions and results for promotion config id : " + configId);
			Conditions conditions = jdbcTemplate.query(GET_CONDITION_FOR_CONFIG_ID, new Object[] {configId}, new ConditionResultSetExtractor());
			if(conditions != null) {
				Results results = jdbcTemplate.query(GET_RESULT_FOR_CONFIG_ID, new Object[] {configId}, new PromotionResultSetExtractor());
				configModule.setConditions(conditions);
				configModule.setResults(results);
				promotionConfig.getModules().add(configModule);
			}
		}
		return promotionConfig;
	}
	
	/**
	 * fetch all config module ids for the given promotion id.
	 */
	private List<Integer> getPromotionConfigIds(int promotionId) {
		try {
			List<Integer> configModuleIds = jdbcTemplate.queryForList(GET_PROMOTION_CONFIG_IDS, Integer.class, new Object[] {promotionId});
			if(configModuleIds.size() == 0) {
				log.error("No promotion found by promotion id : " + promotionId);
				throw new PromotionNotFoundException("No promotion found by promotion id : " + promotionId);
			}
			return configModuleIds;
		} catch (DataAccessException e) {
			log.error("No promotion found by promotion id : " + promotionId);
			throw new PromotionNotFoundException("No promotion found by promotion id : " + promotionId);
		}
	}

	public class ConditionResultSetExtractor implements ResultSetExtractor<Conditions> {
		
		@Override  
        public Conditions extractData(ResultSet rs) throws SQLException {  
			Conditions promotionConditions = null;
			String conditionType = null;
			Condition condition = null;
			ModuleJoin moduleJoin = null;
			while(rs.next()) {
				if(promotionConditions == null) {
					promotionConditions = new Conditions();
				}
				conditionType = rs.getString("condition_type");
				if(conditionType.equalsIgnoreCase("product")) {
					ProductCondition productCondition = new ProductCondition();
					productCondition.setQuantity(rs.getInt("quantity"));
					productCondition.setProductIds(getIds(rs.getString("product_ids")));
					moduleJoin = getJoin(rs.getString("join_type"));
					condition = productCondition;
				} else if (conditionType.equalsIgnoreCase("category")) {
					CategoryCondition categoryCondition = new CategoryCondition();
					categoryCondition.setQuantity(rs.getInt("quantity"));
					categoryCondition.setCategoryIds(getIds(rs.getString("category_ids")));
					moduleJoin = getJoin(rs.getString("join_type"));
					condition = categoryCondition;
				} else if (conditionType.equalsIgnoreCase("brand")) {
					BrandCondition brandCondition = new BrandCondition();
					brandCondition.setQuantity(rs.getInt("quantity"));
					brandCondition.setBrandIds(getIds(rs.getString("brand_ids")));
					moduleJoin = getJoin(rs.getString("join_type"));
					condition = brandCondition;
				} else if (conditionType.equalsIgnoreCase("order")) {
					OrderCondition orderCondition = new OrderCondition();
					orderCondition.setMinimumOrderValue(new Money(rs.getBigDecimal("min_order_value")));
					orderCondition.setMaximumOrderValue(new Money(rs.getBigDecimal("max_order_value")));
					moduleJoin = getJoin(rs.getString("join_type"));
					condition = orderCondition;
				}
				promotionConditions.getConditions().add(condition);
				if(moduleJoin  != null) {
					promotionConditions.getJoins().add(moduleJoin);
				}
			}
			return promotionConditions;
		}
		
		private List<Integer> getIds(String idString) {
			String[] ids = StringUtils.split(idString, ",");
			List<Integer> intIds = new ArrayList<Integer>();
			for(String id : ids) {
				intIds.add(new Integer(StringUtils.trim(id)));
			}
			return intIds;
		}
		
		private ModuleJoin getJoin(String join) {
			ModuleJoin moduleJoin = null;
			if(join != null && join.equalsIgnoreCase(ModuleJoin.AND.toString())) {
				moduleJoin = ModuleJoin.AND;
			} else if(join != null && join.equalsIgnoreCase(ModuleJoin.OR.toString())) {
				moduleJoin = ModuleJoin.OR;
			}
			return moduleJoin;
		}
	}
	
	public class PromotionResultSetExtractor implements ResultSetExtractor<Results> {
		
		@Override  
        public Results extractData(ResultSet rs) throws SQLException {  
			Results promotionResults = null;
			String resultType = null;
			Result result = null;
			ModuleJoin moduleJoin = null;
			while(rs.next()) {
				if(promotionResults == null) {
					promotionResults = new Results();
				}
				resultType = rs.getString("result_type");
				if(resultType.equalsIgnoreCase("product")) {
					ProductResult productResult = new ProductResult();
					productResult.setQuantity(rs.getInt("quantity"));
					productResult.setProductIds(getIds(rs.getString("product_ids")));
					productResult.setOfferValue(new Money(rs.getBigDecimal("offer_value")));
					productResult.setOfferType(getOfferType(rs.getString("offer_type")));
					moduleJoin = getJoin(rs.getString("join_type"));
					result = productResult;
				} else if (resultType.equalsIgnoreCase("category")) {
					CategoryResult categoryResult = new CategoryResult();
					categoryResult.setQuantity(rs.getInt("quantity"));
					categoryResult.setCategoryIds(getIds(rs.getString("category_ids")));
					categoryResult.setOfferValue(new Money(rs.getBigDecimal("offer_value")));
					categoryResult.setOfferType(getOfferType(rs.getString("offer_type")));
					moduleJoin = getJoin(rs.getString("join_type"));
					result = categoryResult;
				} else if (resultType.equalsIgnoreCase("brand")) {
					BrandResult brandResult = new BrandResult();
					brandResult.setQuantity(rs.getInt("quantity"));
					brandResult.setBrandIds(getIds(rs.getString("brand_ids")));
					brandResult.setOfferValue(new Money(rs.getBigDecimal("offer_value")));
					brandResult.setOfferType(getOfferType(rs.getString("offer_type")));
					moduleJoin = getJoin(rs.getString("join_type"));
					result = brandResult;
				} else if (resultType.equalsIgnoreCase("value")) {
					ValueChangeResult valueChangeResult = new ValueChangeResult();
					valueChangeResult.setOfferValue(new Money(rs.getBigDecimal("offer_value")));
					valueChangeResult.setOfferType(getOfferType(rs.getString("offer_type")));
					moduleJoin = getJoin(rs.getString("join_type"));
					result = valueChangeResult;
				}
				promotionResults.getResults().add(result);
				
				if(moduleJoin  != null) {
					promotionResults.getJoins().add(moduleJoin);
				}
			}
			return promotionResults;
		}
		
		private List<Integer> getIds(String idString) {
			String[] ids = StringUtils.split(idString, ",");
			List<Integer> intIds = new ArrayList<Integer>();
			for(String id : ids) {
				intIds.add(new Integer(StringUtils.trim(id)));
			}
			return intIds;
		}
		
		private ModuleJoin getJoin(String join) {
			ModuleJoin moduleJoin = null;
			if(join != null && join.equalsIgnoreCase(ModuleJoin.AND.toString())) {
				moduleJoin = ModuleJoin.AND;
			} else if(join != null && join.equalsIgnoreCase(ModuleJoin.OR.toString())) {
				moduleJoin = ModuleJoin.OR;
			}
			return moduleJoin;
		}
		
		private OfferType getOfferType(String type) {
			OfferType offerType = null;
			if(type.equalsIgnoreCase(OfferType.FIXED_OFF.toString())) {
				offerType = OfferType.FIXED_OFF;
			} else if(type.equalsIgnoreCase(OfferType.FIXED_PRICE.toString())) {
				offerType = OfferType.FIXED_PRICE;
			} else if(type.equalsIgnoreCase(OfferType.PERCENT_OFF.toString())) {
				offerType = OfferType.PERCENT_OFF;
			}
			return offerType;
		}
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
