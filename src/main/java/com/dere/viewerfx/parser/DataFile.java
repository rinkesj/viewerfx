package com.dere.viewerfx.parser;

import java.io.File;
import java.util.List;

public class DataFile implements IDataFile {
	
	private List<IDataRecord> records;
	private File file;
	private List<String> columns;
	
	public DataFile() {
	}
	
	public DataFile(File file) {
		this.file = file;
	}
	
	public DataFile(File file, List<IDataRecord> records) {
		this.file = file;
		this.records = records;
	}

	@Override
	public List<IDataRecord> getRecords() {
		return records;
	}

	@Override
	public File getFile() {
		return file;
	}
}
