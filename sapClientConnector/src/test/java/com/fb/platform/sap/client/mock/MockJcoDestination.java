package com.fb.platform.sap.client.mock;

import java.util.Properties;

import com.sap.conn.jco.JCoAttributes;
import com.sap.conn.jco.JCoCustomDestination;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunctionUnitState;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoRuntimeException;
import com.sap.conn.jco.JCoThroughput;
import com.sap.conn.jco.JCoUnitIdentifier;
import com.sap.conn.jco.monitor.JCoDestinationMonitor;

public class MockJcoDestination implements JCoDestination {

	@Override
	public void changePassword(String arg0, String arg1) throws JCoException {
		
	}

	@Override
	public void confirmFunctionUnit(JCoUnitIdentifier arg0) throws JCoException {
		
	}

	@Override
	public void confirmTID(String arg0) throws JCoException {
		
	}

	@Override
	public JCoCustomDestination createCustomDestination() {
		return null;
	}

	@Override
	public String createTID() throws JCoException {
		return null;
	}

	@Override
	public String getAliasUser() {
		return null;
	}

	@Override
	public String getApplicationServerHost() {
		return null;
	}

	@Override
	public JCoAttributes getAttributes() throws JCoException {
		return null;
	}

	@Override
	public String getClient() {
		return null;
	}

	@Override
	public String getDestinationID() {
		return null;
	}

	@Override
	public String getDestinationName() {
		return null;
	}

	@Override
	public long getExpirationCheckPeriod() {
		return 0;
	}

	@Override
	public long getExpirationTime() {
		return 0;
	}

	@Override
	public JCoFunctionUnitState getFunctionUnitState(JCoUnitIdentifier arg0)
			throws JCoException {
		return null;
	}

	@Override
	public String getGatewayHost() {
		return null;
	}

	@Override
	public String getGatewayService() {
		return null;
	}

	@Override
	public String getLanguage() {
		return null;
	}

	@Override
	public String getLogonCheck() {
		return null;
	}

	@Override
	public String getLogonGroup() {
		return null;
	}

	@Override
	public long getMaxGetClientTime() {
		return 0;
	}

	@Override
	public String getMessageServerHost() {
		return null;
	}

	@Override
	public String getMessageServerService() {
		return null;
	}

	@Override
	public JCoDestinationMonitor getMonitor() throws JCoRuntimeException {
		return null;
	}

	@Override
	public int getPeakLimit() {
		return 0;
	}

	@Override
	public int getPoolCapacity() {
		return 0;
	}

	@Override
	public Properties getProperties() {
		return null;
	}

	@Override
	public String getR3Name() {
		return null;
	}

	@Override
	public JCoRepository getRepository() throws JCoException {
		return new MockJcoRepository();
	}

	@Override
	public JCoDestinationMonitor getRepositoryDestinationMonitor() {
		return null;
	}

	@Override
	public String getRepositoryUser() {
		return null;
	}

	@Override
	public String getSAPRouterString() {
		return null;
	}

	@Override
	public String getSncLibrary() {
		return null;
	}

	@Override
	public String getSncMode() {
		return null;
	}

	@Override
	public String getSncMyName() {
		return null;
	}

	@Override
	public String getSncPartnerName() {
		return null;
	}

	@Override
	public String getSncQOP() {
		return null;
	}

	@Override
	public String getSystemNumber() {
		return null;
	}

	@Override
	public String getTPHost() {
		return null;
	}

	@Override
	public String getTPName() {
		return null;
	}

	@Override
	public JCoThroughput getThroughput() {
		return null;
	}

	@Override
	public char getType() {
		return 0;
	}

	@Override
	public String getUser() {
		return null;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public void ping() throws JCoException {
		
	}

	@Override
	public void removeThroughput() {
		
	}

	@Override
	public void setThroughput(JCoThroughput arg0) {
		
	}

}
