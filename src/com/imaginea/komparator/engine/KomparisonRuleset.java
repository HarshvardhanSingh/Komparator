package com.imaginea.komparator.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class KomparisonRuleset
	{
	Map<Integer, Map<String, Boolean>> nodeRule;
	public KomparisonRuleset(String rulesetFileName)
		{
		nodeRule = new HashMap<Integer, Map<String,Boolean>>();
		Logger logger = LoggerFactory.getLogger(this.getClass());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		InputStream  inputStream = null;
		try
			{
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(rulesetFileName);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inputStream);
			Element root = dom.getDocumentElement();
			logger.debug("Root node is: {}",root.getNodeName());
			NodeList nodes = root.getChildNodes();
			for(int nodeCounter = 0; nodeCounter < nodes.getLength(); nodeCounter++)
				{
				Node node = nodes.item(nodeCounter);
				logger.debug("Reading node: {}", node.getNodeName());
				if("rule".equalsIgnoreCase(node.getNodeName()))
					{
					NamedNodeMap namedNodeMap = node.getAttributes();
					for(int attributeCounter = 0; attributeCounter < namedNodeMap.getLength(); attributeCounter++)
						{
						Node attribute = namedNodeMap.item(attributeCounter);
						logger.debug("Reading attribute: {}", attribute.getNodeName());
						}					
					}			
				}
			}
		catch (ParserConfigurationException parserConfigurationException)
			{
			logger.error("Unable to parse file {}.",rulesetFileName, parserConfigurationException);
			}
		catch (SAXException saxException)
			{
			logger.error("Unable to parse file {}.",rulesetFileName, saxException);
			}
		catch (IOException ioException)
			{
			logger.error("Unable to read file {}.",rulesetFileName, ioException);
			}
		finally
		{
		if(inputStream != null)
			{
			try
				{
				inputStream.close();
				}
			catch (IOException ioException2)
				{
				logger.warn("Unable to close the input stream for file {}.",rulesetFileName, ioException2);
				}
			}
		}
		}
	}
