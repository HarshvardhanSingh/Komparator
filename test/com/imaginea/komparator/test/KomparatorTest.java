package com.imaginea.komparator.test;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.imaginea.komparator.KomparatorManager;
import com.imaginea.komparator.engine.KomparatorEngine;
import com.imaginea.komparator.engine.Komparison;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;

public class KomparatorTest
	{
	@BeforeClass
	public static void testSetup()
		{
		try
			{
			Class.forName("com.imaginea.komparator.xmltype1.parser.XMLType1Parser");
			}
		catch (ClassNotFoundException e)
			{
			e.printStackTrace();
			}
		}

	@AfterClass
	public static void testCleanup()
		{
		// Teardown for data used by the unit tests
		}

	@Test
	public void test()
		{
		KomparatorParser parser = KomparatorManager.getParser();

		KomparatorNode tree1 = null;
		KomparatorNode tree2 = null;
		try
			{
			tree1 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\test\\TestFile1.xml");
			tree2 = parser.parseDocument("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\test\\TestFile2.xml");
			}
		catch (FileNotFoundException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		KomparatorEngine engine = new KomparatorEngine();
		List<Komparison> comparisons = engine.compareDocuments(tree1, tree2);
		for (Komparison comparison : comparisons)
			{
			System.out.println(comparison.toString());
			}
		}
	}
