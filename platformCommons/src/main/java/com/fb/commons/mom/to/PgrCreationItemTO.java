package com.fb.commons.mom.to;

import org.joda.time.DateTime;

/**
 * 
 */

/**
 * @author nehaga
 *
 */
public class PgrCreationItemTO extends ItemTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6847576851593088364L;
	
	private DateTime pgrCreationDate;

	public DateTime getPgrCreationDate() {
		return pgrCreationDate;
	}
	public void setPgrCreationDate(DateTime pgrCreationDate) {
		this.pgrCreationDate = pgrCreationDate;
	}


	@Override
	public String toString(){
		String cancelItem = super.toString()
				+ "\nPGR creation date : " + pgrCreationDate;
		return cancelItem;
	}
}