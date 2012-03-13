/**
 * 
 */
package com.fb.platform.promotion.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.to.*;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.interfaces.PromotionManager;
import com.fb.platform.promotion.model.Promotion;

/**
 * @author Keith Fernandez
 *
 */
public class PromotionManagerImpl implements PromotionManager {

	private static Log logger = LogFactory.getLog(PromotionManagerImpl.class);
	
	private PromotionDao promotionDao;

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getPromotion(java.lang.Integer)
	 */
	@Override
	public PromotionTO getPromotion(Integer promotionId) throws PlatformException {
		logger.info("In PromotionManagerImpl.getPromotion()");
		Promotion promotionBo = promotionDao.get(promotionId);
		PromotionTO pto = new PromotionTO();
		pto.setPromotionId(promotionBo.getPromotionId());
		return pto;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#deletePromotion(java.lang.Integer)
	 */
	@Override
	public void deletePromotion(Integer promotionId) throws PlatformException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#createPromotion(com.fb.commons.promotion.to.PromotionTO)
	 */
	@Override
	public void createPromotion(PromotionTO promotion) throws PlatformException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#updatePromotion(com.fb.commons.promotion.to.PromotionTO)
	 */
	@Override
	public void updatePromotion(PromotionTO promotion) throws PlatformException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getPromotionsForOrder(java.lang.Integer)
	 */
	@Override
	public List<PromotionTO> getPromotionsForOrder(Integer orderId) throws PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getApplicablePromotionsForOrder(com.fb.commons.promotion.to.PromotionOrderTO)
	 */
	@Override
	public List<PromotionTO> getApplicablePromotionsForOrder(
			PromotionOrderTO promotionOrder) throws PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#getActivePromotions()
	 */
	@Override
	public List<PromotionTO> getActivePromotions() throws PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fb.api.promotion.PromotionManager#applyPromotionOnOrder(com.fb.commons.promotion.to.PromotionOrderTO)
	 */
	@Override
	public PromotionOrderTO applyPromotionOnOrder(
			PromotionOrderTO promotionOrder) throws PlatformException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPromotionDao(PromotionDao promotionsDao) throws PlatformException {
		this.promotionDao = promotionsDao;
	}

}
