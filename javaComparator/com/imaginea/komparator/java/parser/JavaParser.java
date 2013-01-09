package com.imaginea.komparator.java.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

public class JavaParser implements KomparatorParser
	{
	Logger logger = LoggerFactory.getLogger(JavaParser.class);
	boolean isParserSetup = false;
	
	static
		{
		Logger logger = LoggerFactory.getLogger(JavaParser.class);
		// Register itself as the parser.
		KomparatorManager.setParser(new JavaParser());

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
			JavaFiniteStateAutomaton javaFiniteStateAutomaton = new JavaFiniteStateAutomaton(new FileInputStream(documentPath));
			}
		catch (IOException ioException)
			{
			logger.error("An error occured while reading the input document {}.", documentPath, ioException);
			}
		return null;
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
