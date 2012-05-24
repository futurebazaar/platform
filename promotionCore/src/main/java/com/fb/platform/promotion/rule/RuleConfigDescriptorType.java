package com.fb.platform.promotion.rule;

public enum RuleConfigDescriptorType {
	CSI,
	DECIMAL,
	PERCENT;
	
	@Override
	public String toString() {
		return this.name();
	}
}
