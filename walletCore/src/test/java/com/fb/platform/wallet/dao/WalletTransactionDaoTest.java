package com.fb.platform.wallet.dao;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.model.WalletSubTransaction;
import com.fb.platform.wallet.model.WalletTransaction;

public class WalletTransactionDaoTest extends BaseTestCase {
	
	@Autowired
	private WalletDao walletDao;
	
	@Autowired
	private WalletTransactionDao walletTransactionDao;
	
	
	@Test
	public void newCreditTransaction(){
		Wallet wallet = walletDao.load(6, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal("100.00")), SubWalletType.GIFT, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());		
	}
	
	@Test
	public void newCompleteTransactionTest(){
		Wallet wallet = walletDao.load(2, -4,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal("100.00")), SubWalletType.GIFT, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		WalletTransaction walletTransaction2 = wallet2.credit(new Money(new BigDecimal("200.00")), SubWalletType.CASH, 1, 0, null);
		WalletTransaction walletTransaction3 = wallet2.credit(new Money(new BigDecimal("200.00")), SubWalletType.REFUND, 0, 1, null);
		assertNotNull(walletTransaction2);
		assertNotNull(walletTransaction3);
		assertEquals(new Money(new BigDecimal("500.00")) , wallet2.getTotalAmount());
		Wallet wallet3 = walletDao.update(wallet2);
		String trnasString = walletTransactionDao.insertTransaction(walletTransaction2);
		String tranString2 = walletTransactionDao.insertTransaction(walletTransaction3);
		assertNotNull(trnasString);
		assertNotNull(tranString2);
		
		WalletTransaction debitWalletTransaction = wallet3.debit(new Money(new BigDecimal("250.00")), 123444);
		assertEquals(new Money(new BigDecimal("250.00")) , wallet3.getTotalAmount());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getGiftSubWallet());
		assertEquals(new Money(new BigDecimal("50.00")) , wallet3.getCashSubWallet());
		assertEquals(new Money(new BigDecimal("200.00")) , wallet3.getRefundSubWallet());
		
		String debitTranString = walletTransactionDao.insertTransaction(debitWalletTransaction);
		Wallet wallet4 = walletDao.update(wallet3);
		
		assertNotNull(debitTranString);
		assertNotNull(wallet4);
		
		WalletTransaction walletRefundTransaction = walletTransactionDao.refundTransactionByRefundId(wallet4.getId(), 1);
		assertNotNull(walletRefundTransaction);
		assertEquals(new BigDecimal("200.00"), walletRefundTransaction.getAmount().getAmount());
		
		WalletTransaction walletRefundTran = wallet4.refund(new Money(new BigDecimal("200.00")),1);
		String tranid = walletTransactionDao.insertTransaction(walletRefundTran);
		Wallet walletRes = walletDao.update(wallet4);
		assertNotNull(tranid);
		assertNotNull(walletRes);
		assertEquals(new BigDecimal("50.00"), walletRes.getTotalAmount().getAmount());
		
		List<WalletTransaction> walletTransactions = walletTransactionDao.walletHistory(walletRes, DateTime.now().minusDays(20), DateTime.now());
		for (WalletTransaction wallTransaction : walletTransactions){
			assertNotNull(wallTransaction);
			assertNotNull(wallTransaction.getAmount());
			for (WalletSubTransaction walletSubTransaction : wallTransaction.getWalletSubTransaction()){
				assertNotNull(walletSubTransaction);
				assertNotNull(walletSubTransaction.getAmount());
				assertNotNull(walletSubTransaction.toString());
				assertEquals(true, walletSubTransaction.equals(walletSubTransaction));
			}
		}
		
	}
	
	@Test
	public void invalidRefundIdTransaction(){
		try{
			WalletTransaction walletRefundTransaction = walletTransactionDao.refundTransactionByRefundId(33, 76878);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WorngRefundIdException",e.getClass().getCanonicalName());
		}
		
	}
	@Test
	public void wrongWalletRefundIdTransaction(){
		Wallet wallet = walletDao.load(2, -4,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal("200.00")), SubWalletType.REFUND, 0, 1, null);
		Wallet wallet3 = walletDao.update(wallet);
		String trnasString = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(trnasString);
		
		WalletTransaction debitWalletTransaction = wallet3.debit(new Money(new BigDecimal("200.00")), 123444);
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getTotalAmount());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getGiftSubWallet());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getCashSubWallet());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getRefundSubWallet());
		
		String debitTranString = walletTransactionDao.insertTransaction(debitWalletTransaction);
		Wallet wallet4 = walletDao.update(wallet3);
		
		assertNotNull(debitTranString);
		assertNotNull(wallet4);
		
		try{
			WalletTransaction walletRefundTransaction = walletTransactionDao.refundTransactionByRefundId(878778, 1);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletRefundMismatchException",e.getClass().getCanonicalName());
		}
		
	}
	@Test
	public void walletHistory(){
		Wallet wallet = walletDao.load(6, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal(100.00)), SubWalletType.GIFT, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		
		List<WalletTransaction> walletTransactions = walletTransactionDao.walletHistory(wallet2, null, null);
		WalletTransaction wallTransaction = walletTransactionDao.transactionById(wallet2.getId(), transactionId);
		assertNotNull(wallTransaction);
		assertNotNull(walletTransactions);
		for(WalletTransaction walletTransaction2 :walletTransactions){
			assertNotNull(walletTransaction2);
			assertNotNull(walletTransaction2.getAmount());
			for (WalletSubTransaction walletSubTransaction : walletTransaction2.getWalletSubTransaction()){
				assertNotNull(walletSubTransaction);
				assertNotNull(walletSubTransaction.getAmount());
			}
		}
		
	}
	
	@Test
	public void walletInvalidTransactionId(){
		Wallet wallet = walletDao.load(6, -5,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal(100.00)), SubWalletType.GIFT, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		try{
			WalletTransaction wallTransaction = walletTransactionDao.transactionById(wallet2.getId(), "asdfnasdf");
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.InvalidTransactionIdException",e.getClass().getCanonicalName());
		}
		
		
	}
	
	@Test
	public void reverseTransactionTest(){
		Wallet wallet = walletDao.load(6, -6,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal("100.00")), SubWalletType.GIFT, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		WalletTransaction walletTransaction2 = wallet2.credit(new Money(new BigDecimal("200.00")), SubWalletType.CASH, 1, 0, null);
		WalletTransaction walletTransaction3 = wallet2.credit(new Money(new BigDecimal("200.00")), SubWalletType.REFUND, 0, 1, null);
		assertNotNull(walletTransaction2);
		assertNotNull(walletTransaction3);
		assertEquals(new Money(new BigDecimal("500.00")) , wallet2.getTotalAmount());
		Wallet wallet3 = walletDao.update(wallet2);
		String trnasString = walletTransactionDao.insertTransaction(walletTransaction2);
		String tranString2 = walletTransactionDao.insertTransaction(walletTransaction3);
		assertNotNull(trnasString);
		assertNotNull(tranString2);
		
		WalletTransaction debitWalletTransaction = wallet3.debit(new Money(new BigDecimal("250.00")), 123444);
		assertEquals(new Money(new BigDecimal("250.00")) , wallet3.getTotalAmount());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getGiftSubWallet());
		assertEquals(new Money(new BigDecimal("50.00")) , wallet3.getCashSubWallet());
		assertEquals(new Money(new BigDecimal("200.00")) , wallet3.getRefundSubWallet());
		
		String debitTranString = walletTransactionDao.insertTransaction(debitWalletTransaction);
		Wallet wallet4 = walletDao.update(wallet3);
		
		assertNotNull(debitTranString);
		assertNotNull(wallet4);
		
		WalletTransaction debitWalletTranFetch = walletTransactionDao.transactionById(wallet4.getId(), debitTranString);
		WalletTransaction walletTransactionNew = wallet4.reverseTransaction(debitWalletTranFetch,debitWalletTranFetch.getAmount());
		String transactionReversal = walletTransactionDao.insertTransaction(walletTransactionNew);
		Wallet wallet5  = walletDao.update(wallet4);
		assertNotNull(transactionReversal);
		assertNotNull(wallet5);
		assertEquals(new Money(new BigDecimal("500.00")) , wallet5.getTotalAmount());
		assertEquals(new Money(new BigDecimal("100.00")) , wallet5.getGiftSubWallet());
		assertEquals(new Money(new BigDecimal("200.00")) , wallet5.getRefundSubWallet());
		assertEquals(new Money(new BigDecimal("200.00")) , wallet5.getCashSubWallet());		
	}
	
	@Test
	public void reverseTransactionCompleteTest(){
		Wallet wallet = walletDao.load(6, -6,true);
		assertNotNull(wallet);
		assertNotNull(wallet.getId());
		
		WalletTransaction walletTransaction = wallet.credit(new Money(new BigDecimal("100.00")), SubWalletType.GIFT, 0, 0, "EGV");
		assertNotNull(walletTransaction);
		String transactionId = walletTransactionDao.insertTransaction(walletTransaction);
		assertNotNull(transactionId);
		Wallet wallet2 = walletDao.update(wallet);
		assertNotNull(wallet2);
		assertNotNull(wallet2.getId());
		WalletTransaction walletTransaction2 = wallet2.credit(new Money(new BigDecimal("200.00")), SubWalletType.CASH, 1, 0, null);
		WalletTransaction walletTransaction3 = wallet2.credit(new Money(new BigDecimal("200.00")), SubWalletType.REFUND, 0, 1, null);
		assertNotNull(walletTransaction2);
		assertNotNull(walletTransaction3);
		assertEquals(new Money(new BigDecimal("500.00")) , wallet2.getTotalAmount());
		Wallet wallet3 = walletDao.update(wallet2);
		String trnasString = walletTransactionDao.insertTransaction(walletTransaction2);
		String tranString2 = walletTransactionDao.insertTransaction(walletTransaction3);
		assertNotNull(trnasString);
		assertNotNull(tranString2);
		
		WalletTransaction debitWalletTransaction = wallet3.debit(new Money(new BigDecimal("500.00")), 123444);
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getTotalAmount());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getGiftSubWallet());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getCashSubWallet());
		assertEquals(new Money(new BigDecimal("0.00")) , wallet3.getRefundSubWallet());
		
		String debitTranString = walletTransactionDao.insertTransaction(debitWalletTransaction);
		Wallet wallet4 = walletDao.update(wallet3);
		
		assertNotNull(debitTranString);
		assertNotNull(wallet4);
		
		WalletTransaction debitWalletTranFetch = walletTransactionDao.transactionById(wallet4.getId(), debitTranString);
		WalletTransaction walletTransactionNew = wallet4.reverseTransaction(debitWalletTranFetch,debitWalletTranFetch.getAmount());
		String transactionReversal = walletTransactionDao.insertTransaction(walletTransactionNew);
		Wallet wallet5  = walletDao.update(wallet4);
		assertNotNull(transactionReversal);
		assertNotNull(wallet5);
		assertEquals(new Money(new BigDecimal("500.00")) , wallet5.getTotalAmount());
		assertEquals(new Money(new BigDecimal("100.00")) , wallet5.getGiftSubWallet());
		assertEquals(new Money(new BigDecimal("200.00")) , wallet5.getRefundSubWallet());
		assertEquals(new Money(new BigDecimal("200.00")) , wallet5.getCashSubWallet());		
	}
}
