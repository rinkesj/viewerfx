package com.dere.viewerfx.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class DelimiterFileParser implements IDataFileParser {
	
	@Override
	public IDataFile parseFile(File file) {
		List<IDataRecord> records = new LinkedList<IDataRecord>();
		try {
			List<String> readAllLines = Files.readAllLines(file.toPath());
			
			for (String line : readAllLines) {
				String[] split = line.split(";");
				DataRecord record = new DataRecord(split);
				records.add(record);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new DataFile(file, records);
	} 
}
