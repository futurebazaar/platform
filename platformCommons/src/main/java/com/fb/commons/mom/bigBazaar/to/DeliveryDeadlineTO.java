/**
 * 
 */
package com.fb.commons.mom.bigBazaar.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author nehaga
 *
 */
public class DeliveryDeadlineTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5474282878585188515L;
	
	private String qualifier;
	private DateTime activityStartDate;
	private DateTime activityFinishDate;
	private DateTime actualStartDate;
	private DateTime actualFinishDate;
	private int segment;
	
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public DateTime getActivityStartDate() {
		return activityStartDate;
	}
	public void setActivityStartDate(DateTime activityStartDate) {
		this.activityStartDate = activityStartDate;
	}
	public DateTime getActivityFinishDate() {
		return activityFinishDate;
	}
	public void setActivityFinishDate(DateTime activityFinishDate) {
		this.activityFinishDate = activityFinishDate;
	}
	public DateTime getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(DateTime actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public DateTime getActualFinishDate() {
		return actualFinishDate;
	}
	public void setActualFinishDate(DateTime actualFinishDate) {
		this.actualFinishDate = actualFinishDate;
	}
	public int getSegment() {
		return segment;
	}
	public void setSegment(int segment) {
		this.segment = segment;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("qualifier", this.qualifier)
			.append("activityStartDate", this.activityStartDate)
			.append("activityFinishDate", this.activityFinishDate)
			.append("actualStartDate", this.actualStartDate)
			.append("actualFinishDate", this.actualFinishDate)
			.append("segment", this.segment)
			.toString();
	}
	
}
