package com.dere.viewerfx;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dere.viewerfx.cdi.model.ViewerModel;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * ViewerFX App
 */
public class ViewerFxApplication {

	@Inject
	private FXMLLoader fxmlLoader;

	@Inject
	private ViewerModel model;

	private static final Logger LOGGER = LogManager.getLogger(ViewerFxApplication.class.getSimpleName());

	StackPane root = new StackPane();
	private static Scene scene;
	public static JMetro jMetro;

	public void start(Stage stage) throws IOException {
		System.out.println("LOAD");
		Parent main = loadFXML("main");

		main.setOnDragOver(event -> {
			// On drag over if the DragBoard has files
			if (event.getGestureSource() != main && event.getDragboard().hasFiles()) {
				// All files on the dragboard must have an accepted extension
				// Allow for both copying and moving
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
		});

		main.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				boolean success = false;
				if (event.getGestureSource() != main && event.getDragboard().hasFiles()) {
					// Print files
					event.getDragboard().getFiles().forEach(file -> System.out.println(file.getAbsolutePath()));
					model.addFile(event.getDragboard().getFiles());
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		root.getChildren().add(main);
		scene = new Scene(root, 1024, 768); // TODO config
		jMetro = new JMetro(Style.LIGHT); // TODO config
		jMetro.setScene(scene);
		stage.setScene(scene);
		stage.show();
	}

	private Parent loadFXML(String fxml) throws IOException {
		fxmlLoader.setLocation(getClass().getResource("./" + fxml + ".fxml"));
		return fxmlLoader.load();
	}

}