/**
 * 
 */
package com.fb.api.promotion.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fb.api.promotion.PromotionManager;
import com.fb.bo.promotion.PromotionBO;
import com.fb.commons.promotion.to.PromotionOrderTO;
import com.fb.commons.promotion.to.PromotionTO;
import com.fb.dao.promotion.PromotionDao;

/**
 * @author Keith Fernandez
 *
 */
public class PromotionManagerImpl implements PromotionManager {

	private PromotionDao promotionDao;

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getPromotion(java.lang.Integer)
	 */
	@Override
	@Transactional
	public PromotionTO getPromotion(Integer promotionId) {
		PromotionBO promotionBo = promotionDao.get(promotionId);
		//map bo to to
		
		PromotionTO pto = new PromotionTO();
//		pto.setAppliesOn(promotionBo.getAppliesOn());
//		pto.setBundleId(promotionBo.getBundleId());
		//pto.setPromotionBundle(promotionBo.getPromotionBundle());
		pto.setPromotionId(promotionBo.getPromotionId());
		return pto;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#deletePromotion(java.lang.Integer)
	 */
	@Override
	@Transactional
	public void deletePromotion(Integer promotionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#createPromotion(com.fb.commons.promotion.to.PromotionTO)
	 */
	@Override
	@Transactional
	public void createPromotion(PromotionTO promotion) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#updatePromotion(com.fb.commons.promotion.to.PromotionTO)
	 */
	@Override
	@Transactional
	public void updatePromotion(PromotionTO promotion) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getPromotionsForOrder(java.lang.Integer)
	 */
	@Override
	@Transactional
	public List<PromotionTO> getPromotionsForOrder(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getApplicablePromotionsForOrder(com.fb.commons.promotion.to.PromotionOrderTO)
	 */
	@Override
	@Transactional
	public List<PromotionTO> getApplicablePromotionsForOrder(
			PromotionOrderTO promotionOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getActivePromotions()
	 */
	@Override
	@Transactional
	public List<PromotionTO> getActivePromotions() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#applyPromotionOnOrder(com.fb.commons.promotion.to.PromotionOrderTO)
	 */
	@Override
	@Transactional
	public PromotionOrderTO applyPromotionOnOrder(
			PromotionOrderTO promotionOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPromotionDao(PromotionDao promotionsDao) {
		this.promotionDao = promotionsDao;
	}

}
