package com.dere.viewerfx;

import org.junit.jupiter.api.Test;

public class MainTest {

	@Test
	public void runWithData() {

		String file1 = getClass().getClassLoader().getResource("delimiter_file1.csv").getPath();
		String file2 = getClass().getClassLoader().getResource("delimiter_file2.csv").getPath();

		String[] args = new String[] { file1, file2 };
		System.setProperty("log4j.configurationFile", "log4j-test.xml");
		ViewerFXStartup.main(args);
	}

}
