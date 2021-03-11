package com.dere.viewerfx.formatter;

import com.dere.viewerfx.api.IDataContentFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// TODO this is maybe not ideal as it has to be valid json and it is parsed. Need better formatter.
public class JSONDataContentFormatter implements IDataContentFormatter {

	@Override
	public String type() {
		return "json";
	}

	@Override
	public String format(Object content) {
		JsonObject json = JsonParser.parseString((String) content).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonFormatted = gson.toJson(json);
		return jsonFormatted;
	}

}
