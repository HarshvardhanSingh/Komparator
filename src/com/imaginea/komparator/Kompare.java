package com.imaginea.komparator;

import com.imaginea.komparator.engine.KomparatorEngine;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;

public class Kompare
	{
	/**
	 * @param args
	 */
	public static void main(String[] args)
		{
		try
			{
			Class.forName("com.imaginea.komparator.xmltype1.parser.XMLType1Parser");
			}
		catch (ClassNotFoundException e)
			{
			e.printStackTrace();
			}

		KomparatorParser parser = KomparatorManager.getParser();

		KomparatorNode tree1 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\ComparisonFiles\\Sample1.xml");
		KomparatorNode tree2 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\ComparisonFiles\\Sample2.xml");
		// Compare the two files.
		KomparatorEngine engine = new KomparatorEngine();
		engine.compareDocuments(tree1, tree2);
		}

	}
