package com.dere.viewerfx.view;

import java.util.Map;

import com.dere.viewerfx.parser.IDataRecord;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class JsonContentTreeView implements IContentView {

	private static final boolean EXPANDED = true;

	@Override
	public String type() {
		return "json";
	}

	@Override
	public Node getNode(IDataRecord record) {
		TreeItem<String> item = getJsonTree((String) record.getContent());
		TreeView<String> treeView = new TreeView<>(item);

		return treeView;
	}

	private TreeItem<String> getJsonTree(String content) {

		TreeItem<String> root = new TreeItem<String>();
		root.setExpanded(EXPANDED);
		JsonElement json = JsonParser.parseString(content);
		getTreeItem(root, json);

		return root;
	}

	private void getTreeItem(TreeItem<String> parent, JsonElement element) {
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
//			      if (primitive != null && primitive.isString()) {
			parent.setValue(parent.getValue() + " : " + primitive.getAsString());
//			      } else {
//			    	  return new TreeItem(primitive.getAsString());
//			      }
		} else if (element.isJsonArray()) {
			JsonArray jsonArray = element.getAsJsonArray();
			
			if(jsonArray.size() == 0) {
				TreeItem<String> empty = new TreeItem<String>();
				parent.getChildren().add(empty);
			}
			
			for (JsonElement jsonElement : jsonArray) {
				getTreeItem(parent, jsonElement);
			}
		} else if (element.isJsonNull()) {
			TreeItem<String> nullType = new TreeItem<String>("null");
			nullType.setExpanded(EXPANDED);
			parent.getChildren().add(nullType);
		} else {
			JsonObject obj = element.getAsJsonObject();
			
			if(obj.entrySet().size() == 0) {
				TreeItem<String> empty = new TreeItem<String>();
				parent.getChildren().add(empty);
			}
			
			for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
				TreeItem<String> elementItem = new TreeItem<String>(entry.getKey());
				getTreeItem(elementItem, entry.getValue());
				elementItem.setExpanded(EXPANDED);
				parent.getChildren().add(elementItem);
			}
		}
	}

}
