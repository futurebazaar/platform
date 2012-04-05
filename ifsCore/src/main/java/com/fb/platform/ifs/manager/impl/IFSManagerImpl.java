/**
 * 
 */
package com.fb.platform.ifs.manager.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.PlatformException;
import com.fb.platform.ifs.caching.LSPCacheAccess;
import com.fb.platform.ifs.caching.ProductGroupCacheAccess;
import com.fb.platform.ifs.dao.IFSDao;
import com.fb.platform.ifs.domain.DCBo;
import com.fb.platform.ifs.domain.LSPBo;
import com.fb.platform.ifs.domain.LspDcBo;
import com.fb.platform.ifs.domain.ProductBo;
import com.fb.platform.ifs.domain.SingleArticleServiceabilityRequestBo;
import com.fb.platform.ifs.manager.IFSManager;
import com.fb.platform.ifs.manager.model.IFSResultTO;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO;
import com.fb.platform.ifs.manager.model.SingleArticleServiceabilityResponseTO;
import com.fb.platform.ifs.mapper.IFSToBoMapper;
import com.fb.platform.ifs.util.Jsonizable;



/**
 * @author sarvesh
 *
 */
public class IFSManagerImpl implements IFSManager {
	
	ResourceBundle ifsResBndl = ResourceBundle.getBundle("ifsResources");
	
	private static Logger log = Logger.getLogger(IFSManagerImpl.class);
	
	public enum ResponseCode {SUCCESS,FAILURE,ERR_PRODGRP,ERR_CVG_AREA,ERR_LSP_COD,ERR_LSP_HV,ERR_INV,ERR_INV_COD_LOCAL,ERR_INV_COD,ERR_INV_LOCAL,ERR_LOCAL,ERR_DELV}
	
	static final String PRDGRP_NOT_FOUND = "SKUPrdGrpNotFound";
	
	static final String LSP_NOT_FOUND = "SKULspNotFound";
	
	static final String LSP_NOT_FOUND_FOR_COD = "SKULspNotFoundForCOD";
	
	static final String LSP_NOT_FOUND_FOR_HIGHVALUE = "SKULspNotFoundForHighValue";
	
	static final String OUT_OF_STOCK = "SKUOutOfStock";
	
	static final String PARTIAL_STOCK_AVAILABLE = "SKUPartialStockAvailable";
	
	static final String SKU_AVAILABLE = "SKUAvailable";
	
	@Autowired
	private ProductGroupCacheAccess productGroupCacheAccess = null;
	
	@Autowired
	private LSPCacheAccess lspCacheAccess = null;
	
	@Autowired
	private IFSDao ifsDao=null;
	
