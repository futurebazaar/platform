package com.fb.commons.mom.to;

import org.apache.commons.lang.builder.ToStringBuilder;
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
		return new ToStringBuilder(this)
		.append("itemTO", super.toString())
		.append("pgrCreationDate", this.pgrCreationDate)
		.toString();
	}
}
