package com.fb.platform.promotion.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Priority {
	VERY_LOW(1),
	LOW(2),
	AVERAGE(3),
	HIGH(4),
	VERY_HIGH(5);
	
	private static final Map<Integer,Priority> lookup 
    = new HashMap<Integer,Priority>();

static {
    for(Priority s : EnumSet.allOf(Priority.class))
         lookup.put(s.getCode(), s);
}

private int code;

private Priority(int code) {
    this.code = code;
}

public int getCode() { return code; }

public static Priority get(int code) { 
    return lookup.get(code); 
}
}
