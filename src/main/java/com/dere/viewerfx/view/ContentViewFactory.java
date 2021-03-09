package com.dere.viewerfx.view;

public class ContentViewFactory {

	public IContentView getFormatter(String type) {
		// TODO add auto discovery via CDI from classpath
		switch (type) {
		case "json":
			return new JsonContentTreeView();
		case "xml":
			return new XmlContentTreeView();
		default:
			return null;
		}
	}

}