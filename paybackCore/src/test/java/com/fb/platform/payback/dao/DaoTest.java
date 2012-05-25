package com.fb.platform.payback.dao;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fb.commons.PlatformException;
import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.model.PointsItems;
import com.fb.platform.payback.rule.BurnPointsRuleEnum;
import com.fb.platform.payback.rule.EarnPointsRuleEnum;
import com.fb.platform.payback.to.OrderItemRequest;

public class DaoTest extends BaseTestCase{

	private PointsHeader pointsHeader = new PointsHeader();
	
	@Autowired
	private PointsDao pointsDao;
	
	@Autowired
	private PointsRuleDao pointsRuleDao;
	
	@Autowired
	private ListDao listDao;
	
	
	@Test
	public void insertPoints(){
		pointsHeader.setPartnerMerchantId("90012970");
		pointsHeader.setPartnerTerminalId("6241718");
		pointsHeader.setTxnClassificationCode("CASH_CASH");
		pointsHeader.setTxnPaymentType("OTHERS");
		pointsHeader.setTxnActionCode("PREALLOC_EARN");
		BigDecimal txnPoints = new BigDecimal(30);
		int ROUND = BigDecimal.ROUND_HALF_DOWN;
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		
		BigDecimal amount = new BigDecimal(1000);
		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
		pointsHeader.setReferenceId("1234");
		pointsHeader.setLoyaltyCard("1234567812345678");
		pointsHeader.setReason("TEST ORDER");
		pointsHeader.setSettlementDate(DateTime.now());
		pointsHeader.setTxnDate(DateTime.now());
		pointsHeader.setOrderId(1);
		
		long headerId = pointsDao.insertPointsHeaderData(pointsHeader);
		
		assertNotNull(headerId);
		
		pointsHeader.setTxnClassificationCode("OTHERS");
		pointsHeader.setTxnPaymentType("NO_PAYMENT");
		pointsHeader.setTxnActionCode("EARN_REVERSAL");
		long headerId1 = pointsDao.insertPointsHeaderData(pointsHeader);
		assertNotNull(headerId1);
	}
	
	@Test
	public void getHeroDeal(){
		assertNotNull(listDao.getHeroDealSellerRateChart(new DateTime(2012, 05, 24, 10, 0, 0)));
	}
	
	@Test
	public void loadRuleTest(){
		
		for (EarnPointsRuleEnum ruleName: EarnPointsRuleEnum.values()){
			assertNotNull(pointsRuleDao.loadEarnRule(ruleName));
		}
		for(BurnPointsRuleEnum ruleName: BurnPointsRuleEnum.values()){
			assertNotNull(pointsRuleDao.loadBurnRule(ruleName));
		}
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getNoHeaderDetailsTest(){
		pointsDao.getHeaderDetails(1, "EARN_REVERASAL", "BONUS_POINTS");
		
	}
	
	@Test
	public void getHeaderDetailsTest(){
		pointsHeader.setPartnerMerchantId("90012970");
		pointsHeader.setPartnerTerminalId("6241718");
		pointsHeader.setTxnClassificationCode("CASH_CASH");
		pointsHeader.setTxnPaymentType("OTHERS");
		pointsHeader.setTxnActionCode("PREALLOC_EARN");
		BigDecimal txnPoints = new BigDecimal(30);
		int ROUND = BigDecimal.ROUND_HALF_DOWN;
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		
		BigDecimal amount = new BigDecimal(1000);
		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
		pointsHeader.setReferenceId("1234");
		pointsHeader.setLoyaltyCard("1234567812345678");
		pointsHeader.setReason("TEST ORDER");
		pointsHeader.setSettlementDate(DateTime.now());
		pointsHeader.setTxnDate(DateTime.now());
		pointsHeader.setOrderId(1);
		
		pointsDao.insertPointsHeaderData(pointsHeader);
		
		pointsDao.getHeaderDetails(1, "PREALLOC_EARN", "CASH_CASH");
	}
	
	@Test(expected=PlatformException.class)
	public void noStatusToUpdateTest(){
		pointsDao.updateStatus("NO_ACTION", "2012-05-23", "90012970");
	}
	
	@Test
	public void updateStatusTest(){
		pointsHeader.setPartnerMerchantId("90012970");
		pointsHeader.setPartnerTerminalId("6241718");
		pointsHeader.setTxnClassificationCode("CASH_CASH");
		pointsHeader.setTxnPaymentType("OTHERS");
		pointsHeader.setTxnActionCode("PREALLOC_EARN");
		BigDecimal txnPoints = new BigDecimal(30);
		int ROUND = BigDecimal.ROUND_HALF_DOWN;
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		
		BigDecimal amount = new BigDecimal(1000);
		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
		pointsHeader.setReferenceId("1234");
		pointsHeader.setLoyaltyCard("1234567812345678");
		pointsHeader.setReason("TEST ORDER");
		pointsHeader.setSettlementDate(new DateTime(2012, 05, 24, 0, 0, 0));
		pointsHeader.setTxnDate(new DateTime(2012, 05, 24, 0, 0, 0));
		pointsHeader.setOrderId(1);
		
		pointsDao.insertPointsHeaderData(pointsHeader);
		
		pointsDao.updateStatus("PREALLOC_EARN", "2012-05-24", "90012970");
	
	}
	
	@Test
	public void loadPointsItemsTest(){
		pointsHeader.setPartnerMerchantId("90012970");
		pointsHeader.setPartnerTerminalId("6241718");
		pointsHeader.setTxnClassificationCode("CASH_CASH");
		pointsHeader.setTxnPaymentType("OTHERS");
		pointsHeader.setTxnActionCode("PREALLOC_EARN");
		BigDecimal txnPoints = new BigDecimal(30);
		int ROUND = BigDecimal.ROUND_HALF_DOWN;
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		
		BigDecimal amount = new BigDecimal(1000);
		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
		pointsHeader.setReferenceId("1234");
		pointsHeader.setLoyaltyCard("1234567812345678");
		pointsHeader.setReason("TEST ORDER");
		pointsHeader.setSettlementDate(new DateTime(2012, 05, 24, 0, 0, 0));
		pointsHeader.setTxnDate(new DateTime(2012, 05, 24, 0, 0, 0));
		pointsHeader.setOrderId(1);
		
		long headerId = pointsDao.insertPointsHeaderData(pointsHeader);

		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setAmount(amount);
		itemRequest.setArticleId("1234");
		itemRequest.setCategoryId(1234);
		itemRequest.setDepartmentCode(1234);
		itemRequest.setDepartmentName("Pooh");
		itemRequest.setEarnRatio(new BigDecimal(0.03));
		itemRequest.setId(1);
		itemRequest.setQuantity(1);
		itemRequest.setSellerRateChartId(1);
		
		pointsDao.insertPointsItemsData(itemRequest, headerId, txnPoints.setScale(0, ROUND).intValue());
		Collection<PointsItems> pointsItems = pointsDao.loadPointsItemData(headerId);
		assertTrue("PointsItems size empty", pointsItems.size() > 0);
		
	}
	
}
