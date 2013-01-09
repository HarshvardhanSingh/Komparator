package com.imaginea.komparator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
	private List<String> errorList;
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
		errorList = new ArrayList<String>();
		errorList.add("ERROR - MISMATCHED_ATTRIBUTE_VALUE - The node (Name: company | Differentiator Value: Imaginea | Rule Id: 2) with attribute location has value Hyderabad. Where as the node (Name: company | Differentiator Value: Imaginea | Rule Id: 2) with attribute location has value Chennai");
		errorList.add("ERROR - MISSING_REQUIRED_ATTRIBUTE - The node (Name: company | Differentiator Value: Qontext | Rule Id: 2) with attribute location in file1 is missing from file2 ");
		errorList.add("ERROR - IMPROPER_NODE_ORDER - The node (Name: info | Differentiator Value: designation | Rule Id: 5) in file2 has a different order than the node in file1.");
		errorList.add("ERROR - MISSING_REQUIRED_NODE - There is no node in file2 for corresponding node (Name: employees | Differentiator Value: hrteam | Rule Id: 3) in file1.");
		KomparatorParser parser = KomparatorManager.getParser();

		KomparatorNode tree1 = null;
		KomparatorNode tree2 = null;
		try
			{
			parser.setupParser("E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\test\\TestRuleSet.xml");
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
		assertEquals("The length of expected error messages and actual error messages differ.", errorList.size(),  comparisons.size());
		for(int counter = 0; counter < comparisons.size(); counter++)
			{
			assertTrue("The error messages that should have matched at counter "+counter+" with value \n[" +  errorList.get(counter) + "]\n\t does not match with the comparison result \n[" + comparisons.get(counter).toString() + "].", errorList.get(counter).equals(comparisons.get(counter).toString()));
			}
		}
	}
