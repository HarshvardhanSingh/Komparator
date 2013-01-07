package com.imaginea.komparator.xmltype1.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.imaginea.komparator.KomparatorManager;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;
import com.imaginea.komparator.util.KomparatorDOMParser;
import com.imaginea.komparator.xmltype1.node.XMLType1Attribute;
import com.imaginea.komparator.xmltype1.node.XMLType1Node;

public class XMLType1Parser implements KomparatorParser
	{
	Logger logger = LoggerFactory.getLogger(XMLType1Parser.class);

	static
		{
		Logger logger = LoggerFactory.getLogger(XMLType1Parser.class);
		// Register itself as the parser.
		KomparatorManager.setParser(new XMLType1Parser());
		logger.info("Registered XMLType1Parser parser.");
		}

	public KomparatorNode parseDocument(String documentPath)
		{
		logger.info("Parsing {} file into tree.", documentPath);

		try
			{
			Element root = KomparatorDOMParser.parseDOM(new FileInputStream(documentPath));
			KomparatorNode komparatorRoot = parseDomTree(root);
			}
		catch (FileNotFoundException fileNotFoundException)
			{
			logger.error("Unable to parse the ruleset file {} as it could not be read.", documentPath, fileNotFoundException);
			}

		return null;
		}

	public XMLType1Node parseDomTree(Node node)
		{
		XMLType1Node xmlNode = new XMLType1Node();
		// Fetch the node data into the xmlNode.
		String nodeName = node.getNodeName().intern();
		xmlNode.setName(nodeName);
		xmlNode.setRuleId(getRuleForNode(nodeName));
		
		// If the rule id was -1, this means the rule was not found. Hence we neglect the node.
		if (xmlNode.getRuleId() == -1)
			{
			return null;
			}
		String value = "";
		if (isDataNode(nodeName))
			{
			value = node.getTextContent();
			xmlNode.getAttributes().add(new XMLType1Attribute("value", value));
			}
		logger.debug("Obtained rule id '{}' for node '{}' with value '{}'.", xmlNode.getRuleId(), xmlNode.getName(), value);

		

		// Create attributes for the node.
		for (int attributeCounter = 0; attributeCounter < node.getAttributes().getLength(); attributeCounter++)
			{
			Node attribute = node.getAttributes().item(attributeCounter);
			XMLType1Attribute xmlAttribute = new XMLType1Attribute();
			xmlAttribute.setName(attribute.getNodeName());
			xmlAttribute.setValue(attribute.getNodeValue());
			xmlNode.getAttributes().add(xmlAttribute);
			logger.debug("Added an attribute '{}' with value '{}' to the node '{}'.", xmlAttribute.getName(), xmlAttribute.getValue(), xmlNode.getName());
			}

		// Create children for the node
		for (int childrenCounter = 0; childrenCounter < node.getChildNodes().getLength(); childrenCounter++)
			{
			Node childNode = node.getChildNodes().item(childrenCounter);
			XMLType1Node xmlChild = parseDomTree(childNode);
			if (xmlChild != null)
				{
				xmlNode.getChildren().add(xmlChild);
				}
			}
		return xmlNode;
		}

	/**
	 * Returns a node's rule based on the node name. Please make sure the node name is interned string.
	 * 
	 * @param nodeName
	 *            The interned string for node name.
	 * @return The rule id for the given node. Returns -1 if no rule was found for the mentioned node name.
	 */
	private int getRuleForNode(String nodeName)
		{
		if ("employee-information" == nodeName)
			{
			return 1;
			}
		if ("company" == nodeName)
			{
			return 2;
			}
		if ("project" == nodeName)
			{
			return 8;
			}
		if ("employees" == nodeName)
			{
			return 3;
			}
		if ("employee" == nodeName)
			{
			return 4;
			}
		if ("name" == nodeName)
			{
			return 5;
			}
		if ("designation" == nodeName)
			{
			return 6;
			}
		if ("email" == nodeName)
			{
			return 7;
			}
		return -1;
		}

	/**
	 * Finds out if the node mentioned may contain data that should be read as node.getTextContent().
	 * 
	 * @param nodeName
	 *            This must be an interned string.
	 * @return true if the node may contain text data. false otherwise.
	 */
	private boolean isDataNode(String nodeName)
		{
		if ("name" == nodeName || "designation" == nodeName || "email" == nodeName)
			{
			return true;
			}
		return false;
		}

	}