	/*
	 * The method checks serviceability for single article
	 *
	 * (non-Javadoc)
	 * @see com.fb.platform.ifs.manager.IFSManager#getSingleArticleServiceabilityInfo(com.fb.platform.ifs.manager.model.SingleArticleServiceabilityRequestTO)
	 */
	@Override
	public SingleArticleServiceabilityResponseTO getSingleArticleServiceabilityInfo(SingleArticleServiceabilityRequestTO serviceabilityRequestTO) {

		IFSToBoMapper ifsToBoMapper = new IFSToBoMapper();
		
		SingleArticleServiceabilityRequestBo serviceabilityRequestBo = ifsToBoMapper.updateBo(serviceabilityRequestTO);
		IFSResultTO ifsResultTO = new IFSResultTO(); 
		SingleArticleServiceabilityResponseTO art = new SingleArticleServiceabilityResponseTO();
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		String articleId = serviceabilityRequestBo.getArticleId();
		String clientId = serviceabilityRequestBo.getClient();
		ifsResultTO.setArticleId(articleId);
		
		String fbVendorId = ifsResBndl.getString("futurebazaarVendorId");
		boolean isThirdPartyProduct = false;
		if(StringUtils.isEmpty(fbVendorId) || !fbVendorId.equalsIgnoreCase(serviceabilityRequestBo.getVendorId()))
		{
			isThirdPartyProduct = true;
		}

		ifsResultTO.setThirdPartyProduct(isThirdPartyProduct);
		
		ProductBo productBo = null;
		String productBoKey = articleId + "_" + clientId;
		productBo = productGroupCacheAccess.get(productBoKey);
        if (productBo == null) {
            try {
            	productBo = ifsDao.getProductGroup(articleId, serviceabilityRequestBo.getClient(), serviceabilityRequestBo.getItemPrice());
                if (productBo != null) {
                    try {
                    	productGroupCacheAccess.lock(productBoKey);
                       	productGroupCacheAccess.put(productBoKey, productBo);
                    } finally {
                    	productGroupCacheAccess.unlock(productBoKey);
                    }
                }
            } 
            catch (PlatformException e) {
                log.error("Unable to authenticate with product bo ", e);
            } 
        }
		
		String message = "";
		
		if(productBo == null)
		{
			message = getFulfillmentMSG(PRDGRP_NOT_FOUND);	
			log.error(message+" for article :: "+serviceabilityRequestBo.getArticleId());
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			art.setStatusCode(ResponseCode.ERR_PRODGRP.toString());
			art.setStatusMessage(message);
			return art;
		}
		ifsResultTO.setProductGroup(productBo.getProductGroup());
		ifsResultTO.setShipLocalOnly(productBo.isShipLocalOnly());
		ifsResultTO.setHighValue(productBo.isHighValueFlag());
		ifsResultTO.setModOfTransport(productBo.getShippingMode());

		if(isThirdPartyProduct)
		{
			art = getSingleArticleServiceabilityInfoForThirdPartyProduct(productBo, serviceabilityRequestBo, ifsResultTO, art);
			return art;
		}
		
		String lspBoKey = serviceabilityRequestBo.getPincode() + "_" + productBo.getProductGroup();
		List<LSPBo> lspBoList =  lspCacheAccess.get(lspBoKey);
		if (lspBoList == null || lspBoList.size() == 0) {
			try{
				lspBoList = ifsDao.getLSPList(productBo, serviceabilityRequestBo);
				if (lspBoList != null && lspBoList.size() > 0) {
					try{
						lspCacheAccess.lock(lspBoKey);
						lspCacheAccess.put(lspBoKey, lspBoList);
					}
					finally{
						lspCacheAccess.unlock(lspBoKey);
					}
				}
			}
			catch (PlatformException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}	
		}	
		
		if(lspBoList == null || lspBoList.size() == 0)
		{
			message = getFulfillmentMSG(LSP_NOT_FOUND, serviceabilityRequestBo.getPincode());
			log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			art.setStatusCode(ResponseCode.ERR_CVG_AREA.toString());
			art.setStatusMessage(message);
			return art;
		}
		if(serviceabilityRequestBo.isCod())
		{
			lspBoList = filterLSPForCOD(lspBoList);
			log.info("lspBoList after cod filter :: " + lspBoList);
			if(lspBoList == null || lspBoList.size() == 0)
			{
				message = getFulfillmentMSG(LSP_NOT_FOUND_FOR_COD, serviceabilityRequestBo.getPincode());
				log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
				ljs.add(ifsResultTO);
				art.setObjects(ljs);
				art.setStatusCode(ResponseCode.ERR_LSP_COD.toString());
				art.setStatusMessage(message);
				return art;
			}
		}
		
		if(productBo.isHighValueFlag())
		{
			lspBoList = filterLSPForHighValue(lspBoList);
			log.info("lspBoList after high value filter :: " + lspBoList);
			if(lspBoList == null || lspBoList.size() == 0)
			{
				message = getFulfillmentMSG(LSP_NOT_FOUND_FOR_HIGHVALUE, serviceabilityRequestBo.getPincode());
				log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
				ljs.add(ifsResultTO);
				art.setObjects(ljs);
				art.setStatusCode(ResponseCode.ERR_LSP_HV.toString());
				art.setStatusMessage(message);
				return art;
			}
		}
		
		List<DCBo> dcBoList = ifsDao.getDcList(productBo, serviceabilityRequestBo);
		
		if(dcBoList == null || dcBoList.size() == 0)
		{
			log.info("no dc found...........");
			message = getFulfillmentMSG(OUT_OF_STOCK);
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			art.setStatusCode(ResponseCode.ERR_INV.toString());
			art.setStatusMessage(message);
			return art;
		}
		
		TreeMap<String, Integer> dcStockMap = new TreeMap<String, Integer>();
		Map<String, Integer> dcBackorderableTimeMap = new TreeMap<String, Integer>();
		
		populateDCStockDeltaTimeMap(dcBoList, dcStockMap, dcBackorderableTimeMap);

		String dcWithMaxStock = getDCWithMaxStock(dcStockMap);
		int maxQtyInSingleDC = dcStockMap.get(dcWithMaxStock);
		
		boolean isBackOrderable = false;
		if(dcBackorderableTimeMap != null && dcBackorderableTimeMap.size() > 0)
		{
			isBackOrderable = true;
			ifsResultTO.setBackorderable(Boolean.TRUE);
		}
		else
		{
			ifsResultTO.setBackorderable(Boolean.FALSE);
		}
		
		boolean isAllQuantityFulfilledWithoutFilter = (maxQtyInSingleDC >= serviceabilityRequestBo.getQty());
		if(!isAllQuantityFulfilledWithoutFilter && !isBackOrderable)
		{
			message = getFulfillmentMSG(PARTIAL_STOCK_AVAILABLE, maxQtyInSingleDC);
			log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			art.setStatusCode(ResponseCode.ERR_INV.toString());
			art.setStatusMessage(message);	
			return art;
		}
		
		boolean isAllQuantityFulfilledWithFilter = true;
		
		if(serviceabilityRequestBo.isCod() || productBo.isShipLocalOnly())
		{
			dcBoList = filterDCsForLocal(dcBoList, serviceabilityRequestBo, productBo);
			if(dcBoList == null || dcBoList.size() == 0)
			{
				log.info("dcBoList after Local filter :: " + dcBoList);
				
				message = getFulfillmentMSG(OUT_OF_STOCK);
				ljs.add(ifsResultTO);
				art.setObjects(ljs);
				art.setStatusCode(ResponseCode.ERR_INV_LOCAL.toString());
				art.setStatusMessage(message);
				return art;
			}
			
			dcBoList = filterDCsForCOD(dcBoList, serviceabilityRequestBo, productBo);
			if(dcBoList == null || dcBoList.size() == 0)
			{
				log.info("dcBoList after COD filter :: " + dcBoList);
				
				message = getFulfillmentMSG(OUT_OF_STOCK);
				ljs.add(ifsResultTO);
				art.setObjects(ljs);
				art.setStatusCode(ResponseCode.ERR_INV_COD.toString());
				art.setStatusMessage(message);
				return art;
			}
			
			dcStockMap = new TreeMap<String, Integer>();
			dcBackorderableTimeMap = new TreeMap<String, Integer>();
			
			populateDCStockDeltaTimeMap(dcBoList, dcStockMap, dcBackorderableTimeMap);
			
			dcWithMaxStock = getDCWithMaxStock(dcStockMap);
			maxQtyInSingleDC = dcStockMap.get(dcWithMaxStock);
			
			isBackOrderable = false;
			if(dcBackorderableTimeMap != null && dcBackorderableTimeMap.size() > 0)
			{
				isBackOrderable = true;
				ifsResultTO.setBackorderable(Boolean.TRUE);
			}
			else
			{
				ifsResultTO.setBackorderable(Boolean.FALSE);
			}
			
			isAllQuantityFulfilledWithFilter = (maxQtyInSingleDC >= serviceabilityRequestBo.getQty());
			if(!isAllQuantityFulfilledWithFilter && !isBackOrderable)
			{
				message = getFulfillmentMSG(PARTIAL_STOCK_AVAILABLE, maxQtyInSingleDC);
				log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
				ljs.add(ifsResultTO);
				art.setObjects(ljs);
				if(productBo.isShipLocalOnly())
				{
					art.setStatusCode(ResponseCode.ERR_INV_LOCAL.toString());
				}
				else {
					art.setStatusCode(ResponseCode.ERR_INV_COD.toString());
				}
				art.setStatusMessage(message);	
				return art;
			}
		}
		
		long totalQtyFound = getTotalQtyFound(dcBoList);
		ifsResultTO.setTotalQuantityFound(totalQtyFound);
		
		log.info("dcWithMaxStock :: " + dcWithMaxStock);

		String dcStockString = getDCStockString(dcStockMap);
		String dcPHStockString = getDCPHStockString(dcBoList);
		ifsResultTO.setDcStockString(dcStockString);
		ifsResultTO.setDcPhysicalStockString(dcPHStockString);
		log.info("dcStockMap :: "+dcStockMap);
		
		List<LspDcBo> lspDcBoList = ifsDao.getLSPDCList(dcBoList, lspBoList, productBo, serviceabilityRequestBo);
		
		if(lspDcBoList == null || lspDcBoList.size() == 0)
		{
			message = getFulfillmentMSG(OUT_OF_STOCK);
			log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			if(productBo.isShipLocalOnly())
			{
				art.setStatusCode(ResponseCode.ERR_LOCAL.toString());
			}
			else {
				art.setStatusCode(ResponseCode.ERR_DELV.toString());
			}
			art.setStatusMessage(message);
			return art;
		}
		
		String processingTimeForPI = ifsResBndl.getString("processingTimeForPI");
		ifsResultTO.setProcessingTimeForPI(processingTimeForPI);
		setLSPZGInfo(lspDcBoList, lspBoList);
		setProcessingTimeAndStockLevel(lspDcBoList, dcBoList, isThirdPartyProduct);
		
		log.info("dcStockMap :: "+dcStockMap);
		log.info("lspDcBoList after setting delta del time :: "+lspDcBoList);
		JSONArray dcLspSequence = getDcLspSequence(lspDcBoList, dcStockMap);
		ifsResultTO.setDcLspSequence(dcLspSequence.toString());
		
		LspDcBo primaryDCLSP = lspDcBoList.get(0);
		
		log.info("primaryDCLSP :: " + primaryDCLSP);
		ifsResultTO.setAllQuantityFulfilled(primaryDCLSP.getStockLevel() >= serviceabilityRequestBo.getQty());
		
		int totalDeliveryTime = primaryDCLSP.getTotalDeliveryTime();
		
		ifsResultTO.setPrimaryDCLSP(primaryDCLSP.getDcCode()+"_"+primaryDCLSP.getLspCode());
		ifsResultTO.setDeliveryTime(primaryDCLSP.getDeliveryTime());
		ifsResultTO.setInventoryTime(primaryDCLSP.getInventoryTime());
		ifsResultTO.setTotalDeliveryTime(totalDeliveryTime);
		ifsResultTO.setDefaultDC("");
		
		message = getFulfillmentMSG(SKU_AVAILABLE, totalDeliveryTime);
		log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
		art.setStatusCode(ResponseCode.SUCCESS.toString());
		art.setStatusMessage(message);
		ifsResultTO.setFlfMessages(message);
		ljs.add(ifsResultTO);
		art.setObjects(ljs);
		return art;
	}
	
