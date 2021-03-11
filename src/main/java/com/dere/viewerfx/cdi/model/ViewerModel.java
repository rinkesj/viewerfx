package com.dere.viewerfx.cdi.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import com.dere.viewerfx.api.IDataFile;
import com.dere.viewerfx.api.IDataRecord;
import com.dere.viewerfx.parser.DelimiterFileParser;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

@ApplicationScoped
public class ViewerModel {
	
	private ObservableList<IDataFile> files = FXCollections.observableList(new ArrayList<IDataFile>());
	private ObservableSet<String> allColumns = FXCollections.observableSet(new HashSet<String>());
	private ObservableSet<String> viewColumns = FXCollections.observableSet(new HashSet<String>());
	private ObservableList<IDataRecord> records = FXCollections.observableList(new ArrayList<IDataRecord>());
	private File selectedFile = null;
	DelimiterFileParser parser = new DelimiterFileParser(); // TODO replace with factory with config
	
	String[] columns = new String[] {"Col0","Col1","Col2","Col3","Col4"};
	
//	private static ViewerModel INSTANCE;
	
	public ViewerModel() {
//		INSTANCE = this;
		allColumns.addAll(Stream.of(columns).collect(Collectors.toList()));
		viewColumns.addAll(Stream.of(columns).collect(Collectors.toList()));
	}
	
	public ViewerModel(List<File> files) {
		addFile(files);
//		INSTANCE = this;
	}
	
//	public static ViewerModel getInstance() {
//		return INSTANCE;
//	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
	
	public List<IDataFile> getFiles() {
		return files;
	}
	
	public void addFile(File file) {
		IDataFile parseFile = parser.parseFile(file);
		records.addAll(parseFile.getRecords());
		this.files.add(parseFile);
	}
	
	public void addFile(List<File> files) {
		for (File file : files) {
			addFile(file);
		}
	}
	
	public void registerListener(ListChangeListener<IDataFile> listener) {
		files.addListener(listener);
	}
	
	public ObservableSet<String> getAllColumns() {
		return allColumns;
	}
	
	public ObservableSet<String> getViewColumns() {
		return viewColumns;
	}
	
	public ObservableList<IDataRecord> getRecords() {
		return records;
	}
}
