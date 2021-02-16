package com.dere.viewerfx.parser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DelimiterFileParserTest {

	DelimiterFileParser parser = new DelimiterFileParser();
	
	@Test
	public void testDelimiter() throws URISyntaxException {
		String resource = getClass().getClassLoader().getResource("delimiter_file.csv").getPath();
		
		IDataFile parseFile = parser.parseFile(new File(resource));
		List<IDataRecord> records = parseFile.getRecords();
		System.out.println(records.size());
		for (IDataRecord iDataRecord : records) {
			System.out.println(Arrays.toString((String[])iDataRecord.getColumnValues()));
		}
	}
	
}
