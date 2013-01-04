package com.imaginea.komparator.engine;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

public class KomparatorEngine
	{
	/**
	 * Compares the document trees that have been created by their respective parsers. The comparison is done based on
	 * the ruleset provided by the comparison framework registered.
	 * 
	 * @param documentTree1
	 *            The tree1 that needs to be compared.
	 * @param documentTree2
	 *            The tree2 that needs to be compared.
	 * @param ruleset
	 *            The ruleset that needs to be used to compare the documents.
	 * @return A list of differences as Komparison objects.
	 */
	public List<Komparison> compareDocuments(KomparatorNode documentTree1, KomparatorNode documentTree2, KomparisonRuleset ruleset)
		{
		List<Komparison> comparisons = new ArrayList<Komparison>();

		// Comparison logic goes here.
		
		
		return comparisons;
		}

	}
