package com.dere.viewerfx;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.dere.viewerfx.api.IDataFile;
import com.dere.viewerfx.cdi.model.ViewerModel;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import jfxtras.styles.jmetro.Style;

@Singleton
public class MenuBarController implements ListChangeListener<IDataFile> {
	
	@Inject
	private ViewerModel model;
	
	@FXML
	public void initialize() throws IOException {
		model.registerListener(this);
	}
	
	@FXML
	private void handleMenuAction(ActionEvent event) {
		MenuItem source = (MenuItem) event.getSource();
		if (source.getId().equalsIgnoreCase("StyleLight")) {
			ViewerFxApplication.jMetro.setStyle(Style.LIGHT);
		} else {
			ViewerFxApplication.jMetro.setStyle(Style.DARK);
		}
	}
	
	@FXML
	private void showColumnDialog(ActionEvent event) {
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(null, model.getAllColumns());
		dialog.setResizable(true);
		dialog.show();
		// hack for issue : https://github.com/javafxports/openjdk-jfx/issues/222
		Platform.runLater(() -> {
			dialog.getDialogPane().getScene().getWindow().sizeToScene();
			dialog.setResizable(false);
			});
		
		System.out.println("HERE");
	}
	
	


	@Override
	public void onChanged(Change<? extends IDataFile> c) {
		
	}
	
}
