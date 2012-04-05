/**
 * 
 */
package com.fb.platform.ifs.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fb.platform.ifs.dao.IFSDao;
import com.fb.platform.ifs.domain.DCBo;
import com.fb.platform.ifs.domain.LSPBo;
import com.fb.platform.ifs.domain.LspDcBo;
import com.fb.platform.ifs.domain.ProductBo;
import com.fb.platform.ifs.domain.SingleArticleServiceabilityRequestBo;
import com.fb.platform.ifs.mapper.DCMapper;
import com.fb.platform.ifs.mapper.LSPMapper;
import com.fb.platform.ifs.mapper.LspDcMapper;
import com.fb.platform.ifs.mapper.ProductMapper;

/**
 * @author sarvesh
 *
 */
public class IFSDaoJdbcImpl implements IFSDao {

	private static Logger log = Logger.getLogger(IFSDaoJdbcImpl.class);
	
	private JdbcTemplate jdbcTemplate;

	private static final String GET_PRODUCTGROUP_QUERY = "SELECT a.name, a.local_tag, a.ship_mode, case when ((a.high_value_flag = 1) and (COALESCE(a.threshold_amount, 0) > ?)) then 1 else 0 end high_value_flag, COALESCE(a.threshold_amount, 0) threshold_amount, b.article_id" +
														 " FROM fulfillment_productgroup a, fulfillment_articleproductgroup b" + 
														 " where a.id = b.product_group_id and b.article_id = ? and a.client_id = ?";
	
	/*
	 * The query returns the DCs having stock > 0 (available stock = stock - bookings - stock_adjustment - threshold - outward) from rate_chart_id and client_id 
	 */
	private static final String GET_DC_BASIC = "select distinct a.id inv_id, b.id, b.code, a.type, b.cod_flag, e.dc_id local_dc, case when (COALESCE(a.stock, 0) - COALESCE(a.bookings, 0) -  " +
							"COALESCE(a.stock_adjustment, 0) - COALESCE(a.threshold, 0) - COALESCE(a.outward, 0))> 0 then (COALESCE(a.stock, 0) -  " +
							"COALESCE(a.bookings, 0) - COALESCE(a.stock_adjustment, 0) - COALESCE(a.threshold, 0) - COALESCE(a.outward, 0)) else 0 end stock_level,  " +
							"a.expected_in, a.expected_on, (case when (a.type = 'madetoorder') then (case when ((a.expected_in is null or a.expected_in = ''))  " +
							"then 0 else a.expected_in end) when (a.type = 'preorder') then (case when ((a.expected_on is null or a.expected_on = ''))  " +
							"then 0 else COALESCE(DATEDIFF(a.expected_on, now()), 0) end) else 0 end) delta " +
							"from inventory_inventory a join fulfillment_dc b on (a.dc_id = b.id) left join fulfillment_dczipgroup e on (b.id = e.dc_id)  " +
							"where a.is_active = 1 and a.rate_chart_id = ? and b.client_id = ? and a.starts_on <= now() and now() <= a.ends_on and  " +
							"((COALESCE(a.stock, 0) - COALESCE(a.bookings, 0) - COALESCE(a.stock_adjustment, 0) - COALESCE(a.threshold, 0) - COALESCE(a.outward, 0)) >= ?)";
	 
	 /*
	 * removed check for expected_on for virtual inventory. clause:(case when ((a.type = 'virtual')) then a.expected_on >= now() else 1 end) and 
	 */
	/*
	 * The query returns the LSPs from pincode and product group 
	 */
	private static final String GET_LSP_BASIC = "select c.lsp_id, b.zipgroup_id, b.lsp_priority, a.code lsp_code, c.zipgroup_code, b.cod_flag, b.high_value from fulfillment_lsp a, fulfillment_pincodezipgroupmap b, fulfillment_lspzipgroup c" +
					" where a.id = c.lsp_id and b.zipgroup_id = c.id and b.pincode = ? and b.supported_product_groups like ? order by b.lsp_priority";

