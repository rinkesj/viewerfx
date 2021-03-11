package com.dere.viewerfx.formatter;

import com.dere.viewerfx.api.IDataContentFormatter;

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
