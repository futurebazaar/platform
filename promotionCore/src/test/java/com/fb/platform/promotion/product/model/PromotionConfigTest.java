/**
 * 
 */
package com.fb.platform.promotion.product.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.promotion.dao.PromotionDao;
import com.fb.platform.promotion.model.Promotion;
import com.fb.platform.promotion.product.model.promotion.AutoPromotion;
import com.fb.platform.promotion.product.util.ConditionResultProcessor;
import com.fb.platform.promotion.product.util.ConditionResultProcessorFactory;
import com.fb.platform.promotion.product.util.ProdConditionValueResult;
import com.fb.platform.promotion.to.OrderItem;
import com.fb.platform.promotion.to.OrderRequest;
import com.fb.platform.promotion.to.Product;

/**
 * @author vinayak
 *
 */
public class PromotionConfigTest extends BaseTestCase {

	@Autowired
	private PromotionDao promotionDao;

	@Test
	public void verifyProdCondValueResult() {
		Promotion promotion = promotionDao.load(8000);

		assertNotNull(promotion);
		assertTrue(promotion instanceof AutoPromotion);
		AutoPromotion autoPromotion = (AutoPromotion)promotion;
		assertNotNull(autoPromotion.getPromotionConfig());

		PromotionConfig config = autoPromotion.getPromotionConfig();
		assertNotNull(config.getModules());
		assertEquals(8000, config.getPromotionId());

		assertEquals(1, config.getModules().size());
		ConditionResultProcessor conditionResultProcessor = ConditionResultProcessorFactory.get(config.getModules().get(0));
		assertNotNull(conditionResultProcessor);
		assertTrue(conditionResultProcessor instanceof ProdConditionValueResult);
	}

	@Test
	public void apply() {
		AutoPromotion autoPromotion = (AutoPromotion) promotionDao.load(8000);

		assertNotNull(autoPromotion.getPromotionConfig());

		PromotionConfig config = autoPromotion.getPromotionConfig();

		boolean applied = config.apply(getSampleOrderRequest());
		assertTrue(applied);
	}

	@Test
	public void applyWithReminder() {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(100);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(3);
		oItem1.setProduct(p1);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		orderReq1.setOrderItems(oList1);
		orderReq1.setClientId(5);

		AutoPromotion autoPromotion = (AutoPromotion) promotionDao.load(8000);

		assertNotNull(autoPromotion.getPromotionConfig());

		PromotionConfig config = autoPromotion.getPromotionConfig();

		boolean applied = config.apply(orderReq1);
		assertTrue(applied);
		for (OrderItem orderItem : orderReq1.getOrderItems()) {
			assertEquals(new BigDecimal("666.33"), orderItem.getProduct().getDiscountedPrice());
		}
	}

	private OrderRequest getSampleOrderRequest(){

		Product p1 = new Product();
		p1.setPrice(new BigDecimal(700));
		p1.setMrpPrice(new BigDecimal(1200));
		p1.setProductId(100);

		//Create OrderItems
		OrderItem oItem1 = new OrderItem();
		oItem1.setQuantity(2);
		oItem1.setProduct(p1);

		//Create OrderReq
		OrderRequest orderReq1 = new OrderRequest();
		orderReq1.setOrderId(1);
		List<OrderItem> oList1 = new ArrayList<OrderItem>();
		oList1.add(oItem1);
		orderReq1.setOrderItems(oList1);
		orderReq1.setClientId(5);

		return orderReq1;
	}

}
