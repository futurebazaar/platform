package com.fb.commons.mom.to;

/**
 * @author nehaga
 *
 */
public enum CorruptMessageCause {
	CORRUPT_IDOC,
	NO_HANDLER;
	
	@Override
	public String toString() {
		return this.name();
	}
}
