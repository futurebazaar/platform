package com.fb.platform.sap.client.mock;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoRequest;

public class MockJcoFunctionTemplate implements JCoFunctionTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public JCoListMetaData getChangingParameterList() {
		return null;
	}

	@Override
	public AbapException getException(String arg0) {
		return null;
	}

	@Override
	public AbapException[] getExceptionList() {
		return null;
	}

	@Override
	public JCoListMetaData getExportParameterList() {
		return null;
	}

	@Override
	public JCoFunction getFunction() {
		return new MockJcoFunction();
	}

	@Override
	public JCoListMetaData getFunctionInterface() {
		return null;
	}

	@Override
	public JCoListMetaData getImportParameterList() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public JCoRequest getRequest() {
		return null;
	}

	@Override
	public JCoListMetaData getTableParameterList() {
		return null;
	}

	@Override
	public boolean supportsASXML() {
		return false;
	}

}
