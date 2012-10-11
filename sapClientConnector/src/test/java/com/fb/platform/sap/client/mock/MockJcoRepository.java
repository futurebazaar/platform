package com.fb.platform.sap.client.mock;

import com.sap.conn.jco.JCoClassMetaData;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoRequest;
import com.sap.conn.jco.monitor.JCoRepositoryMonitor;

public class MockJcoRepository implements JCoRepository {

	@Override
	public void clear() {
		
	}

	@Override
	public String[] getCachedClassMetaDataNames() {
		return null;
	}

	@Override
	public String[] getCachedFunctionTemplateNames() {
		return null;
	}

	@Override
	public String[] getCachedRecordMetaDataNames() {
		return null;
	}

	@Override
	public JCoClassMetaData getClassMetaData(String arg0) throws JCoException {
		return null;
	}

	@Override
	public JCoFunction getFunction(String arg0) throws JCoException {
		return null;
	}

	@Override
	public JCoListMetaData getFunctionInterface(String arg0) throws JCoException {
		return null;
	}

	@Override
	public JCoFunctionTemplate getFunctionTemplate(String arg0) throws JCoException {
		return new MockJcoFunctionTemplate();
	}

	@Override
	public JCoRepositoryMonitor getMonitor() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public JCoRecordMetaData getRecordMetaData(String arg0) throws JCoException {
		return null;
	}

	@Override
	public JCoRequest getRequest(String arg0) throws JCoException {
		return null;
	}

	@Override
	public JCoRecordMetaData getStructureDefinition(String arg0) throws JCoException {
		return null;
	}

	@Override
	public boolean isUnicode() {
		return false;
	}

	@Override
	public void removeClassMetaDataFromCache(String arg0) {
		
	}

	@Override
	public void removeFunctionTemplateFromCache(String arg0) {
		
	}

	@Override
	public void removeRecordMetaDataFromCache(String arg0) {
		
	}

}
