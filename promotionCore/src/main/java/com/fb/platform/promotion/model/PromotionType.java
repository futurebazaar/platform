package com.fb.platform.promotion.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PromotionType {
	
	ON_PRODUCT(1),
	ON_CATEGORY(2),
	ON_USER(3),
	ON_ORDER(4);


	private static final Map<Integer,PromotionType> lookup 
    = new HashMap<Integer,PromotionType>();

static {
    for(PromotionType s : EnumSet.allOf(PromotionType.class))
         lookup.put(s.getCode(), s);
}

private int code;

private PromotionType(int code) {
    this.code = code;
}

public int getCode() { return code; }

public static PromotionType get(int code) { 
    return lookup.get(code); 
}
	
}
