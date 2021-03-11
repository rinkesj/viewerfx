package com.dere.viewerfx.parser;

import com.dere.viewerfx.api.IDataFile;
import com.dere.viewerfx.api.IDataRecord;

public class DataRecord implements IDataRecord {
	
	private String[] columns;
	private IDataFile dataFile;
	private int start;
	private int end;
	
	public DataRecord(String[] columns, int start, int end) {
		this.columns = columns;
		this.start = start;
		this.end = end;
	}
	
	public DataRecord(String[] columns, IDataFile dataFile) {
		this.columns = columns;
		this.dataFile = dataFile;
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

	@Override
	public IDataFile getDataFile() {
		return dataFile;
	}
	
	@Override
	public void setDataFile(IDataFile dataFile) {
		this.dataFile = dataFile;
	}

	@Override
	public int getStartIndex() {
		return start;
	}

	@Override
	public int getEndIndex() {
		return end;
	}
}
