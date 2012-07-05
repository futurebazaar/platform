package com.fb.platform.promotion.rule;

public enum RuleConfigDescriptorType {
	CSI,
	DECIMAL,
	PERCENT,
	CSKV; //Comma Separated Key Value Pairs
	
	@Override
	public String toString() {
		return this.name();
	}
}