	private SingleArticleServiceabilityResponseTO getSingleArticleServiceabilityInfoForThirdPartyProduct(ProductBo productBo,
			SingleArticleServiceabilityRequestBo serviceabilityRequestBo, IFSResultTO ifsResultTO, SingleArticleServiceabilityResponseTO art) {
		List<DCBo> dcBoList = ifsDao.getDcList(productBo, serviceabilityRequestBo);
		String message = "";
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		if(dcBoList == null || dcBoList.size() == 0)
		{
			log.info("no dc found...........");
			message = getFulfillmentMSG(OUT_OF_STOCK);
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			art.setStatusCode(ResponseCode.ERR_INV.toString());
			art.setStatusMessage(message);
			return art;
		}
		
		TreeMap<String, Integer> dcStockMap = new TreeMap<String, Integer>();
		Map<String, Integer> dcBackorderableTimeMap = new TreeMap<String, Integer>();
		
		populateDCStockDeltaTimeMap(dcBoList, dcStockMap, dcBackorderableTimeMap);

		String dcWithMaxStock = getDCWithMaxStock(dcStockMap);
		int maxQtyInSingleDC = dcStockMap.get(dcWithMaxStock);
		
		boolean isBackOrderable = false;
		if(dcBackorderableTimeMap != null && dcBackorderableTimeMap.size() > 0)
		{
			isBackOrderable = true;
			ifsResultTO.setBackorderable(Boolean.TRUE);
		}
		else
		{
			ifsResultTO.setBackorderable(Boolean.FALSE);
		}
		
		boolean isAllQuantityFulfilledWithoutFilter = (maxQtyInSingleDC >= serviceabilityRequestBo.getQty());
		if(!isAllQuantityFulfilledWithoutFilter && !isBackOrderable)
		{
			message = getFulfillmentMSG(PARTIAL_STOCK_AVAILABLE, maxQtyInSingleDC);
			log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			art.setStatusCode(ResponseCode.ERR_INV.toString());
			art.setStatusMessage(message);	
			return art;
		}
		
		List<LspDcBo> lspDcBoList = ifsDao.getLspDcBoForThirdPartyProduct(dcBoList, productBo, serviceabilityRequestBo);
		
		if(lspDcBoList == null)
		{
			message = getFulfillmentMSG(OUT_OF_STOCK);
			log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
			ljs.add(ifsResultTO);
			art.setObjects(ljs);
			if(productBo.isShipLocalOnly())
			{
				art.setStatusCode(ResponseCode.ERR_LOCAL.toString());
			}
			else {
				art.setStatusCode(ResponseCode.ERR_DELV.toString());
			}
			art.setStatusMessage(message);
			return art;
		}
		
		setProcessingTimeAndStockLevel(lspDcBoList, dcBoList, true);
		JSONArray dcLspSequence = getDcLspSequence(lspDcBoList, dcStockMap);
		ifsResultTO.setDcLspSequence(dcLspSequence.toString());
		
		LspDcBo primaryDCLSP = lspDcBoList.get(0);
		
		log.info("primaryDCLSP :: " + primaryDCLSP);
		ifsResultTO.setAllQuantityFulfilled(primaryDCLSP.getStockLevel() >= serviceabilityRequestBo.getQty());
		
		int totalDeliveryTime = primaryDCLSP.getTotalDeliveryTime();
		
		ifsResultTO.setPrimaryDCLSP(primaryDCLSP.getDcCode()+"_"+primaryDCLSP.getLspCode());
		ifsResultTO.setDeliveryTime(primaryDCLSP.getDeliveryTime());
		ifsResultTO.setInventoryTime(primaryDCLSP.getInventoryTime());
		ifsResultTO.setTotalDeliveryTime(totalDeliveryTime);
		ifsResultTO.setDefaultDC("");
		
		message = getFulfillmentMSG(SKU_AVAILABLE, totalDeliveryTime);
		log.info(message+" for article : "+serviceabilityRequestBo.getArticleId());
		art.setStatusCode(ResponseCode.SUCCESS.toString());
		art.setStatusMessage(message);
		ifsResultTO.setFlfMessages(message);
		ljs.add(ifsResultTO);
		art.setObjects(ljs);
		return art;
	}

