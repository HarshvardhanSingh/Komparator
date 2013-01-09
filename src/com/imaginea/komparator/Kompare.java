package com.imaginea.komparator;

import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.komparator.engine.KomparatorEngine;
import com.imaginea.komparator.engine.Komparison;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;

public class Kompare
	{
	/**
	 * @param args
	 */
	public static void main(String[] args)
		{
		Logger logger = LoggerFactory.getLogger(Kompare.class);
		try
			{
			Class.forName("com.imaginea.komparator.xmltype1.parser.XMLType1Parser");
			}
		catch (ClassNotFoundException e)
			{
			e.printStackTrace();
			}

		KomparatorParser parser = KomparatorManager.getParser();
		KomparatorNode tree1 = null;
		KomparatorNode tree2 = null;
		try
			{
			parser.setupParser("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\test\\TestRuleSet.xml");
			tree1 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\test\\TestFile1.xml");
			tree2 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\test\\TestFile2.xml");
			
//			parser.setupParser("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\XMLType1\\XML1RuleSet.xml");
//			tree1 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\ComparisonFiles\\Sample1.xml");
//			tree2 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\ComparisonFiles\\Sample3.xml");
			}
		catch (FileNotFoundException e)
			{
			logger.error("File not found.", e);
			}
		// Compare the two files.
		KomparatorEngine engine = new KomparatorEngine();
		List<Komparison> comparisons = engine.compareDocuments(tree1, tree2);
		for (Komparison comparison : comparisons)
			{
			logger.error(comparison.toString());
			}
		}

	}
