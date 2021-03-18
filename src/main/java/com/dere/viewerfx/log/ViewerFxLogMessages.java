package com.dere.viewerfx.log;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;

public enum ViewerFxLogMessages {

	INPUT_FILE_ADDED("Added file %s into view model."),
	INPUT_FILE_FAILED("Input file %s is not a file or doesnt exist."),
	FXML_LOADER_NO_APPLICATION_SCOPED("Controller class %s has annotation %s. Controller should be one per program and annotated by @ApplicationScope or @Singleton otherwise controller might not work properly."),
	;

	private String messagePattern;

	private ViewerFxLogMessages(String messagePattern) {
		this.messagePattern = messagePattern;
	}

	public Message log(Object... params) {
		StringFormattedMessage message = new StringFormattedMessage(messagePattern, params);
		return message;
	}
}
