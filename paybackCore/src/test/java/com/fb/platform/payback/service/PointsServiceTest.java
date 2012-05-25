package com.fb.platform.payback.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.platform.payback.dao.PointsDao;
import com.fb.platform.payback.model.PointsHeader;
import com.fb.platform.payback.to.BurnActionCodesEnum;

public class PointsServiceTest extends BaseTestCase{
	
	@Autowired
	private PointsService pointsService;
	
	@Autowired
	private PointsDao pointsDao;
	
	private PointsHeader pointsHeader = new PointsHeader();
	
	@Test
	public void mailBurnDataTest(){
		assertSame(pointsService.mailBurnData(BurnActionCodesEnum.BURN_REVERSAL, "90012970"), "");
		
		pointsHeader.setPartnerMerchantId("90012970");
		pointsHeader.setPartnerTerminalId("6241718");
		pointsHeader.setTxnClassificationCode("CASH_CASH");
		pointsHeader.setTxnPaymentType("OTHERS");
		pointsHeader.setTxnActionCode("BURN_REVERSAL");
		BigDecimal txnPoints = new BigDecimal(30);
		int ROUND = BigDecimal.ROUND_HALF_DOWN;
		pointsHeader.setTxnPoints(txnPoints.setScale(0, ROUND).intValue());
		
		BigDecimal amount = new BigDecimal(1000);
		pointsHeader.setTxnValue(amount.setScale(0, ROUND).intValue());
		pointsHeader.setReferenceId("1234");
		pointsHeader.setLoyaltyCard("1234567812345678");
		pointsHeader.setReason("TEST ORDER");
		pointsHeader.setSettlementDate(DateTime.now().minusDays(1));
		pointsHeader.setTxnDate(DateTime.now());
		pointsHeader.setOrderId(1);
		
		long headerId = pointsDao.insertPointsHeaderData(pointsHeader);
		
		assertFalse(pointsService.mailBurnData(BurnActionCodesEnum.BURN_REVERSAL, "90012970").isEmpty());
	}

}
