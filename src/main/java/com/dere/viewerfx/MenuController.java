package com.dere.viewerfx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.dere.viewerfx.formatter.FormatterFactory;
import com.dere.viewerfx.parser.IDataFile;
import com.dere.viewerfx.parser.IDataRecord;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
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
				
				//TODO i am ugly
				Tab obj = fileTabs.getTabs().stream().filter(t -> t.getId().equalsIgnoreCase(selectedItem.getDataFile().getFile().getAbsolutePath())).findFirst().get();
				fileTabs.getSelectionModel().select(obj);
				
				
				TextArea content = (TextArea) obj.getContent();
				int start = content.getText().indexOf((String) selectedItem.getContent());
				int end = start + ((String)selectedItem.getContent()).length();
				System.out.println("start" + start);
				System.out.println("end" + end);
				System.out.println(selectedItem.getStartIndex());
				System.out.println(selectedItem.getEndIndex());
				content.selectRange(selectedItem.getStartIndex(), selectedItem.getEndIndex());
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
		tab.setId(file.getAbsolutePath());
		String content = "";
		try {
			content = Files.readString(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		TextArea value = new TextArea(content);
		value.setOnMouseClicked(evt -> {
		    if (evt.getButton() == MouseButton.PRIMARY) {

		        // check, if click was inside the content area
		        Node n = evt.getPickResult().getIntersectedNode();
		        while (n != value) {
		            if (n.getStyleClass().contains("content")) {
		                // find previous/next line break
		            	
		            	//this is crap - we read file with cords. lets use it to detect caretPosition
		                int caretPosition = value.getCaretPosition();
		                String text = value.getText();
		                int lineBreak1 = text.lastIndexOf(System.lineSeparator(), caretPosition-1);
		                int lineBreak2 = text.indexOf(System.lineSeparator(), caretPosition);
		                if (lineBreak2 < 0) {
		                    // if no more line breaks are found, select to end of text
		                    lineBreak2 = text.length();
		                }
		                int start = lineBreak1 + 1;
		                int end = lineBreak2+1;
		                System.out.println("FileTab select " + (lineBreak1+1) + " x " + (lineBreak2+1));
		                IDataRecord record = ViewerModel.getInstance().getRecords().stream().filter(r->r.getDataFile().getFile().getAbsolutePath().equalsIgnoreCase(tab.getId())).filter(r->(r.getStartIndex() == start && r.getEndIndex() == end)).findFirst().get();
		                System.out.println(record);
		                
		                // when filetab line is selected then preselected table line should also change detail view - doesnt work
		                MenuController.this.tableView.getSelectionModel().select(record);
		                
//						((TextArea)((AnchorPane)tabPaneDetail.getTabs().get(0).getContent()).getChildren().get(0)).setText(formatFactory.getFormatter(record.getContentType()).format(record.getContent()));;
		                
		                value.selectRange(lineBreak1, lineBreak2);
		                evt.consume();
		                break;
		            }
		            n = n.getParent();
		        }
		    }
		});
//		value.setWrapText(true);
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
