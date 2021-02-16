package com.dere.viewerfx;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;


/**
 * ViewerFX App
 * TODO
 * - Logger
 * - CDI
 * - Docking
 * - Loading custom config
 */
public class ViewerFxApplication extends Application {

    private static Scene scene;
	public static JMetro jMetro;

    @Override
    public void start(Stage stage) throws IOException {
    	new ViewerModel();
    	Parent root = loadFXML("main");
    	
    	root.setOnDragOver(event -> {
            // On drag over if the DragBoard has files
            if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
				// All files on the dragboard must have an accepted extension
                // Allow for both copying and moving
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    	
    	root.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
            	boolean success = false;
                if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
                    // Print files
                    event.getDragboard().getFiles().forEach(file -> System.out.println(file.getAbsolutePath()));
                    ViewerModel.getInstance().addFile(event.getDragboard().getFiles());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    	
        scene = new Scene(root, 1024, 768);
        jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewerFxApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}