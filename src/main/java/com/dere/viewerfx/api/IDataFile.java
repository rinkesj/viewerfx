package com.dere.viewerfx.api;

import java.io.File;
import java.util.List;

public interface IDataFile {
	
	List<IDataRecord> getRecords();
	File getFile();
}
