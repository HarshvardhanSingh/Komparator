package com.imaginea.komparator.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class KomparatorDOMParser
	{
	/**
	 * Parses the DOM tree based on the inpur stream. The Element object (root node) of DOM is returned.
	 * 
	 * @param inputStream
	 *            The input stream that needs to be parsed. It will be closed once parsed.
	 * @return The Element object representing the DOM tree. It can be null if there was a problem in parsing the DOM
	 *         tree.
	 */
	public static Element parseDOM(InputStream inputStream)
		{
		Logger logger = LoggerFactory.getLogger(KomparatorDOMParser.class);

		Element element = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
			{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inputStream);
			element = dom.getDocumentElement();
			}
		catch (ParserConfigurationException parserConfigurationException)
			{
			logger.error("Unable to parse stream.", parserConfigurationException);
			}
		catch (SAXException saxException)
			{
			logger.error("Unable to parse stream.", saxException);
			}
		catch (IOException ioException)
			{
			logger.error("Unable to read stream.", ioException);
			}
		if (inputStream != null)
			{
			try
				{
				inputStream.close();
				}
			catch (IOException ioException2)
				{
				logger.warn("Unable to close the input stream.", ioException2);
				}
			}
		return element;
		}
	}
