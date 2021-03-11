package com.dere.viewerfx.api;

import javafx.scene.Node;

public interface IContentView {
	
	String type();
	Node getNode(IDataRecord record);
}