	/*
	 * The query returns the LSP-DC from resolved DCs, zipGroups and shipping mode 
	 */
	private static final String GET_LSPDC_BASIC = "select distinct c.dc_id, d.code dc_code, c.zipgroup_id, b.lsp_id, c.transit_time, e.type" + 
												" from fulfillment_lspzipgroup b, fulfillment_lspdeliverychart c, fulfillment_dc d, inventory_inventory e" +
												" where b.id = c.zipgroup_id and c.dc_id = d.id and c.dc_id = e.dc_id and c.ship_mode = ?";

	/*
	 * The query returns the LSP-DC from resolved DCs, zipGroups and shipping mode for locally shippable product 
	 */
	private static final String GET_LSPDC_LOCAL = "select c.dc_id, d.code dc_code, c.zipgroup_id, c.lsp_id, e.type from fulfillment_dczipgroup c, fulfillment_dc d, inventory_inventory e " +
												"where c.dc_id = d.id and c.dc_id = e.dc_id ";
	
	private static final String GET_LSPDC_THIRD_PARTY = "select b.id dc_id, b.code dc_code, c.type, a.shipping_time, a.delivery_time, 1 third_party from fulfillment_deliverychart_vendor a, fulfillment_dc b, inventory_inventory c " +
	" where b.id = c.dc_id and (a.pincode = ? or a.pincode = ?) and a.product_group like ?";
	
	
	/*
	 * This method fetch the product group from articleId, client and returns the product object
	 */
	public ProductBo getProductGroup(String articleId, String client, double itemPrice) {
		List<ProductBo> productBoList = (List<ProductBo>)jdbcTemplate.query(GET_PRODUCTGROUP_QUERY, new Object[]{itemPrice, articleId, client}, new ProductMapper());
		log.info("productBoList :: "+productBoList);
		if(productBoList == null || productBoList.size() == 0)
		{
			return null;
		}
		return productBoList.get(0);
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * This method fetch the DCs having stock > 0 from rateChartId, client and returns list of dc objects for given article
	 */
	public List<DCBo> getDcList(ProductBo productBo, SingleArticleServiceabilityRequestBo serviceabilityRequestBo) {
		List<DCBo> dcBoList = null;
		String rateChartId = serviceabilityRequestBo.getRateChartId();
		String query = GET_DC_BASIC;
		Object[] params = new Object[3];
		params[0] = rateChartId;
		params[1] = serviceabilityRequestBo.getClient();
		params[2] = serviceabilityRequestBo.getQty();
		
		log.info("dc query :: " + query + " <<>> rateChartId :: " + rateChartId + " <> client :: " +serviceabilityRequestBo.getClient());
		dcBoList = jdbcTemplate.query(GET_DC_BASIC, params, new DCMapper());
		log.info("dcBoList :: " + dcBoList);
		return dcBoList;
	}

	/**
	 * This method returns comma seperated string of all the DCs
	 * @param dcBoList
	 * @return
	 */
	private String getDCArray(List<DCBo> dcBoList) {
		String dcArray = ""; 
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			dcArray += dcBo.getId()+",";
		}
		if(!StringUtils.isEmpty(dcArray))
		{
			dcArray = dcArray.substring(0,dcArray.lastIndexOf(","));
		}
		log.info("dcList :: "+dcArray);
		return dcArray;
	}
	
	/*
	 * This method fetch the LSPs from pincode, productGroup and returns list of lsp objects  
	 */
	public List<LSPBo> getLSPList(ProductBo productBo, SingleArticleServiceabilityRequestBo serviceabilityRequestBo) {
		log.info("IFSDaoJdbcImpl.getLSPList()\n\n");
		List<LSPBo> lspBoList = null;
		Object[] params = new Object[2];
		params[0] = serviceabilityRequestBo.getPincode();
		params[1] = "%"+productBo.getProductGroup()+",%";
		log.info("lsp query :: "+GET_LSP_BASIC + " <<>> pincode :: " + serviceabilityRequestBo.getPincode() + " <> product group :: " + productBo.getProductGroup());
		lspBoList = jdbcTemplate.query(GET_LSP_BASIC, params, new LSPMapper());
		log.info("lspBoList :: "+lspBoList);
		return lspBoList;
	}
	
