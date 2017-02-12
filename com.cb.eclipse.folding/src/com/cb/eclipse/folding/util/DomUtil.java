/*
 * Created on Sep 9, 2004
 */
package com.cb.eclipse.folding.util;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Happy fun XML dom coding.
 * @author R.J. Lorimer
 */
public final class DomUtil {

	private DomUtil() { }
	
	public static boolean getBooleanValue(NamedNodeMap attributes, String attribute) throws SAXException {
		
		Node enabledNode= attributes.getNamedItem(attribute);
		if (enabledNode == null)
			throw new SAXException("Attribute " + attribute + " not available.");
		else if (enabledNode.getNodeValue().equals(Boolean.toString(true)))
			return true;
		else if (enabledNode.getNodeValue().equals(Boolean.toString(false)))
			return false;
		else
			throw new SAXException("Unable to parse " + attribute + " into boolean");
		
	}
	
	public static boolean getBooleanValue(NamedNodeMap attributes, String attribute, boolean defaultValue) throws SAXException {
		Node enabledNode= attributes.getNamedItem(attribute);
		if (enabledNode == null)
			return defaultValue;
		else if (enabledNode.getNodeValue().equals(Boolean.toString(true)))
			return true;
		else if (enabledNode.getNodeValue().equals(Boolean.toString(false)))
			return false;
		else
			throw new SAXException("Unable to parse " + attribute + " into boolean");
	}
	
	public static String getStringValue(NamedNodeMap attributes, String name) throws SAXException {
		String val= getStringValue(attributes, name, null);
		if (val == null)
			throw new SAXException("Unable to get value for " + name + " attribute.");
		return val;
	}
	
	
	public static String getStringValue(NamedNodeMap attributes, String name, String defaultValue) {
		Node node= attributes.getNamedItem(name);
		return node == null	? defaultValue : node.getNodeValue();
	}

}
