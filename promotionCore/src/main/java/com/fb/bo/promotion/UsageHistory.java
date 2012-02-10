package com.fb.bo.promotion;

import java.sql.Timestamp;

public class UsageHistory {

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getUsedOn() {
		return usedOn;
	}
	public void setUsedOn(Timestamp usedOn) {
		this.usedOn = usedOn;
	}
	public int getUsedBy() {
		return usedBy;
	}
	public void setUsedBy(int usedBy) {
		this.usedBy = usedBy;
	}
	public float getDiscValueClaimed() {
		return discValueClaimed;
	}
	public void setDiscValueClaimed(float discValueClaimed) {
		this.discValueClaimed = discValueClaimed;
	}
	private int id;
	private Timestamp usedOn;
	private int usedBy;
	private float discValueClaimed;
}