	@Override
	public List<LspDcBo> getLspDcBoForThirdPartyProduct(List<DCBo> dcBoList, ProductBo productBo,
			SingleArticleServiceabilityRequestBo serviceabilityRequestBo) {
		List<LspDcBo> lspDcBoList = null;
		String dcList = getDCArray(dcBoList);
		String invIdList = getInvIdArray(dcBoList);
		Object[] params = new Object[3];
		params[0] = serviceabilityRequestBo.getPincode();
		params[1] = "all";
		params[2] = "%"+productBo.getProductGroup()+",%";
		String query = GET_LSPDC_THIRD_PARTY + "and b.id in ("+ dcList +") and c.id in ("+ invIdList +")";
		log.info("lsp query :: "+ query + " <<>> pincode :: " + serviceabilityRequestBo.getPincode() + " <> product group :: " + productBo.getProductGroup());
		lspDcBoList = jdbcTemplate.query(GET_LSPDC_THIRD_PARTY, params, new LspDcMapper());
		log.info("lspBoList :: "+lspDcBoList);
		if(lspDcBoList  == null || lspDcBoList.size() == 0)
		{
			return null;
		}
		return lspDcBoList;
	}

	/*
	 * This method fetch the delivery time for resolved zipGroups and DCs returns list of lsp-dc objects
	 */
	public List<LspDcBo> getLSPDCList(List<DCBo> dcBoList, List<LSPBo> lspBoList, ProductBo productBo, SingleArticleServiceabilityRequestBo serviceabilityRequestBo) {
		log.info("IFSDaoJdbcImpl.getLSPDCList()\n\n");
		String query = GET_LSPDC_BASIC;
		String dcList = getDCArray(dcBoList);
		String invIdList = getInvIdArray(dcBoList);
		String zipgroupList = getZipGroupArray(lspBoList);
		
		List<LspDcBo> lspDcBoList = null;
		Object[] params = null;
		if(productBo.isShipLocalOnly())
		{
			log.info("shipLocalOnly.....");
			query = GET_LSPDC_LOCAL+" and c.dc_id in ("+dcList+") and c.zipgroup_id in ("+zipgroupList+") and e.id in ("+invIdList+")";
			log.info("lsp-dc query :: " + query + " <<>> dcArray :: " + dcList + " <> zipgroupArray :: " + zipgroupList + " <<>> invIdList :: " + invIdList);
			lspDcBoList = jdbcTemplate.query(query, new Object[]{}, new LspDcMapper());
		}
		else
		{
			log.info("non-cod non-shipLocalOnly.....");
			params = new Object[1];
			params[0] = productBo.getShippingMode();
			query = GET_LSPDC_BASIC+" and c.dc_id in ("+dcList+") and c.zipgroup_id in ("+zipgroupList+") and e.id in ("+invIdList+")";
			log.info("lsp-dc query :: "+query + " <<>> dcArray :: " + dcList + " <> zipgroupArray :: " + zipgroupList + " <<>> invIdList :: " + invIdList);
			lspDcBoList = jdbcTemplate.query(query, params, new LspDcMapper());
		}
	
		log.info("lspDcBoList :: "+lspDcBoList);
		
		if(lspDcBoList == null || lspDcBoList.size() == 0)
		{
			log.info("no entry found in delivery chart...");
			return null;
		}
		
		return lspDcBoList;
	}

	private String getInvIdArray(List<DCBo> dcBoList) {
		String invIdArray = ""; 
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			invIdArray += dcBo.getInvId()+",";
		}
		if(!StringUtils.isEmpty(invIdArray))
		{
			invIdArray = invIdArray.substring(0,invIdArray.lastIndexOf(","));
		}
		log.info("dcList :: "+invIdArray);
		return invIdArray;
	}

	/**
	 * This method returns the comma seperated string of zipGroupIds
	 * @param lspBoList
	 * @return
	 */
	private String getZipGroupArray(List<LSPBo> lspBoList) {
		String zipGroupArray = ""; 
		for (Iterator<LSPBo> iterator = lspBoList.iterator(); iterator.hasNext(); ) {
			LSPBo lspBo = (LSPBo) iterator.next();
			zipGroupArray += lspBo.getZipGroupId()+",";
		}
		if(!StringUtils.isEmpty(zipGroupArray))
		{
			zipGroupArray = zipGroupArray.substring(0,zipGroupArray.lastIndexOf(","));
		}
		log.info("zipgroupList :: "+zipGroupArray);
		return zipGroupArray;
	}

}

