package com.fb.commons.mom.to;

/**
 * 
 */

/**
 * @author nehaga
 *
 */
public class AtgDocumentItemTO extends ItemTO {
	private int atgDocumentId;

	public int getAtgDocumentId() {
		return atgDocumentId;
	}

	public void setAtgDocumentId(int atgDocumentId) {
		this.atgDocumentId = atgDocumentId;
	}

	@Override
	public String toString(){
		String cancelItem = super.toString()
				+ "\nATG document id : " + atgDocumentId;
		return cancelItem;
	}
}
