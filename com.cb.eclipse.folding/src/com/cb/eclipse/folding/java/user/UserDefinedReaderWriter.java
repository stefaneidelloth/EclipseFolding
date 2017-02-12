/*
 * Created on Sep 9, 2004
 */
package com.cb.eclipse.folding.java.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cb.eclipse.folding.util.DomUtil;

/**
 * Reads and writes the user defined regions preferences.
 * 
 * @author R.J. Lorimer
 */
public class UserDefinedReaderWriter {

	private static final String ROOT = "regions";
	private static final String REGION = "region";
	private static final String NAME_ATTR = "name";
	private static final String IS_FOLD_ATTR = "fold";
	private static final String IS_COLLAPSE_ATTR = "collapse";

	public Collection read(InputStream in) throws IOException {
		return read(new InputSource(in));
	}

	public Collection read(Reader in) throws IOException {
		return read(new InputSource(in));
	}

	public void write(OutputStream out, Collection entries) throws IOException {
		write(new StreamResult(out), entries);
	}

	public void write(Writer out, Collection entries) throws IOException {
		write(new StreamResult(out), entries);
	}

	private void write(StreamResult result, Collection entries) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Node root = document.createElement(ROOT);
			document.appendChild(root);
			
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				UserDefinedEntry entry = (UserDefinedEntry) iter.next();
				String name = entry.getName();
				boolean isFold = entry.isFold();
				boolean isCollapse = entry.isCollapse();
				Node region = document.createElement(REGION);
				root.appendChild(region);
				NamedNodeMap attrs = region.getAttributes();
				Attr nameAttr = document.createAttribute(NAME_ATTR);
				nameAttr.setValue(entry.getName());
				Attr foldAttr = document.createAttribute(IS_FOLD_ATTR);
				foldAttr.setValue(String.valueOf(isFold));
				Attr collapseAttr = document.createAttribute(IS_COLLAPSE_ATTR);
				collapseAttr.setValue(String.valueOf(isCollapse));
				attrs.setNamedItem(nameAttr);
				attrs.setNamedItem(foldAttr);
				attrs.setNamedItem(collapseAttr);
				
			}
						

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			DOMSource source = new DOMSource(document);

			transformer.transform(source, result);

		}
		catch (ParserConfigurationException e) {
			throw new IOException("Unable to build document for saving user defined settings.");
		}
		catch (TransformerException e) {
			if (e.getException() instanceof IOException) throw (IOException) e.getException();
			else throw new IOException("Unable to build document for saving user defined settings.");
		}

	}

	private Collection read(InputSource in) throws IOException {
		try {
			List results = new ArrayList();
			DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			DocumentBuilder parser= factory.newDocumentBuilder();		
			Document document= parser.parse(in);
		
			NodeList elements= document.getElementsByTagName(REGION);
			int count= elements.getLength();
			for (int i= 0; i != count; i++) {
				Node node= elements.item(i);					
				NamedNodeMap attributes= node.getAttributes();
				String name = DomUtil.getStringValue(attributes, NAME_ATTR);
				boolean isFold = DomUtil.getBooleanValue(attributes, IS_FOLD_ATTR);
				boolean isCollapse = DomUtil.getBooleanValue(attributes, IS_COLLAPSE_ATTR);
				UserDefinedEntry entry = new UserDefinedEntry();
				entry.setName(name);
				entry.setFold(isFold);
				entry.setCollapse(isCollapse);
				results.add(entry);
			}
			return results;
		}
		catch(SAXException e) {
			e.printStackTrace();
			throw new IOException("Unable to read user defined properties.");
		}
		catch(ParserConfigurationException e) {
			throw new IOException("Unable to read user defined properties.");	
		}
	}

	// <regions><region name="..." fold="true" collapse="true" /></regions>

}