	private void setProcessingTimeAndStockLevel(List<LspDcBo> lspDcBoList, List<DCBo> dcBoList, boolean isThirdPartyProduct) {
		String processingTimeForPI = ifsResBndl.getString("processingTimeForPI");
		String processingTimeForBackOrderable = ifsResBndl.getString("processingTimeForBackOrderable");
		String processingTimeForMadeToOrder = ifsResBndl.getString("processingTimeForMadeToOrder");
		String processingTimeForPreOrder = ifsResBndl.getString("processingTimeForPreOrder");

		for (Iterator<DCBo> itr = dcBoList.iterator(); itr.hasNext(); ) {
			int processingTimeForVI = 0;
			DCBo dcBo = (DCBo) itr.next();
			String dcCode = dcBo.getDcId();
			if(StringUtils.isEmpty(dcCode))
			{
				continue;
			}
			for (Iterator<LspDcBo> iterator = lspDcBoList.iterator(); iterator.hasNext(); ) {
				LspDcBo lspDcBo = (LspDcBo) iterator.next();
				int deliveryTime = lspDcBo.getDeliveryTime();
				
				if(StringUtils.isEmpty(lspDcBo.getOrderType()))
				{
					continue;
				}
				int deltaTime = 0;
				if(dcBo.getDcId().equals(lspDcBo.getDcCode()) && dcBo.getType().equalsIgnoreCase(lspDcBo.getOrderType()))
				{
					deltaTime = dcBo.getDeltaDeliveryTime();
					lspDcBo.setStockLevel(dcBo.getStockLevel());
					lspDcBo.setInventoryTime(dcBo.getDeltaDeliveryTime());
				}
				if(dcBo.getType().equalsIgnoreCase(lspDcBo.getOrderType()) && lspDcBo.getOrderType().equalsIgnoreCase("backorder"))
				{
					if(!isThirdPartyProduct)
					{
						processingTimeForVI = (processingTimeForBackOrderable == null) ? 0 : Integer.parseInt(processingTimeForBackOrderable);
						lspDcBo.setProcessingTimeForVI(processingTimeForBackOrderable);
					}
					else {
						processingTimeForVI = StringUtils.isEmpty(lspDcBo.getProcessingTimeForVI()) ? 0 : Integer.parseInt(lspDcBo.getProcessingTimeForVI());
					}
					lspDcBo.setTotalDeliveryTime(deliveryTime + deltaTime + processingTimeForVI);
					lspDcBo.setDcPriority(3);
				} else if (dcBo.getType().equalsIgnoreCase(lspDcBo.getOrderType()) && lspDcBo.getOrderType().equalsIgnoreCase("madetoorder")) {
					if(!isThirdPartyProduct)
					{
						processingTimeForVI = (processingTimeForMadeToOrder == null) ? 0 : Integer.parseInt(processingTimeForMadeToOrder);
						lspDcBo.setProcessingTimeForVI(processingTimeForMadeToOrder);
					}
					else {
						processingTimeForVI = StringUtils.isEmpty(lspDcBo.getProcessingTimeForVI()) ? 0 : Integer.parseInt(lspDcBo.getProcessingTimeForVI());
					}
					lspDcBo.setTotalDeliveryTime(deliveryTime + deltaTime + processingTimeForVI);
					lspDcBo.setDcPriority(4);
				} else if (dcBo.getType().equalsIgnoreCase(lspDcBo.getOrderType()) && lspDcBo.getOrderType().equalsIgnoreCase("preorder")) {
					if(!isThirdPartyProduct)
					{
						processingTimeForVI = (processingTimeForPreOrder == null) ? 0 : Integer.parseInt(processingTimeForPreOrder);
						lspDcBo.setProcessingTimeForVI(processingTimeForPreOrder);
					}
					else {
						processingTimeForVI = StringUtils.isEmpty(lspDcBo.getProcessingTimeForVI()) ? 0 : Integer.parseInt(lspDcBo.getProcessingTimeForVI());
					}
					lspDcBo.setTotalDeliveryTime(deliveryTime + deltaTime + processingTimeForVI);
					lspDcBo.setDcPriority(2);
				} else if (dcBo.getType().equalsIgnoreCase(lspDcBo.getOrderType()) && lspDcBo.getOrderType().equalsIgnoreCase("physical")) {
					if(!isThirdPartyProduct)
					{
						processingTimeForVI = (processingTimeForPI == null) ? 0 : Integer.parseInt(processingTimeForPI);
						lspDcBo.setProcessingTimeForVI(processingTimeForPI);
					}
					else {
						processingTimeForVI = StringUtils.isEmpty(lspDcBo.getProcessingTimeForVI()) ? 0 : Integer.parseInt(lspDcBo.getProcessingTimeForVI());
					}
					lspDcBo.setTotalDeliveryTime(deliveryTime + deltaTime + processingTimeForVI);
					lspDcBo.setDcPriority(1);
				}
			}
		}
		Collections.sort(lspDcBoList);		
	}

