package com.fb.platform.test;

import java.math.BigDecimal;

import com.fb.commons.to.Money;

public class MoneyTest {

	
	public static void main(String[] args) {
		Money money = new Money(new BigDecimal("10.5"));
		System.out.println(money.getAmount().toPlainString());
	}
}
