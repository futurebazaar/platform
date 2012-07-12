package com.fb.platform.wallet.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import com.fb.commons.test.BaseTestCase;
import com.fb.commons.to.Money;
import com.fb.platform.wallet.model.SubWalletType;
import com.fb.platform.wallet.model.Wallet;
import com.fb.platform.wallet.to.WalletTransaction;

@SuppressWarnings("unused")
public class WalletServiceTest extends BaseTestCase {

	@Autowired
	private WalletService walletService;
	
	@Test
	public void basicWalletload(){
		Wallet wallet = walletService.load(6, -6);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());
	}
	
	@Test
	public void basicWalletloadByWalletId(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());
		
		Wallet walletById = walletService.load(wallet.getId());
		assertNotNull(walletById);
		assertEquals(wallet,walletById);		
	}
	
	@Test
	public void wrongWalletIdLoad(){
		try{
			Wallet wallet = walletService.load(100000L);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}	
	}
	
	@Test
	public void walletCreditTest(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransaction = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		assertNotNull(walletTransaction);
		assertNotNull(walletTransaction.getTransactionId());
		assertNotNull(walletTransaction.getWallet());
		assertEquals(wallet.getId(), walletTransaction.getWallet().getId());
		assertEquals(new BigDecimal("200.00"), walletTransaction.getWallet().getTotalAmount().getAmount());
	}
	
	@Test
	public void wrongWalletIdCredit(){
		try{
			WalletTransaction walletTransaction = walletService.credit(100000L, new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}	
	}
	
	@Test
	public void insufficientFundWalletDebit(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransaction = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		assertNotNull(walletTransaction);
		assertNotNull(walletTransaction.getTransactionId());
		assertNotNull(walletTransaction.getWallet());
		assertEquals(wallet.getId(), walletTransaction.getWallet().getId());
		assertEquals(new BigDecimal("200.00"), walletTransaction.getWallet().getTotalAmount().getAmount());
		try{	
			WalletTransaction walletTransactionDebit = walletService.debit(6,-5,new Money(new BigDecimal("500.00")),32);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.InSufficientFundsException",e.getClass().getCanonicalName());
		}	
	}
	@Test
	public void walletNotFoundWalletDebit(){
		try{
			WalletTransaction walletTransaction = walletService.debit(1,-5,new Money(new BigDecimal("100.00")),32);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}	
	}
	
	@Test
	public void walletDebitTest(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransaction = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		assertNotNull(walletTransaction);
		assertNotNull(walletTransaction.getTransactionId());
		assertNotNull(walletTransaction.getWallet());
		assertEquals(wallet.getId(), walletTransaction.getWallet().getId());
		assertEquals(new BigDecimal("200.00"), walletTransaction.getWallet().getTotalAmount().getAmount());
		
		WalletTransaction walletTransactionDebit = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),32);
		assertNotNull(walletTransactionDebit);
		assertNotNull(walletTransactionDebit.getTransactionId());
		assertNotNull(walletTransactionDebit.getWallet());
		assertEquals(wallet.getId(), walletTransactionDebit.getWallet().getId());
		assertEquals(new BigDecimal("100.00"), walletTransactionDebit.getWallet().getTotalAmount().getAmount());
		
	}
	
	@Test
	public void walletDebitTestPartialRefunds(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransaction = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 1, null,null);
		assertNotNull(walletTransaction);
		assertNotNull(walletTransaction.getTransactionId());
		assertNotNull(walletTransaction.getWallet());
		assertEquals(wallet.getId(), walletTransaction.getWallet().getId());
		assertEquals(new BigDecimal("200.00"), walletTransaction.getWallet().getTotalAmount().getAmount());
		WalletTransaction walletTransaction2 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 1, null,null);
		assertNotNull(walletTransaction2);
		assertNotNull(walletTransaction2.getTransactionId());
		assertNotNull(walletTransaction2.getWallet());
		assertEquals(wallet.getId(), walletTransaction2.getWallet().getId());
		assertEquals(new BigDecimal("400.00"), walletTransaction2.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("400.00"), walletTransaction2.getWallet().getRefundableAmount().getAmount());
		
		WalletTransaction walletTransactionDebit = walletService.debit(6,-5,new Money(new BigDecimal("300.00")),32);
		assertNotNull(walletTransactionDebit);
		assertNotNull(walletTransactionDebit.getTransactionId());
		assertNotNull(walletTransactionDebit.getWallet());
		assertEquals(wallet.getId(), walletTransactionDebit.getWallet().getId());
		assertEquals(new BigDecimal("100.00"), walletTransactionDebit.getWallet().getTotalAmount().getAmount());
		
	}
	
	@Test 
	public void walletHistoryTest(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr1 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		WalletTransaction walletTransactionCr2 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV",DateTime.now().plusYears(3));
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 3, null,null);
		WalletTransaction walletTransactionDebit1 = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),30);
		WalletTransaction walletTransactionDebit2 = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),36);
		assertNotNull(walletTransactionCr1);
		assertNotNull(walletTransactionCr2);
		assertNotNull(walletTransactionCr3);
		assertNotNull(walletTransactionDebit1);
		assertNotNull(walletTransactionDebit2);
		
		List<WalletTransaction> walletTransactions = walletService.walletHistory(wallet.getId(), null, null, null);
		assertNotNull(walletTransactions);
		for (WalletTransaction walletTransaction : walletTransactions){
			assertNotNull(walletTransaction);
		}
		
	}
	@Test
	public void wrongWalletIdHistory(){
		try{
			walletService.walletHistory(100000L, null, null, null);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}	
	}
	@Test 
	public void walletRefundTest(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 3, null,null);
		assertNotNull(walletTransactionCr3);
		
		WalletTransaction walletTransactionrefund = walletService.refund(6, -5, new Money(new BigDecimal("200.00")), 3, false);
		assertNotNull(walletTransactionrefund);
		
		assertNotNull(walletTransactionrefund);
		assertNotNull(walletTransactionrefund.getWallet());
		assertEquals(wallet.getId(), walletTransactionrefund.getWallet().getId());
		assertEquals(new Money(new BigDecimal("0.00")) ,walletTransactionrefund.getWallet().getTotalAmount() );
		assertEquals(new Money(new BigDecimal("0.00")) ,walletTransactionrefund.getWallet().getRefundSubWallet() );
	}
	
	@Test 
	public void walletAlreadyRefundTest(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 3, null,null);
		assertNotNull(walletTransactionCr3);
		
		WalletTransaction walletTransactionrefund = walletService.refund(6, -5, new Money(new BigDecimal("200.00")), 3, false);
		assertNotNull(walletTransactionrefund);
		
		assertNotNull(walletTransactionrefund);
		assertNotNull(walletTransactionrefund.getWallet());
		assertEquals(wallet.getId(), walletTransactionrefund.getWallet().getId());
		assertEquals(new Money(new BigDecimal("0.00")) ,walletTransactionrefund.getWallet().getTotalAmount() );
		assertEquals(new Money(new BigDecimal("0.00")) ,walletTransactionrefund.getWallet().getRefundSubWallet() );
		try{
			WalletTransaction walletTransactionrefundAlready = walletService.refund(6, -5, new Money(new BigDecimal("200.00")), 3, false);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.AlreadyRefundedException",e.getClass().getCanonicalName());
		}
		
	}
	
	@Test 
	public void walletWrongRefundAmountTest(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 3, null,null);
		assertNotNull(walletTransactionCr3);
		try{
			WalletTransaction walletTransactionrefund = walletService.refund(6, -5, new Money(new BigDecimal("100.00")), 3, false);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.InSufficientFundsException",e.getClass().getCanonicalName());
		}		
	}
	@Test 
	public void walletNotFountRefundTest(){
		try{
			WalletTransaction walletTransactionrefund = walletService.refund(7, -5, new Money(new BigDecimal("100.00")), 3, false);
		}catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}		
	}

	@Test 
	public void walletReverseTransaction(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr1 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		WalletTransaction walletTransactionCr2 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV",DateTime.now().plusYears(5));
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 3, null,null);
		WalletTransaction walletTransactionDebit1 = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),30);
		WalletTransaction walletTransactionDebit2 = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),36);
		assertNotNull(walletTransactionCr1);
		assertNotNull(walletTransactionCr2);
		assertNotNull(walletTransactionCr3);
		assertNotNull(walletTransactionDebit1);
		assertNotNull(walletTransactionDebit2);
		WalletTransaction walletTransactionRev = walletService.reverseTransaction(6, -5, walletTransactionDebit2.getTransactionId(),null);
		assertNotNull(walletTransactionRev);
		assertEquals(new BigDecimal("500.00"), walletTransactionRev.getWallet().getTotalAmount().getAmount());		
	}
	
	@Test 
	public void walletReverseTransactionPartial(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr1 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.CASH.toString(), 2, 0, null,null);
		WalletTransaction walletTransactionCr2 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV",DateTime.now().plusYears(5));
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.REFUND.toString(), 0, 3, null,null);
		WalletTransaction walletTransactionDebit1 = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),30);
		WalletTransaction walletTransactionDebit2 = walletService.debit(6,-5,new Money(new BigDecimal("400.00")),36);
		assertNotNull(walletTransactionCr1);
		assertNotNull(walletTransactionCr2);
		assertNotNull(walletTransactionCr3);
		assertNotNull(walletTransactionDebit1);
		assertNotNull(walletTransactionDebit2);
		WalletTransaction walletTransactionRev = walletService.reverseTransaction(6, -5, walletTransactionDebit2.getTransactionId(),new Money(new BigDecimal("200.00")));
		assertNotNull(walletTransactionRev);
		assertEquals(new BigDecimal("300.00"), walletTransactionRev.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("200.00"), walletTransactionRev.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("100.00"), walletTransactionRev.getWallet().getCashSubWallet().getAmount());
		
		WalletTransaction walletTransactionRevAgain = walletService.reverseTransaction(6, -5, walletTransactionDebit2.getTransactionId(),new Money(new BigDecimal("150.00")));
		assertNotNull(walletTransactionRevAgain);
		assertEquals(new BigDecimal("450.00"), walletTransactionRevAgain.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("200.00"), walletTransactionRevAgain.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("200.00"), walletTransactionRevAgain.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("50.00"), walletTransactionRevAgain.getWallet().getGiftSubWallet().getAmount());
		
	}
	
	@Test 
	public void walletReverseTransactionPartialGifts(){
		Wallet wallet = walletService.load(6, -5);
		assertNotNull(wallet);
		assertEquals(new BigDecimal("0.00"), wallet.getTotalAmount().getAmount());		
		WalletTransaction walletTransactionCr1 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV1",DateTime.now().plusMonths(1));
		WalletTransaction walletTransactionCr2 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV2",DateTime.now().plusDays(10));
		WalletTransaction walletTransactionCr4 = walletService.credit(wallet.getId(), new Money(new BigDecimal("300.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV3",DateTime.now().plusYears(1));
		WalletTransaction walletTransactionCr3 = walletService.credit(wallet.getId(), new Money(new BigDecimal("200.00")), SubWalletType.GIFT.toString(), 0, 0, "EGV4",DateTime.now().plusHours(10));
		WalletTransaction walletTransactionDebit1 = walletService.debit(6,-5,new Money(new BigDecimal("100.00")),30);
		WalletTransaction walletTransactionDebit2 = walletService.debit(6,-5,new Money(new BigDecimal("400.00")),36);
		assertNotNull(walletTransactionCr1);
		assertNotNull(walletTransactionCr2);
		assertNotNull(walletTransactionCr3);
		assertNotNull(walletTransactionDebit1);
		assertNotNull(walletTransactionDebit2);
		assertEquals(new BigDecimal("900.00"), walletTransactionCr3.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionCr3.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionCr3.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("900.00"), walletTransactionCr3.getWallet().getGiftSubWallet().getAmount());
		
		assertEquals(new BigDecimal("800.00"), walletTransactionDebit1.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionDebit1.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionDebit1.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("800.00"), walletTransactionDebit1.getWallet().getGiftSubWallet().getAmount());
		
		
		assertEquals(new BigDecimal("400.00"), walletTransactionDebit2.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionDebit2.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionDebit2.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("400.00"), walletTransactionDebit2.getWallet().getGiftSubWallet().getAmount());
		
		WalletTransaction walletTransactionRev = walletService.reverseTransaction(6, -5, walletTransactionDebit2.getTransactionId(),new Money(new BigDecimal("150.00")));
		assertNotNull(walletTransactionRev);
		assertEquals(new BigDecimal("550.00"), walletTransactionRev.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionRev.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionRev.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("550.00"), walletTransactionRev.getWallet().getGiftSubWallet().getAmount());
		
		WalletTransaction walletTransactionRevAgain = walletService.reverseTransaction(6, -5, walletTransactionDebit2.getTransactionId(),new Money(new BigDecimal("150.00")));
		assertNotNull(walletTransactionRevAgain);
		assertEquals(new BigDecimal("700.00"), walletTransactionRevAgain.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionRevAgain.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionRevAgain.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("700.00"), walletTransactionRevAgain.getWallet().getGiftSubWallet().getAmount());
		
		WalletTransaction walletTransactionDebit3 = walletService.debit(6,-5,new Money(new BigDecimal("500.00")),38);
		assertNotNull(walletTransactionDebit3);
		assertEquals(new BigDecimal("200.00"), walletTransactionDebit3.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionDebit3.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionDebit3.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("200.00"), walletTransactionDebit3.getWallet().getGiftSubWallet().getAmount());
		
		WalletTransaction walletTransactionRevAgain1 = walletService.reverseTransaction(6, -5, walletTransactionDebit3.getTransactionId(),new Money(new BigDecimal("500.00")));
		assertNotNull(walletTransactionRevAgain1);
		assertEquals(new BigDecimal("700.00"), walletTransactionRevAgain1.getWallet().getTotalAmount().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionRevAgain1.getWallet().getRefundSubWallet().getAmount());
		assertEquals(new BigDecimal("0.00"), walletTransactionRevAgain1.getWallet().getCashSubWallet().getAmount());
		assertEquals(new BigDecimal("700.00"), walletTransactionRevAgain1.getWallet().getGiftSubWallet().getAmount());
		
		
	}
	
	@Test 
	public void walletNotFoundTransactionRevTest(){
		try{
			WalletTransaction walletTransactionRev = walletService.reverseTransaction(6,-5, "adfsadf",null);
		} catch (Exception e) {
			assertEquals("com.fb.platform.wallet.service.exception.WalletNotFoundException",e.getClass().getCanonicalName());
		}			
	}
}