package com.fb.platform.promotion.rule.config.type;

public enum RuleConfigDescriptorTypeEnum {
	CSI(new StringToIntegerListConvertor()),
	DECIMAL(new StringToDecimalConvertor()),
	PERCENT(new StringToDecimalConvertor()),
	MONEY(new StringToMoneyConvertor()),
	CSKV(new StringToDiscountQuantityMapConvertor() ), //Comma Separated Key Value Pairs
    INT(new StringToIntegerConvertor()); 
	
	private Convertor convertor = null;
	
	private RuleConfigDescriptorTypeEnum(Convertor convertor) {
		this.convertor = convertor;
	}
	

	@Override
	public String toString() {
		return this.name();
	}
	
	public Object convert(String toConvertStr) {
		return this.convertor.convert(toConvertStr);
	}
}
