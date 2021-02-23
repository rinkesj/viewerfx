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
	public Object getContent() { // TODO trim and null check
		return columns[columns.length-1].trim();
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

	@Override
	public String getContentType() {
		return columns[0].replace("SERVICE_", "").toLowerCase(); // TODO this is far away from ideal
	}
}
