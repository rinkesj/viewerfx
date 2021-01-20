package com.dere.viewerfx;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

/**
 * ViewerFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	final SplitPane rootPane = new SplitPane();
    	final SplitPane main = new SplitPane();
    	rootPane.setOrientation(Orientation.VERTICAL);
    	main.setOrientation(Orientation.HORIZONTAL);
    	
    	main.getItems().add(loadFXML("primary"));
    	main.getItems().add(loadFXML("secondary"));
    	
    	rootPane.getItems().add(main);
    	rootPane.getItems().add(loadFXML("third"));
    	
    	rootPane.setOnDragOver(event -> {
            // On drag over if the DragBoard has files
            if (event.getGestureSource() != rootPane && event.getDragboard().hasFiles()) {
				// All files on the dragboard must have an accepted extension
                // Allow for both copying and moving
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    	
    	rootPane.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
            	boolean success = false;
                if (event.getGestureSource() != rootPane && event.getDragboard().hasFiles()) {
                    // Print files
                    event.getDragboard().getFiles().forEach(file -> System.out.println(file.getAbsolutePath()));
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    	
        scene = new Scene(rootPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}