package com.dere.viewerfx.formatter;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

// TODO this is maybe not ideal as it has to be valid xml somehow and it is parsed. Need better formatter.
public class XMLDataContentFormatter implements IDataContentFormatter {

	@Override
	public String type() {
		return "xml";
	}

	@Override
	public String format(Object content) {
		try {
			Source xmlInput = new StreamSource(new StringReader((String) content));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 4);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			transformer.transform(xmlInput, xmlOutput);
			String xmlFormatted = xmlOutput.getWriter().toString();
			return xmlFormatted;
		} catch (Exception e) {
			throw new RuntimeException(e); // simple exception handling, please review it
		}
	}

}
