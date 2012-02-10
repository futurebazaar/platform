package com.fb.api.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.fb.api.APIRequest;
import com.fb.api.APIResponse;
import com.fb.api.interfaces.Jsonizable;
import com.fb.bo.promotion.PromotionBO;
import com.fb.bo.promotion.PromotionOrder;
import com.fb.bo.promotion.PromotionType;
import com.fb.dao.promotion.PromotionDao;

public class OldPromotionManagerImpl  {
/*
	private static Logger logger = Logger.getLogger(OldPromotionManagerImpl.class);
	public void setPromotionDao(PromotionDao promotionsDao) {
		this.promotionDao = promotionsDao;
	}
	
	private PromotionDao promotionDao;

	@Transactional
	public String getPromotionById(Integer promotionId) {
		PromotionBo promotion = promotionDao.get(promotionId);
		return promotion.toString();
	}

	@Transactional
	public APIResponse getPromotionsById(APIRequest req) {
		
		int id = req.getParamAsInt("id");
		logger.info("Retrieving promotions by id = " + id);
		
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		PromotionBo pbo = promotionDao.get(id);
		ljs.add(pbo);
		
		APIResponse apr = new APIResponse();
		apr.setObjects(ljs);
		apr.setNumFound(ljs.size());
		apr.setNumReturned(ljs.size());
		
		logger.info("Found = " + ljs.size());
		return apr;
	}

	@Transactional
	public APIResponse getPromotionsByOrderId(APIRequest req) {
		
		int orderId = req.getParamAsInt("order_id");
		logger.info("Retrieving promotions by order id = " + orderId);
		
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		List<PromotionBo> lpbo = promotionDao.getByOrderID(orderId);
		for(PromotionBo pbo: lpbo){
			ljs.add(pbo);
		}
		APIResponse apr = new APIResponse();
		apr.setObjects(ljs);
		apr.setNumFound(ljs.size());
		apr.setNumReturned(ljs.size());
		
		logger.info("Found = " + ljs.size());
		return apr;
	}	


	@Transactional
	public APIResponse deleteById(APIRequest request) {
		int id = request.getParamAsInt("id");
		logger.info("Deleting promotion with id = " + id);
		promotionDao.delete(id);
		APIResponse apr = new APIResponse();
		//apr.setStatusMessage(new Boolean(result).toString());
		return apr;
	}


	@Transactional
	public APIResponse add(APIRequest request) {
		logger.info("Adding promotions with post data = " + request.getPostData().toString());
		PromotionBo promotionBo = PromotionBo.fromJson(request.getPostData());
		promotionDao.create(promotionBo);
		
		APIResponse apr = new APIResponse();
		return apr;
	}


	@Transactional
	public APIResponse update(APIRequest request) {
		logger.info("Updating promotion with post data = " + request.getPostData().toString());
		PromotionBo promotionBo = PromotionBo.fromJson(request.getPostData());
		promotionDao.update(promotionBo);
		APIResponse apr = new APIResponse();
		return apr;
	}
	@Transactional
	public APIResponse getApplicablePromotionsForOrder(APIRequest request){
		logger.info("Getting applicable promotions for order = " + request.getPostData().toString());
		PromotionOrder promotionOrder = PromotionOrder.fromJson(request.getPostData());
		List<PromotionBo> result = new ArrayList<PromotionBo>();
		
		//-------------------------------------------------------//	
		// Promotion applied on products. 
		// Add promotions which contain bundles with all product ids present in the given order.
		List<Integer> productIds = promotionOrder.getProductIds();
		//List<PromotionBo> promotionBos = promotionDao.getPromotionsByProductIds(productIds);
		List<PromotionBo> promotionBos = new ArrayList<PromotionBo>();

		for (Integer productId : productIds) {
			promotionBos.add(promotionDao.getByProductId(productId));
		}
		
		if (!promotionBos.isEmpty()) {
			result.addAll(promotionBos);
		}
		
		//Promotion applied on order total 
		//Add all promtions with minAmountValue < orderTotal
		double orderTotal =  promotionOrder.getOrderTotal();
		promotionBos = promotionDao.getByMinAmount(orderTotal);
		if(promotionBos!=null){
			result.addAll(promotionBos);
		}
		
		//-------------------------------------------------------//
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		for(PromotionBo pbo: result){
			ljs.add(pbo);
		}
		APIResponse apr = new APIResponse();
		apr.setObjects(ljs);
		apr.setNumFound(ljs.size());
		apr.setNumReturned(ljs.size());
		logger.info("Found = " + ljs.size());
		return apr;
	}
	
	

	@Transactional
	public APIResponse getActivePromotions(APIRequest req) {
		
		logger.info("Retrieving Active promotions");
		
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		List<PromotionBo> lpbo = promotionDao.getAllActive();
		for(PromotionBo pbo: lpbo){
			ljs.add(pbo);
		}
		APIResponse apr = new APIResponse();
		apr.setObjects(ljs);
		apr.setNumFound(ljs.size());
		apr.setNumReturned(ljs.size());
		
		logger.info("Found = " + ljs.size());
		return apr;
		
	}
	
	private void applyPromotion_GlobalCoupon(PromotionOrder promotionOrder, PromotionBo promotionBo){
					
		promotionOrder.addAppliedPromotion(PromotionType.GLOBAL_COUPON.toString());
	
		if(promotionBo.getAppliesOn().equalsIgnoreCase("Order")){
			
			if(promotionBo.getDiscountType().equalsIgnoreCase("Rupees")){
									
				double newOrderTotal = applyFlatDiscount(promotionOrder.getOrderTotal(),promotionBo.getDiscountValue(),promotionBo.getCeil());
				promotionOrder.setDiscountValue(promotionOrder.getOrderTotal()-newOrderTotal);
				promotionOrder.setDiscountType("Order");
				promotionOrder.setOrderTotal(newOrderTotal);
				
				
			}else if(promotionBo.getDiscountType().equalsIgnoreCase("Percent")){ 
				
				double newOrderTotal = applyPercentDiscount(promotionOrder.getOrderTotal(),promotionBo.getDiscountValue(),promotionBo.getCeil());
				promotionOrder.setDiscountValue(promotionOrder.getOrderTotal()-newOrderTotal);
				promotionOrder.setDiscountType("Order");
				promotionOrder.setOrderTotal(newOrderTotal);
			}
				
			
		}else if(promotionBo.getAppliesOn().equalsIgnoreCase("Payment")){
			if(promotionBo.getDiscountType().equalsIgnoreCase("Rupees")){
				
				double newPaymentTotal = applyFlatDiscount(promotionOrder.getPaymentCharges(),promotionBo.getDiscountValue(),promotionBo.getCeil());
				promotionOrder.setDiscountValue(promotionOrder.getPaymentCharges()-newPaymentTotal);
				promotionOrder.setDiscountType("Payment");
				promotionOrder.setPaymentCharges(newPaymentTotal);
				
			}else if(promotionBo.getDiscountType().equalsIgnoreCase("Percent")){ 
				
				double newPaymentTotal = applyPercentDiscount(promotionOrder.getPaymentCharges(),promotionBo.getDiscountValue(),promotionBo.getCeil());
				promotionOrder.setDiscountValue(promotionOrder.getPaymentCharges()-newPaymentTotal);
				promotionOrder.setDiscountType("Payment");
				promotionOrder.setPaymentCharges(newPaymentTotal);
			}
			
			
		}else if(promotionBo.getAppliesOn().equalsIgnoreCase("Shipping")){
			if(promotionBo.getDiscountType().equalsIgnoreCase("Rupees")){
				
				double newshippingTotal = applyFlatDiscount(promotionOrder.getShippingTotal(), promotionBo.getDiscountValue(),promotionBo.getCeil());
				promotionOrder.setDiscountValue(promotionOrder.getShippingTotal()-newshippingTotal);
				promotionOrder.setDiscountType("Shipping");
				promotionOrder.setShippingTotal(newshippingTotal);
				
			}else if(promotionBo.getDiscountType().equalsIgnoreCase("Percent")){ 
				
				double newshippingTotal = applyPercentDiscount(promotionOrder.getShippingTotal(),promotionBo.getDiscountValue(),promotionBo.getCeil());
				promotionOrder.setDiscountValue(promotionOrder.getShippingTotal()-newshippingTotal);
				promotionOrder.setDiscountType("Shipping");
				promotionOrder.setShippingTotal(newshippingTotal);
			}
		}
				
	}
	
	private double applyFlatDiscount(double baseAmount,double discount, double ceil){
		
		
		if(ceil!=0 && discount>ceil){
			discount = ceil;
		}
		if((baseAmount - discount)>0){
			return baseAmount - discount;
		}else{
			return 0;
		}
	}

	private double applyPercentDiscount(double baseAmount,double percent, double ceil){
		
		double discount = (baseAmount*percent)/100;
		if(ceil!=0 && discount>ceil){
			discount = ceil;
		}
	    if((baseAmount - discount)>0){
	    	return baseAmount - discount;
	    }else{
	    	return 0;
	    }
	}
	
	private boolean isDateValid(PromotionBo promotionBo){
		long startTime = promotionBo.getStartDate().getTime();
		long endTime = promotionBo.getEndDate().getTime();
		Date date = new Date();
		long thisTime = date.getTime();
		
		if(startTime<=thisTime && thisTime<=endTime){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Transactional
	public APIResponse applyPromotionOnOrder(APIRequest request) {
		logger.info("Applying promotions on order = " + request.getPostData().toString());
		PromotionOrder promotionOrder = PromotionOrder.fromJson(request.getPostData());
		
		
		// APPLY PROMOTIONS COUPONS 
		if(promotionOrder.getCouponCode()!=null){
			List<PromotionBo> couponPromotions = promotionDao.getByCouponCode(promotionOrder.getCouponCode());
		
			for(PromotionBo promotionBo : couponPromotions){
				
				int nUses = promotionDao.getPreviousUsesForUser(promotionOrder.getUserId(),promotionBo.getPromotionId());
				
				if(isDateValid(promotionBo) && nUses < promotionBo.getMaxUsesPerUser()){
		
					if( promotionBo.getPromotionType() != null && promotionBo.getPromotionType().trim().equalsIgnoreCase(PromotionType.GLOBAL_COUPON.toString())){
							applyPromotion_GlobalCoupon(promotionOrder,promotionBo);
							//TODO : This must be done after Order Confirmation!!.
							promotionDao.updatePreviousUsesForUser(promotionOrder.getUserId(),promotionBo.getPromotionId(),nUses+1);
					}
									
				}
				
			}
		}
		List<Jsonizable> ljs = new ArrayList<Jsonizable>();
		ljs.add(promotionOrder);
		
		APIResponse apr = new APIResponse();
		apr.setObjects(ljs);
		apr.setNumFound(ljs.size());
		apr.setNumReturned(ljs.size());
 
		
		return apr;
	}
*/
}
