package com.fb.platform.sap.client.mock;

import com.sap.conn.jco.AbapClassException.Mode;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoParameterList;

public class MockJcoFunction implements JCoFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(JCoDestination arg0) throws JCoException {
		
	}

	@Override
	public void execute(JCoDestination arg0, String arg1) throws JCoException {
		
	}

	@Override
	public void execute(JCoDestination arg0, String arg1, String arg2) throws JCoException {
		
	}

	@Override
	public JCoParameterList getChangingParameterList() {
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
	public JCoParameterList getExportParameterList() {
		return new MockJcoParameterList();
	}

	@Override
	public JCoFunctionTemplate getFunctionTemplate() {
		return new MockJcoFunctionTemplate();
	}

	@Override
	public JCoParameterList getImportParameterList() {
		return new MockJcoParameterList();
	}

	@Override
	public String getName() {
		return "TEST JCO FUNCTION";
	}

	@Override
	public JCoParameterList getTableParameterList() {
		return new MockJcoParameterList();
	}

	@Override
	public boolean isAbapClassExceptionEnabled() {
		return false;
	}

	@Override
	public void setAbapClassExceptionMode(Mode arg0) {
		
	}

	@Override
	public String toXML() {
		return null;
	}

}
