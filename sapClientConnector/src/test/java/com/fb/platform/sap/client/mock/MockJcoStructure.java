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
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class MockJcoStructure implements JCoStructure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public final Object clone() {
	    return new Object();
	  }
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int copyFrom(JCoRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JCoAbapObject getAbapObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoAbapObject getAbapObject(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getBigInteger(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getBigInteger(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getByte(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getByteArray(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getByteArray(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getChar(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getChar(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char[] getCharArray(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCharArray(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClassNameOfValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JCoFieldIterator getFieldIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getFloat(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JCoMetaData getMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoStructure getStructure(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoStructure getStructure(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoTable getTable(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoTable getTable(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getTime(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getTime(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInitialized(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInitialized(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<JCoField> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, char[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, short arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, JCoStructure arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, JCoTable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, char[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, short arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, double arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, BigDecimal arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, BigDecimal arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, byte arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, byte arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, JCoStructure arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, JCoTable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, JCoAbapObject arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, JCoAbapObject arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0, char[] arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, char[] arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer write(int arg0, Writer arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer write(String arg0, Writer arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoRecordFieldIterator getRecordFieldIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JCoRecordMetaData getRecordMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

}
