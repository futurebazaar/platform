package com.fb.platform.sap.client.mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;

import com.sap.conn.jco.JCoAbapObject;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class MockJcoParameterList implements JCoParameterList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public final Object clone() {
	    return new Object();
	  }
	
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public int copyFrom(JCoRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public JCoAbapObject getAbapObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public JCoAbapObject getAbapObject(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public BigDecimal getBigDecimal(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public BigDecimal getBigDecimal(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public BigInteger getBigInteger(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public BigInteger getBigInteger(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public InputStream getBinaryStream(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public InputStream getBinaryStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public byte getByte(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public byte getByte(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public byte[] getByteArray(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public byte[] getByteArray(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public char getChar(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public char getChar(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public char[] getCharArray(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public char[] getCharArray(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Reader getCharacterStream(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Reader getCharacterStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getClassNameOfValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Date getDate(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Date getDate(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public double getDouble(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double getDouble(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public JCoFieldIterator getFieldIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public float getFloat(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public float getFloat(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getInt(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getInt(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long getLong(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public long getLong(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public JCoMetaData getMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public short getShort(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public short getShort(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String getString(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getString(String arg0) {
		return new String();
	}

	
	public JCoStructure getStructure(int arg0) {
		return new MockJcoStructure();
	}

	
	public JCoStructure getStructure(String arg0) {
		return new MockJcoStructure();
	}

	
	public JCoTable getTable(int arg0) {
		return new MockJcoTable();
	}

	
	public JCoTable getTable(String arg0) {
		return new MockJcoTable();
	}

	
	public Date getTime(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Date getTime(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object getValue(int arg0) {
		return new Object();
	}

	
	public Object getValue(String key) {
		return new String("TEST");
	}

	
	public boolean isInitialized(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isInitialized(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Iterator<JCoField> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setValue(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, char[] arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, short arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, JCoStructure arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, JCoTable arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, char[] arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, short arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, BigDecimal arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, BigDecimal arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, byte arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, byte arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, JCoStructure arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, JCoTable arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, JCoAbapObject arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, JCoAbapObject arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(int arg0, char[] arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	
	public void setValue(String arg0, char[] arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String toXML(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String toXML(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Writer write(int arg0, Writer arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Writer write(String arg0, Writer arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public JCoListMetaData getListMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public JCoParameterFieldIterator getParameterFieldIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isActive(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isActive(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void setActive(int arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void setActive(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

}
