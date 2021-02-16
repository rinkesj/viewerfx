package com.dere.viewerfx.parser;

import java.io.File;
import java.util.List;

public interface IDataFile {
	
	List<IDataRecord> getRecords();
	File getFile();
}