	/**
	 * This method returns the dc having maximum stock
	 * @param dcStockMap
	 * @return
	 */
	private String getDCWithMaxStock(TreeMap<String, Integer> dcStockMap) {
		if(dcStockMap == null || dcStockMap.size() == 0)
		{
			return null;
		}
		
		String dcId = dcStockMap.firstKey();
		int val1 = 0;
		for (Entry<String, Integer> entry : dcStockMap.entrySet()) {
		    int val2 = entry.getValue();
			if(val2 > val1)
			{
				val1 = val2;
				dcId = entry.getKey();
			}
		}
		return dcId;
	}

	/**
	 * This method returns the list of lsps which are eligible for COD  
	 * @param lspBoList
	 */
	private List<LSPBo> filterLSPForCOD(List<LSPBo> lspBoList) {
		List<LSPBo> lspBoListRevised = new ArrayList<LSPBo>();
		for (int i = 0; i < lspBoList.size(); i++) {
			LSPBo lspBo = (LSPBo) lspBoList.get(i);
			if(lspBo.isCod())
			{
				lspBoListRevised.add(lspBo);
			}
		}
		return lspBoListRevised;
	}
	
	/**
	 * This method returns the list of lsps which carry high value products
	 * @param lspBoList
	 */
	private List<LSPBo> filterLSPForHighValue(List<LSPBo> lspBoList) {
		List<LSPBo> lspBoListRevised = new ArrayList<LSPBo>();
		for (int i = 0; i < lspBoList.size(); i++) {
			LSPBo lspBo = (LSPBo) lspBoList.get(i);
			if(lspBo.isHighValue())
			{
				lspBoListRevised.add(lspBo);
			}
		}
		return lspBoListRevised;
	}

