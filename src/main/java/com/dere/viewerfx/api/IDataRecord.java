package com.dere.viewerfx.api;

public interface IDataRecord {
	
	Object getContent();
	Object getColumnValues();
	Object getColumnValue(String column);
	Object getColumnValue(int index);
	String getContentType();
	IDataFile getDataFile();
	void setDataFile(IDataFile dataFile);
	int getStartIndex();
	int getEndIndex();
}
