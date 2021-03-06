package com.dere.viewerfx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.proxy.WeldClientProxy;

import com.dere.viewerfx.api.IContentView;
import com.dere.viewerfx.api.IDataFile;
import com.dere.viewerfx.api.IDataRecord;
import com.dere.viewerfx.cdi.EventSelectionRecord;
import com.dere.viewerfx.cdi.model.ViewerModel;
import com.dere.viewerfx.formatter.FormatterFactory;
import com.dere.viewerfx.view.ContentViewFactory;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import jfxtras.styles.jmetro.Style;

@Singleton
public class MainController implements ListChangeListener<IDataFile> {
	

	@FXML
	MenuBar menuBar;

	@FXML
	TabPane fileTabs;
	
	@FXML
	TabPane tabPaneDetail;
	
	@FXML
	AnchorPane tableViewAnchor;
	
	@Inject
	FormatterFactory formatFactory;
	
	@Inject
	private ContentViewFactory viewFactory;
	
	@Inject
	private ViewerModel model;
	
	
	public boolean isProxy(Object obj) {
	    try{
	        return Class.forName(WeldClientProxy.class.getName()).isInstance(obj);
	    } catch (Exception e) {
	        System.out.println("Unable to check if object is proxy");
	    }
	    return false;
	}

	
	@FXML
	public void initialize() throws IOException {
		System.out.println(isProxy(this));
		System.out.println(this.getClass().getSimpleName());
		System.out.println(this);
		System.out.println(this.getClass());
		
		List<IDataFile> files = model.getFiles();
		for (IDataFile file : files) {
			fileTabs.getTabs().add(createTab(file.getFile()));
		}
		
//		tableViewAnchor.getChildren().add(loadFXML("tableView"));

//		tableView.getColumns().addAll(createColumn());
//		tableView.setItems(model.getRecords());
//		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		tableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<IDataRecord>() {
//
//			@Override
//			public void onChanged(Change<? extends IDataRecord> c) {
//				
//				if(tabPaneDetail.getTabs().size() ==  3) {
//					tabPaneDetail.getTabs().remove(0); // remove custom view - FIX ME i am ugly
//				}
//				
//				IDataRecord selectedItem = (IDataRecord) MenuController.this.tableView.getSelectionModel().getSelectedItem();
//				TextArea formated = new TextArea(formatFactory.getByType(selectedItem.getContentType()).format(selectedItem.getContent()));
//				formated.setWrapText(true);
//				
//				TextArea source = new TextArea(selectedItem.getContent().toString());
//				source.setWrapText(true);
//				tabPaneDetail.getTabs().get(0).setContent(formated); // would be wise to use IDs
//				tabPaneDetail.getTabs().get(1).setContent(source); // would be wise to use IDs
//				
//				IContentView view = viewFactory.getByType(selectedItem.getContentType());
//				if(view != null) {
//					Node node = view.getNode(selectedItem);
//					if(node != null) {
//						Tab customViewTab = new Tab(selectedItem.getContentType());
//						customViewTab.setContent(node);
//						tabPaneDetail.getTabs().add(0, customViewTab);
//						tabPaneDetail.getSelectionModel().select(0);
//					}
//				}
//				
//				//TODO i am ugly
//				Tab obj = fileTabs.getTabs().stream().filter(t -> t.getId().equalsIgnoreCase(selectedItem.getDataFile().getFile().getAbsolutePath())).findFirst().get();
//				fileTabs.getSelectionModel().select(obj);
//				
//				
//				TextArea content = (TextArea) obj.getContent();
//				int start = content.getText().indexOf((String) selectedItem.getContent());
//				int end = start + ((String)selectedItem.getContent()).length();
//				content.selectRange(selectedItem.getEndIndex(),selectedItem.getStartIndex()); // select from end to start to keep- caret on start of line to avoid jump on end
//			}
//			
//		});
		model.registerListener(this);
		System.out.println(this);
		System.out.println(tabPaneDetail);
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
		                IDataRecord record = model.getRecords().stream().filter(r->r.getDataFile().getFile().getAbsolutePath().equalsIgnoreCase(tab.getId())).filter(r->(r.getStartIndex() == start && r.getEndIndex() == end)).findFirst().get();
		                
		                // when filetab line is selected then preselected table line should also change detail view - doesnt work
//		                MenuController.this.tableView.getSelectionModel().select(record);
		                
//						((TextArea)((AnchorPane)tabPaneDetail.getTabs().get(0).getContent()).getChildren().get(0)).setText(formatFactory.getFormatter(record.getContentType()).format(record.getContent()));;
		                
		                value.selectRange(lineBreak2, lineBreak1); // select from end to start to keep- caret on start of line to avoid jump on end
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
	
	public void onAnySelectionEvent(@Observes @EventSelectionRecord IDataRecord selectedItem) {
		System.out.println("hello cdi event " + selectedItem.getContentType());
		System.out.println(this);
		
		if(tabPaneDetail.getTabs().size() ==  3) {
			tabPaneDetail.getTabs().remove(0); // remove custom view - FIX ME i am ugly
		}
		
		TextArea formated = new TextArea(formatFactory.getByType(selectedItem.getContentType()).format(selectedItem.getContent()));
		formated.setWrapText(true);
		
		TextArea source = new TextArea(selectedItem.getContent().toString());
		source.setWrapText(true);
		tabPaneDetail.getTabs().get(0).setContent(formated); // would be wise to use IDs
		tabPaneDetail.getTabs().get(1).setContent(source); // would be wise to use IDs
		
		IContentView view = viewFactory.getByType(selectedItem.getContentType());
		if(view != null) {
			Node node = view.getNode(selectedItem);
			if(node != null) {
				Tab customViewTab = new Tab(selectedItem.getContentType());
				customViewTab.setContent(node);
				tabPaneDetail.getTabs().add(0, customViewTab);
				tabPaneDetail.getSelectionModel().select(0);
			}
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
