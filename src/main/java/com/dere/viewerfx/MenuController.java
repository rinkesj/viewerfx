package com.dere.viewerfx;

import java.awt.print.Book;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.dere.viewerfx.formatter.FormatterFactory;
import com.dere.viewerfx.parser.IDataFile;
import com.dere.viewerfx.parser.IDataRecord;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import jfxtras.styles.jmetro.Style;

public class MenuController implements Initializable, ListChangeListener<IDataFile> {

	@FXML
	private MenuBar menuBar;

	@FXML
	private TabPane fileTabs;
	
	@FXML
	private TabPane tabPaneDetail;

	@FXML
	private TableView tableView;
	
	FormatterFactory formatFactory = new FormatterFactory();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		List<IDataFile> files = ViewerModel.getInstance().getFiles();
		for (IDataFile file : files) {
			fileTabs.getTabs().add(createTab(file.getFile()));
		}

		tableView.getColumns().addAll(createColumn());
		tableView.setItems(ViewerModel.getInstance().getRecords());
		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<IDataRecord>() {

			@Override
			public void onChanged(Change<? extends IDataRecord> c) {
				IDataRecord selectedItem = (IDataRecord) MenuController.this.tableView.getSelectionModel().getSelectedItem();
				System.out.println(selectedItem.getColumnValue(0));
				TextArea value = new TextArea(formatFactory.getFormatter(selectedItem.getContentType()).format(selectedItem.getContent()));
				value.setWrapText(true);
				tabPaneDetail.getTabs().get(0).setContent(value);
			}
			
		});
		ViewerModel.getInstance().registerListener(this);
	}
	
	private Collection createColumn() {

		List<TableColumn> viewColumns = ViewerModel.getInstance().getViewColumns().stream().map(s -> new TableColumn(s))
				.collect(Collectors.toList());

		for (TableColumn tableColumn : viewColumns) {
			tableColumn
					.setCellValueFactory(new Callback<CellDataFeatures<IDataRecord, String>, SimpleStringProperty>() {
						public SimpleStringProperty call(CellDataFeatures<IDataRecord, String> record) {

							return new SimpleStringProperty((String) record.getValue().getColumnValue(
									record.getTableView().getColumns().indexOf(record.getTableColumn())));
						}
					});
		}

		return viewColumns;
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
	public void onChanged(Change<? extends IDataFile> change) {

		while (change.next()) {
			List<? extends IDataFile> addedSubList = change.getAddedSubList();
			for (IDataFile file : addedSubList) {
				fileTabs.getTabs().add(createTab(file.getFile()));
			}
		}
	}
}
