package com.fb.platform.payback.model;

import java.io.Serializable;

public class RollbackHeader implements Serializable {

		private int itemRowsDeleted;
		private int headerRowsDeleted;
		
		private long headerId;

		public int getItemRowsDeleted() {
			return itemRowsDeleted;
		}

		public void setItemRowsDeleted(int itemRowsDeleted) {
			this.itemRowsDeleted = itemRowsDeleted;
		}

		public int getHeaderRowsDeleted() {
			return headerRowsDeleted;
		}

		public void setHeaderRowsDeleted(int headerRowsDeleted) {
			this.headerRowsDeleted = headerRowsDeleted;
		}

		public long getHeaderId() {
			return headerId;
		}

		public void setHeaderId(long headerId) {
			this.headerId = headerId;
		}
}
