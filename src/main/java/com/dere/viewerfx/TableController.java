package com.dere.viewerfx;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.proxy.WeldClientProxy;

import com.dere.viewerfx.api.IDataFile;
import com.dere.viewerfx.api.IDataRecord;
import com.dere.viewerfx.cdi.EventSelectionRecord;
import com.dere.viewerfx.cdi.model.ViewerModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

@Singleton
public class TableController implements ListChangeListener<IDataFile> {

	@FXML
	TableView<IDataRecord> table;

	@Inject
	private ViewerModel model;

	@Inject
	@EventSelectionRecord
	Event<IDataRecord> selectRecordEvent;
	
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

		table.getColumns().addAll(createColumn());
		table.setItems(model.getRecords());
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<IDataRecord>() {

			@Override
			public void onChanged(Change<? extends IDataRecord> c) {
				IDataRecord selectedItem = (IDataRecord) TableController.this.table.getSelectionModel().getSelectedItem();
				System.out.println("TABLE SELECTION " + selectedItem.getContentType());
				selectRecordEvent.fire(selectedItem);
			}

		});
		model.registerListener(this);
	}

	private Collection<TableColumn<IDataRecord, String>> createColumn() {

		List<TableColumn<IDataRecord, String>> viewColumns = model.getViewColumns().stream()
				.map(s -> new TableColumn<IDataRecord, String>(s)).collect(Collectors.toList());

		for (TableColumn<IDataRecord, String> tableColumn : viewColumns) {
			tableColumn.setCellValueFactory(
					new Callback<CellDataFeatures<IDataRecord, String>, ObservableValue<String>>() {
						public SimpleStringProperty call(CellDataFeatures<IDataRecord, String> record) {
							return new SimpleStringProperty((String) record.getValue().getColumnValue(
									record.getTableView().getColumns().indexOf(record.getTableColumn())));
						}
					});
		}

		return viewColumns;
	}

	@Override
	public void onChanged(Change<? extends IDataFile> c) {
		// TODO Auto-generated method stub
	}

}
