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
public class PgiCreationItemTO extends ItemTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4863184489294614520L;
	
	private DateTime pgiCreationDate;

	public DateTime getPgiCreationDate() {
		return pgiCreationDate;
	}
	public void setPgiCreationDate(DateTime pgiCreationDate) {
		this.pgiCreationDate = pgiCreationDate;
	}

	@Override
	public String toString(){
		return new ToStringBuilder(this)
		.append("itemTO", super.toString())
		.append("pgiCreationDate", this.pgiCreationDate)
		.toString();
	}
}
