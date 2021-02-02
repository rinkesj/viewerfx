package com.dere.viewerfx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import jfxtras.styles.jmetro.Style;

public class MenuController implements Initializable, ListChangeListener<File> {

	@FXML
	private MenuBar menuBar;

	@FXML
	private TabPane fileTabs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		List<File> files = ViewerModel.getInstance().getFiles();
		for (File file : files) {
			fileTabs.getTabs().add(createTab(file));
		}

		ViewerModel.getInstance().registerListener(this);
	}

	private Tab createTab(File file) {
		Tab tab = new Tab(file.getName());
		String content = "";
		try {
			content = Files.readString(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		TextArea value = new TextArea(content);
		value.setWrapText(true);
		tab.setContent(value);
		return tab;
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

	@Override
	public void onChanged(Change<? extends File> change) {

		while (change.next()) {
			List<? extends File> addedSubList = change.getAddedSubList();
			for (File file : addedSubList) {
				fileTabs.getTabs().add(createTab(file));
			}
		}
	}
}
