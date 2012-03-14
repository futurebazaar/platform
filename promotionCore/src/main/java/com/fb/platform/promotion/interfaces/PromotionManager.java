/**
 * 
 */
package com.fb.platform.promotion.interfaces;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fb.commons.PlatformException;
import com.fb.platform.promotion.to.*;

/**
 * @author Keith Fernandez
 *
 */
@Transactional
public interface PromotionManager {

	/**
     * Authenticate a session token. Token is passed as a parameter
     * 
     * @param token
     * @return
     * @throws RemoteException
     * @throws PlatformException
     */
	@Transactional(propagation=Propagation.SUPPORTS)
	public PromotionTO getPromotion(Integer promotionId) throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public void deletePromotion(Integer promotionId) throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public void createPromotion(PromotionTO promotion) throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public void updatePromotion(PromotionTO promotion) throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public List<PromotionTO> getPromotionsForOrder(Integer orderId) throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public List<PromotionTO> getApplicablePromotionsForOrder(PromotionOrderTO promotionOrder) throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public List<PromotionTO> getActivePromotions() throws PlatformException;

	@Transactional(propagation=Propagation.SUPPORTS)
	public PromotionOrderTO applyPromotionOnOrder(PromotionOrderTO promotionOrder) throws PlatformException;

}
