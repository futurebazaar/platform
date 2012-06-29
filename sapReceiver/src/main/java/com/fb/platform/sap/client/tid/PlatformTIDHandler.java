/**
 * 
 */
package com.fb.platform.sap.client.tid;

import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerTIDHandler;

/**
 * @author vinayak
 *
 */
public class PlatformTIDHandler implements JCoServerTIDHandler {

	/* (non-Javadoc)
	 * @see com.sap.conn.jco.server.JCoServerTIDHandler#checkTID(com.sap.conn.jco.server.JCoServerContext, java.lang.String)
	 */
	@Override
	public boolean checkTID(JCoServerContext serverCtx, String tid) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.sap.conn.jco.server.JCoServerTIDHandler#commit(com.sap.conn.jco.server.JCoServerContext, java.lang.String)
	 */
	@Override
	public void commit(JCoServerContext serverCtx, String tid) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.sap.conn.jco.server.JCoServerTIDHandler#confirmTID(com.sap.conn.jco.server.JCoServerContext, java.lang.String)
	 */
	@Override
	public void confirmTID(JCoServerContext serverCtx, String tid) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.sap.conn.jco.server.JCoServerTIDHandler#rollback(com.sap.conn.jco.server.JCoServerContext, java.lang.String)
	 */
	@Override
	public void rollback(JCoServerContext serverCtx, String tid) {
		// TODO Auto-generated method stub

	}

}
