package com.imaginea.komparator.xmltype1.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.imaginea.komparator.KomparatorManager;
import com.imaginea.komparator.engine.KomparisonRuleset;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;
import com.imaginea.komparator.util.KomparatorDOMParser;
import com.imaginea.komparator.xmltype1.node.XMLType1Attribute;
import com.imaginea.komparator.xmltype1.node.XMLType1Node;

public class XMLType1Parser implements KomparatorParser
	{
	Logger logger = LoggerFactory.getLogger(XMLType1Parser.class);
	boolean isParserSetup = false;

	static
		{
		Logger logger = LoggerFactory.getLogger(XMLType1Parser.class);
		// Register itself as the parser.
		KomparatorManager.setParser(new XMLType1Parser());

		logger.info("Registered XMLType1Parser parser.");
		}
	
	public void setupParser(String rulesetFile) throws FileNotFoundException
		{
		// Create the ruleset based on the rules file.
		KomparisonRuleset ruleset = new KomparisonRuleset(new FileInputStream(rulesetFile));
		KomparatorManager.setRuleset(ruleset);
		isParserSetup = true;
		}

	public KomparatorNode parseDocument(String documentPath) throws FileNotFoundException
		{
		logger.info("Parsing {} file into tree.", documentPath);
		if(! isParserSetup)
			{
			logger.error("Parser is not setup yet. Invoke setupParser before invoking this method.");
			return null;
			}
		try
			{
			Element root = KomparatorDOMParser.parseDOM(new FileInputStream(documentPath));
			return parseDomTree(root);
			}
		catch (FileNotFoundException fileNotFoundException)
			{
			logger.error("Unable to parse the ruleset file {} as it could not be read.", documentPath, fileNotFoundException);
			throw fileNotFoundException;
			}
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
			// Setting node value as an attribute.
			value = node.getTextContent();
			xmlNode.getAttributes().add(new XMLType1Attribute("value", value));
			}
		logger.debug("Obtained rule id '{}' for node '{}' with value '{}'.", xmlNode.getRuleId(), xmlNode.getName(), value);

		// Create attributes for the node.
		for (int attributeCounter = 0; attributeCounter < node.getAttributes().getLength(); attributeCounter++)
			{
			Node attribute = node.getAttributes().item(attributeCounter);
			String attributeName = attribute.getNodeName().intern();
			String attributeValue = attribute.getNodeValue();
			XMLType1Attribute xmlAttribute = new XMLType1Attribute();
			xmlAttribute.setName(attributeName);
			xmlAttribute.setValue(attributeValue);
			xmlNode.getAttributes().add(xmlAttribute);
			if(attributeName == getDifferentiatingAttributeForNode(nodeName))
				{
				// The return value is interned, hence we can do this comparison.
				xmlNode.setDifferentiatorValue(attributeValue);
				}
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
			return 6;
			}
		if ("employees" == nodeName)
			{
			return 3;
			}
		if ("employee" == nodeName)
			{
			return 4;
			}
		if ("info" == nodeName)
			{
			return 5;
			}
		return -1;
		}

	/**
	 * Fetches the differentiating attribute for a given node.
	 * 
	 * @param nodeName
	 *            The name must be an interned string.
	 * @return The attribute name that contains the differentiating value.
	 */
	private String getDifferentiatingAttributeForNode(String nodeName)
		{
		if ("employee-information" == nodeName || "company" == nodeName || "employees" == nodeName || "info" == nodeName || "project" == nodeName)
			{
			return "name";
			}
		if ("employee" == nodeName)
			{
			return "id";
			}
		return null;
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
		if ("info" == nodeName)
			{
			return true;
			}
		return false;
		}

	}
