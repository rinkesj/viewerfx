package com.dere.viewerfx;

import java.io.File;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dere.viewerfx.cdi.model.ViewerModel;
import com.dere.viewerfx.log.ViewerFxLogMessages;

import javafx.application.Application;
import javafx.stage.Stage;

public class ViewerFXStartup extends Application {
	
	private static final Logger LOGGER = LogManager.getLogger(ViewerFXStartup.class.getSimpleName());
	private static SeContainer container;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		container.select(ViewerFxApplication.class).get().start(primaryStage);
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		container.close();
	}
	
	public static void main(String[] args) {

		new ViewerModel();

		LOGGER.info("LAUNCH");
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();
		container = initializer.initialize();
		
		ViewerModel model = container.select(ViewerModel.class).get();

		for (String inputFilePath : args) {
			File inputFile = new File(inputFilePath);
			if (inputFile.exists() && inputFile.isFile()) {
				model.addFile(inputFile);
				LOGGER.warn(ViewerFxLogMessages.INPUT_FILE_ADDED.log(inputFilePath));
			} else {
				LOGGER.info(ViewerFxLogMessages.INPUT_FILE_FAILED.log(inputFilePath));
			}
		}
		
		
		launch(args);
	}

}
