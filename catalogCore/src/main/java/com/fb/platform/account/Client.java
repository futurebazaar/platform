/**
 * 
 */
package com.fb.platform.account;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author vinayak
 *
 */
public class Client {

	private int id;
	private String name;
	private String confirmedOrderEmail;
	private String pendingOrderEmail;
	private String shareProductEmail;
	private String signature;
	private String pendingOrderHelpline;
	private String confirmedOrderHelpline;
	private String smsMask;
	private String noReplyEmail;
	private String feedbackEmail;
	private String promotionsEmail;
	private String clientDomainName;
	private String salePriceList;
	private String listPriceList;
	private String slug;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConfirmedOrderEmail() {
		return confirmedOrderEmail;
	}
	public void setConfirmedOrderEmail(String confirmedOrderEmail) {
		this.confirmedOrderEmail = confirmedOrderEmail;
	}
	public String getPendingOrderEmail() {
		return pendingOrderEmail;
	}
	public void setPendingOrderEmail(String pendingOrderEmail) {
		this.pendingOrderEmail = pendingOrderEmail;
	}
	public String getShareProductEmail() {
		return shareProductEmail;
	}
	public void setShareProductEmail(String shareProductEmail) {
		this.shareProductEmail = shareProductEmail;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getPendingOrderHelpline() {
		return pendingOrderHelpline;
	}
	public void setPendingOrderHelpline(String pendingOrderHelpline) {
		this.pendingOrderHelpline = pendingOrderHelpline;
	}
	public String getConfirmedOrderHelpline() {
		return confirmedOrderHelpline;
	}
	public void setConfirmedOrderHelpline(String confirmedOrderHelpline) {
		this.confirmedOrderHelpline = confirmedOrderHelpline;
	}
	public String getSmsMask() {
		return smsMask;
	}
	public void setSmsMask(String smsMask) {
		this.smsMask = smsMask;
	}
	public String getNoReplyEmail() {
		return noReplyEmail;
	}
	public void setNoReplyEmail(String noReplyEmail) {
		this.noReplyEmail = noReplyEmail;
	}
	public String getFeedbackEmail() {
		return feedbackEmail;
	}
	public void setFeedbackEmail(String feedbackEmail) {
		this.feedbackEmail = feedbackEmail;
	}
	public String getPromotionsEmail() {
		return promotionsEmail;
	}
	public void setPromotionsEmail(String promotionsEmail) {
		this.promotionsEmail = promotionsEmail;
	}
	public String getClientDomainName() {
		return clientDomainName;
	}
	public void setClientDomainName(String clientDomainName) {
		this.clientDomainName = clientDomainName;
	}
	public String getSalePriceList() {
		return salePriceList;
	}
	public void setSalePriceList(String salePriceList) {
		this.salePriceList = salePriceList;
	}
	public String getListPriceList() {
		return listPriceList;
	}
	public void setListPriceList(String listPriceList) {
		this.listPriceList = listPriceList;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("name", this.getName())
		.append("id", this.getId())
		.append("slug", this.getSlug())
		.append("clientDomainName", this.getClientDomainName())
		.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((clientDomainName == null) ? 0 : clientDomainName.hashCode());
		result = prime
				* result
				+ ((confirmedOrderEmail == null) ? 0 : confirmedOrderEmail
						.hashCode());
		result = prime
				* result
				+ ((confirmedOrderHelpline == null) ? 0
						: confirmedOrderHelpline.hashCode());
		result = prime * result
				+ ((feedbackEmail == null) ? 0 : feedbackEmail.hashCode());
		result = prime * result
				+ ((listPriceList == null) ? 0 : listPriceList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((noReplyEmail == null) ? 0 : noReplyEmail.hashCode());
		result = prime
				* result
				+ ((pendingOrderEmail == null) ? 0 : pendingOrderEmail
						.hashCode());
		result = prime
				* result
				+ ((pendingOrderHelpline == null) ? 0 : pendingOrderHelpline
						.hashCode());
		result = prime * result
				+ ((promotionsEmail == null) ? 0 : promotionsEmail.hashCode());
		result = prime * result
				+ ((salePriceList == null) ? 0 : salePriceList.hashCode());
		result = prime
				* result
				+ ((shareProductEmail == null) ? 0 : shareProductEmail
						.hashCode());
		result = prime * result + ((slug == null) ? 0 : slug.hashCode());
		result = prime * result + ((smsMask == null) ? 0 : smsMask.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (clientDomainName == null) {
			if (other.clientDomainName != null)
				return false;
		} else if (!clientDomainName.equals(other.clientDomainName))
			return false;
		if (confirmedOrderEmail == null) {
			if (other.confirmedOrderEmail != null)
				return false;
		} else if (!confirmedOrderEmail.equals(other.confirmedOrderEmail))
			return false;
		if (confirmedOrderHelpline == null) {
			if (other.confirmedOrderHelpline != null)
				return false;
		} else if (!confirmedOrderHelpline.equals(other.confirmedOrderHelpline))
			return false;
		if (feedbackEmail == null) {
			if (other.feedbackEmail != null)
				return false;
		} else if (!feedbackEmail.equals(other.feedbackEmail))
			return false;
		if (listPriceList == null) {
			if (other.listPriceList != null)
				return false;
		} else if (!listPriceList.equals(other.listPriceList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (noReplyEmail == null) {
			if (other.noReplyEmail != null)
				return false;
		} else if (!noReplyEmail.equals(other.noReplyEmail))
			return false;
		if (pendingOrderEmail == null) {
			if (other.pendingOrderEmail != null)
				return false;
		} else if (!pendingOrderEmail.equals(other.pendingOrderEmail))
			return false;
		if (pendingOrderHelpline == null) {
			if (other.pendingOrderHelpline != null)
				return false;
		} else if (!pendingOrderHelpline.equals(other.pendingOrderHelpline))
			return false;
		if (promotionsEmail == null) {
			if (other.promotionsEmail != null)
				return false;
		} else if (!promotionsEmail.equals(other.promotionsEmail))
			return false;
		if (salePriceList == null) {
			if (other.salePriceList != null)
				return false;
		} else if (!salePriceList.equals(other.salePriceList))
			return false;
		if (shareProductEmail == null) {
			if (other.shareProductEmail != null)
				return false;
		} else if (!shareProductEmail.equals(other.shareProductEmail))
			return false;
		if (slug == null) {
			if (other.slug != null)
				return false;
		} else if (!slug.equals(other.slug))
			return false;
		if (smsMask == null) {
			if (other.smsMask != null)
				return false;
		} else if (!smsMask.equals(other.smsMask))
			return false;
		return true;
	}
}
