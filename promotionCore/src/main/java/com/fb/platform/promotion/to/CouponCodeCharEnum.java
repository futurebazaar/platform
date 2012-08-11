/**
 * 
 */
package com.fb.platform.promotion.to;

import java.io.Serializable;

import com.fb.platform.promotion.exception.InvalidAlphaNumericTypeException;

/**
 * @author ashish
 *
 */
public enum CouponCodeCharEnum implements Serializable {
	
	
	UPPER_CASE_ALPHABETS(AlphabetCase.UPPER, AlphaNumericType.ALPHABETS) {
		@Override
		public char[] getCharSet() {
			return UPPER_ALPHABETS.toCharArray();
		}
	},
	
	MIXED_CASE_ALPHABETS(AlphabetCase.MIXED, AlphaNumericType.ALPHABETS){
		@Override
		public char[] getCharSet() {
			return MIXED_ALPHABETS.toCharArray();
		}
	},
	
	LOWER_CASE_ALPHABETS(AlphabetCase.LOWER, AlphaNumericType.ALPHABETS) {
		@Override
		public char[] getCharSet() {
			return LOWER_ALPHABETS.toCharArray();
		}
	},
	
	UPPER_CASE_ALPHA_NUMBERIC(AlphabetCase.UPPER, AlphaNumericType.ALPHA_NUMERIC) {
		@Override
		public char[] getCharSet() {
			return (UPPER_ALPHABETS + NUMBERS).toCharArray();
		}
	},
	
	MIXED_CASE_ALPHA_NUMBERIC(AlphabetCase.MIXED, AlphaNumericType.ALPHA_NUMERIC){
		@Override
		public char[] getCharSet() {
			return (MIXED_ALPHABETS + NUMBERS).toCharArray();
		}
	},
	
	LOWER_CASE_ALPHA_NUMBERIC(AlphabetCase.LOWER, AlphaNumericType.ALPHA_NUMERIC){
		@Override
		public char[] getCharSet() {
			return (LOWER_ALPHABETS + NUMBERS).toCharArray();
		}
	},
	
	NUMBERIC(AlphabetCase.INVARIANT, AlphaNumericType.NUMBERS){
		@Override
		public char[] getCharSet() {
			return NUMBERS.toCharArray();
		}
	};

	// excludes I, L and O
	private static final String UPPER_ALPHABETS = "ABCDEFGHJKMNPQRSTUVWXYZ";
	private static final String LOWER_ALPHABETS = "abcdefghjkmnpqrstuvwxyz";
	private static final String MIXED_ALPHABETS = UPPER_ALPHABETS + LOWER_ALPHABETS;
	//excludes 0, 1
	private static final String NUMBERS = "23456789";
	
	private final AlphabetCase alphabetCase;
	private final AlphaNumericType alphaNumericType;
	
	private CouponCodeCharEnum(AlphabetCase alphabetCase, AlphaNumericType alphaNumericType) {
		this.alphabetCase = alphabetCase;
		this.alphaNumericType = alphaNumericType;
	}
	/**
	 * The API returns the character set on the basis of the alphaNumericType and AlphabetCase
	 * provided in the input.
	 * The API can throw InvalidAlphaNumericTypeException if the input provided are invalid.
	 * 
	 * @param alphaNumericType
	 * @param alphabetCase
	 * @return
	 */
	public static char[] from(AlphaNumericType alphaNumericType, AlphabetCase alphabetCase){
		for(CouponCodeCharEnum codeCharEnum : CouponCodeCharEnum.values()){
			if(codeCharEnum.getAlphabetCase().compareTo(alphabetCase)==0
				&& codeCharEnum.getAlphaNumericType().compareTo(alphaNumericType)==0){
				return codeCharEnum.getCharSet();
			}
		}
		
		throw new InvalidAlphaNumericTypeException("Invalid combinatin of alpha number type and case provided for coupon code generation");
	}

	public abstract char[] getCharSet();

	public AlphabetCase getAlphabetCase() {
		return alphabetCase;
	}

	public AlphaNumericType getAlphaNumericType() {
		return alphaNumericType;
	}
}
