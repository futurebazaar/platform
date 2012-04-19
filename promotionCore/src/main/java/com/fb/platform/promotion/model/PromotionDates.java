/**
 * 
 */
package com.fb.platform.promotion.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

/**
 * @author vinayak
 *
 */
public class PromotionDates implements Serializable {

	private DateTime createdOn;
	private DateTime validFrom;
	private DateTime validTill;
	private DateTime lastModifiedOn;

	public boolean isWithinDates() {
		DateTimeComparator dateComparator = DateTimeComparator.getInstance();
		boolean greaterThanStart = false;
		boolean lessThanEnd = false;
		if (validFrom == null) {
			greaterThanStart = true;
		} else {
			if (dateComparator.compare(validFrom, null) <= 0) {
				greaterThanStart = true;
			}
		}
		if (validTill == null) {
			lessThanEnd = true;
		} else {
			if (dateComparator.compare(validTill, null) >= 0) {
				lessThanEnd = true;
			}
		}
		if (greaterThanStart && lessThanEnd) {
			return true;
		}
		return false;
	}

	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}

	public DateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(DateTime validFrom) {
		this.validFrom = validFrom;
	}

	public DateTime getValidTill() {
		return validTill;
	}

	public void setValidTill(DateTime validTill) {
		this.validTill = validTill;
	}

	public DateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(DateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
}
