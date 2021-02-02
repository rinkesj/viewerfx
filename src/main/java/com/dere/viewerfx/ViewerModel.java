package com.dere.viewerfx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ViewerModel {
	
	private ObservableList<File> files = FXCollections.observableList(new ArrayList<File>());
	private File selectedFile = null;
	
	private static ViewerModel INSTANCE;
	
	public ViewerModel() {
		INSTANCE = this;
	}
	
	public ViewerModel(List<File> files) {
		this.files.addAll(files);
		INSTANCE = this;
	}
	
	public static ViewerModel getInstance() {
		return INSTANCE;
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
	
	public List<File> getFiles() {
		return files;
	}
	
	public void addFile(File file) {
		this.files.add(file);
	}
	
	public void addFile(List<File> files) {
		this.files.addAll(files);
	}
	
	public void registerListener(ListChangeListener<File> listener) {
		files.addListener(listener);
	}
}
