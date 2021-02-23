package com.dere.viewerfx.formatter;

public class FormatterFactory {

	public IDataContentFormatter getFormatter(String type) {
		// TODO add auto discovery via CDI from classpath
		switch (type) {
		case "json":
			return new JSONDataContentFormatter();
		case "xml":
			return new XMLDataContentFormatter();
		default:
			return new NoDataContentFormatter();
		}
	}
}
