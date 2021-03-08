package com.dere.viewerfx.view;

import com.dere.viewerfx.parser.IDataRecord;

import javafx.scene.Node;

public interface IContentView {
	
	String type();
	Node getNode(IDataRecord record);
}
