package com.dere.viewerfx.parser;

public class DataRecord implements IDataRecord {
	
	private String[] columns;

	public DataRecord(String[] columns) {
		this.columns = columns;
	}
	
	public String[] getColumns() {
		return columns;
	}

	@Override
	public Object getContent() {
		return columns[columns.length-1];
	}

	@Override
	public Object getColumnValue(String column) {
		return null;
	}
	
	@Override
	public Object getColumnValue(int index) {
		return columns[index];
	}

	@Override
	public String[] getColumnValues() {
		return columns;
	}
}
