package com.dere.viewerfx.formatter;

public class NoDataContentFormatter implements IDataContentFormatter {

	@Override
	public String format(Object content) {
		return (String) content;
	}

	@Override
	public String type() {
		return "text";
	}

}
