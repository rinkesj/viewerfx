package com.dere.viewerfx.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import com.dere.viewerfx.api.IDataFile;
import com.dere.viewerfx.api.IDataFileParser;
import com.dere.viewerfx.api.IDataRecord;

/**
 * TODO add configurable delimiter character per record type
 */
public class DelimiterFileParser implements IDataFileParser {
	
	@Override
	public IDataFile parseFile(File file) {
		List<IDataRecord> records = new LinkedList<IDataRecord>();
		try {
			List<String> readAllLines = Files.readAllLines(file.toPath());
			
			int start = 0;
			int end = 0;
			for (String line : readAllLines) {
				end = start + line.length() + System.lineSeparator().length();
				String[] split = line.split(";");
				DataRecord record = new DataRecord(split, start, end);
				records.add(record);
				start = end;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new DataFile(file, records);
	}

	@Override
	public String type() {
		return "delimiter";
	} 
}