	/**
	 * This method removes non-local DCs from the list
	 * @param dcBoList
	 * @param ifsBo
	 * @param productBo
	 */
	private List<DCBo> filterDCsForLocal(List<DCBo> dcBoList,
			SingleArticleServiceabilityRequestBo ifsBo, ProductBo productBo) {
		boolean isShipLocalOnly = productBo.isShipLocalOnly();
		if(!isShipLocalOnly){
			return dcBoList;
		}
		List<DCBo> dcBoListRevised = new ArrayList<DCBo>();
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			if(dcBo.isLocalDC())
			{
				dcBoListRevised.add(dcBo);
			}
		}
		return dcBoListRevised;
	}

	/**
	 * This method removes non-cod DCs from the list
	 * @param dcBoList
	 * @param ifsBo
	 * @param productBo
	 */
	private List<DCBo> filterDCsForCOD(List<DCBo> dcBoList,
			SingleArticleServiceabilityRequestBo ifsBo, ProductBo productBo) {
		boolean isCod = ifsBo.isCod();
		if(!isCod){
			return dcBoList;
		}
		List<DCBo> dcBoListRevised = new ArrayList<DCBo>();
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			if(dcBo.isCod())
			{
				dcBoListRevised.add(dcBo);
			}
		}
		return dcBoListRevised;
	}
	
	
	/**
	 * This method sets lspPriority, zipGroupCode, lspCode into lspDc object
	 * @param lspDcBoList
	 * @param lspBoList
	 */
	private void setLSPZGInfo(List<LspDcBo> lspDcBoList, List<LSPBo> lspBoList) {
		for (Iterator<LspDcBo> iterator = lspDcBoList.iterator(); iterator.hasNext(); ) {
			LspDcBo lspDcBo = (LspDcBo) iterator.next();
			if(lspDcBo == null)
			{
				continue;
			}
			String zipgroupId = lspDcBo.getZipGroupId();
			String lspId = lspDcBo.getLspId();
			if(StringUtils.isEmpty(zipgroupId) || StringUtils.isEmpty(lspId))
			{
				continue;
			}
			for (Iterator<LSPBo> iterator2 = lspBoList.iterator(); iterator2.hasNext(); ) {
				LSPBo lspBo = (LSPBo) iterator2.next();
				if(lspBo == null)
				{
					continue;
				}
				if(zipgroupId.equalsIgnoreCase(lspBo.getZipGroupId()) && lspId.equalsIgnoreCase(lspBo.getLspId()))
				{
					lspDcBo.setLspPriority(lspBo.getLspPriority());
					lspDcBo.setZipGroupCode(lspBo.getZipGroupCode());
					lspDcBo.setLspCode(lspBo.getLspCode());
				}
			}
		}
	}

	/**
	 * @param messageCode
	 * @param params
	 * @return
	 */
	private String getFulfillmentMSG(String messageCode, Object... params)
	{
		String message = "";
		try {
			message = ifsResBndl.getString(messageCode);
			if (params != null) {
				message = MessageFormat.format(message, params);
			}
		} 
		catch (Exception e) {
			 e.printStackTrace();
			 log.error(e.getMessage());
		}
		log.info("\n\n\n flf message :::::::::::: "+message);
		return message;
	}
	
	/**
	 * The method returns dc and physical stock sequence seperated with comma
	 * @param dcBoList
	 * @return
	 */
	private String getDCPHStockString(List<DCBo> dcBoList) {
		String dcPHStockStr = "";
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			String invType = dcBo.getType();
			if(!StringUtils.isEmpty(invType) && invType.equalsIgnoreCase("physical"))
			{
				dcBo.setPhysicalStock(dcBo.getStockLevel());
				if(StringUtils.isEmpty(dcPHStockStr) || dcPHStockStr.indexOf(dcBo.getDcId()+"_"+dcBo.getStockLevel()+",") == -1)
				{
					dcPHStockStr += dcBo.getDcId()+"_"+dcBo.getStockLevel()+",";
				}
			}
		}
		
		if(StringUtils.isEmpty(dcPHStockStr)){
			return dcPHStockStr;
		}
		dcPHStockStr = dcPHStockStr.substring(0, dcPHStockStr.lastIndexOf(","));
		
		return dcPHStockStr;
	}

	/**
	 * The method returns dc and stock (physical + virtual) sequence seperated with comma
	 * @param dcStockMap
	 * @return
	 */
	private String getDCStockString(Map<String, Integer> dcStockMap) {
		String dcStockStr = "";
		if(dcStockMap == null || dcStockMap.size() == 0)
		{
			return dcStockStr;
		}
		Iterator<Entry<String, Integer>> it = dcStockMap.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
	        log.info(pairs.getKey() + " = " + pairs.getValue());
	        dcStockStr += pairs.getKey()+"_"+pairs.getValue()+",";
	    }
		if(StringUtils.isEmpty(dcStockStr)){
			return dcStockStr;
		}
		dcStockStr = dcStockStr.substring(0, dcStockStr.lastIndexOf(","));
		
		return dcStockStr;
	}


	/**
	 * The method returns total stock in all DCs
	 * @param dcBoList
	 * @return
	 */
	private long getTotalQtyFound(List<DCBo> dcBoList) {
		long totalQtyFound = 0;
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			totalQtyFound += dcBo.getStockLevel();
		}
		return totalQtyFound;
	}

	/**
	 * The method populate the map of dc to stock (physical + virtual), dc to stock expected in days
	 * @param dcBoList
	 * @param dcStockMap
	 * @param dcBackorderableTimeMap
	 */
	private void populateDCStockDeltaTimeMap(List<DCBo> dcBoList, Map<String, Integer> dcStockMap, Map<String, Integer> dcBackorderableTimeMap) {
		
		for (Iterator<DCBo> iterator = dcBoList.iterator(); iterator.hasNext(); ) {
			DCBo dcBo = (DCBo) iterator.next();
			int stockLevel = dcBo.getStockLevel();
			if(dcStockMap != null && dcStockMap.get(dcBo.getDcId()) != null )
			{
				stockLevel += dcStockMap.get(dcBo.getDcId());
			}
			dcStockMap.put(dcBo.getDcId(), stockLevel);
			
			boolean isBackorderable = dcBo.isBackOrderable();
			if (isBackorderable) {
				dcBackorderableTimeMap.put(dcBo.getDcId(), dcBo.getExpectedInDays());
			}
		}
		Collections.sort(dcBoList);
	}

	/**
	 * This method returns comma seperated sequence of DC, lsp, delivery time for physical stock, virtual stock and backorderable stock 
	 * @param lspDcBoList
	 * @param dcStockMap
	 * @param dcBackorderableTimeMap
	 * @return
	 */
	private JSONArray getDcLspSequence(List<LspDcBo> lspDcBoList, Map<String, Integer> dcStockMap) {
		JSONArray dcLspSequenceArray = new JSONArray();
		for (Iterator<LspDcBo> iterator = lspDcBoList.iterator(); iterator.hasNext(); ) {
			JSONObject dcLspObject = new JSONObject();
			LspDcBo lspDcBo = (LspDcBo) iterator.next();
						
			int shippingTime = lspDcBo.getInventoryTime() + (!StringUtils.isEmpty(lspDcBo.getProcessingTimeForVI()) ? Integer.parseInt(lspDcBo.getProcessingTimeForVI()) : 0);
			try {
				dcLspObject.put("dc", lspDcBo.getDcCode());
			
				dcLspObject.put("lsp", lspDcBo.getLspCode());
				dcLspObject.put("order_type", lspDcBo.getOrderType());
				dcLspObject.put("stock_expected", lspDcBo.getInventoryTime());
				dcLspObject.put("shipping", shippingTime);
				dcLspObject.put("delivery", lspDcBo.getTotalDeliveryTime());
				
				dcLspSequenceArray.put(dcLspObject);
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return dcLspSequenceArray;
	}

	/**
	 * @param ifsDao the ifsDao to set
	 */
	public void setIfsDao(IFSDao ifsDao) {
		this.ifsDao = ifsDao;
	}

}


