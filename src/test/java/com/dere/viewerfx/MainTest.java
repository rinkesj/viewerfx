package com.dere.viewerfx;

import org.junit.jupiter.api.Test;

public class MainTest {

	@Test
	public void runWithData() {

		String file1 = getClass().getClassLoader().getResource("delimiter_file.csv").getPath();

		String[] args = new String[] { file1, file1, file1 };
		System.setProperty("log4j.configurationFile", "log4j-test.xml");
		ViewerFxApplication.main(args);
	}

}
