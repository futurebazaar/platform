/**
 * 
 */
package com.fb.api.promotion;

import java.util.List;

import com.fb.commons.promotion.to.PromotionOrderTO;
import com.fb.commons.promotion.to.PromotionTO;

/**
 * @author Keith Fernandez
 *
 */
public interface PromotionManager {

	public PromotionTO getPromotion(Integer promotionId);

	public void deletePromotion(Integer promotionId);

	public void createPromotion(PromotionTO promotion);

	public void updatePromotion(PromotionTO promotion);

	public List<PromotionTO> getPromotionsForOrder(Integer orderId);

	public List<PromotionTO> getApplicablePromotionsForOrder(PromotionOrderTO promotionOrder);

	public List<PromotionTO> getActivePromotions();

	public PromotionOrderTO applyPromotionOnOrder(PromotionOrderTO promotionOrder);

}
