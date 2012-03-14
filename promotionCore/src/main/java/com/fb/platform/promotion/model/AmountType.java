package com.fb.platform.promotion.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AmountType {
	ON_MRP(0),
	ON_OFFER_PRICE(1),
	ON_NET_AMOUNT(2);
	
	 private static final Map<Integer,AmountType> lookup 
     = new HashMap<Integer,AmountType>();

static {
     for(AmountType s : EnumSet.allOf(AmountType.class))
          lookup.put(s.getCode(), s);
}

private int code;

private AmountType(int code) {
     this.code = code;
}

public int getCode() { return code; }

public static AmountType get(int code) { 
     return lookup.get(code); 
}
	
}
