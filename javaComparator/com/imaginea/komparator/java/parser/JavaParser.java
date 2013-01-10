package com.imaginea.komparator.java.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.komparator.KomparatorManager;
import com.imaginea.komparator.engine.KomparisonRuleset;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;

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
			JavaLexemeParser lexemeParser = new JavaLexemeParser(documentPath);
			return lexemeParser.getTree();
			}
		catch (IOException ioException)
			{
			logger.error("An error occured while reading the input document {}.", documentPath, ioException);
			}
		return null;
		}
	}
