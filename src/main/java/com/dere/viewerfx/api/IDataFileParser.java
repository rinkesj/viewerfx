package com.dere.viewerfx.api;

import java.io.File;

public interface IDataFileParser {
	
	String type();
	IDataFile parseFile(File file);
}
