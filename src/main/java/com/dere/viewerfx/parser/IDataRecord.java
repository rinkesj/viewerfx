package com.dere.viewerfx.parser;

public interface IDataRecord {
	
	Object getContent();
	Object getColumnValues();
	Object getColumnValue(String column);
	Object getColumnValue(int index);
}
