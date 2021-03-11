package com.dere.viewerfx.view;

import java.io.IOException;
import java.io.StringReader;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.dere.viewerfx.api.IContentView;
import com.dere.viewerfx.api.IDataRecord;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

@ApplicationScoped
public class XmlContentTreeView implements IContentView {
	
	private boolean compactMode = false;

	@Override
	public String type() {
		return "xml";
	}

	@Override
	public Node getNode(IDataRecord record) {

		Object content = record.getContent();

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = parserFactory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			TreeItemCreationContentHandler contentHandler = new TreeItemCreationContentHandler();

			// parse file using the content handler to create a TreeItem representation
			reader.setContentHandler(contentHandler);
			InputSource is = new InputSource(new StringReader((String) content));
			reader.parse(is);

			// use first child as root (the TreeItem initially created does not contain data
			// from the file)
			TreeItem<String> item = contentHandler.item.getChildren().get(0);
			TreeView<String> treeView = new TreeView<>(item);

			return treeView;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class TreeItemCreationContentHandler extends DefaultHandler {

		private TreeItem<String> item = new TreeItem<>();

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			// finish this node by going back to the parent
			this.item = this.item.getParent();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			// start a new node and use it as the current item
			TreeItem<String> item = new TreeItem<>(qName);
			item.setExpanded(true);
			this.item.getChildren().add(item);
			this.item = item;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String s = String.valueOf(ch, start, length).trim();
			if (!s.isEmpty()) {
				// add text content as new child
				if (XmlContentTreeView.this.compactMode)
					this.item.setValue(this.item.getValue() + " : " + s);
				else {
					TreeItem<String> leaf = new TreeItem<>(s);
					leaf.setExpanded(true);
					item.getChildren().add(leaf);
				}
			}
		}
	}
}
