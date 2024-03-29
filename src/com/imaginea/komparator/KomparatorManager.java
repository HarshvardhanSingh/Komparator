package com.imaginea.komparator;

import com.imaginea.komparator.engine.KomparisonRuleset;
import com.imaginea.komparator.interfaces.parser.KomparatorParser;

public class KomparatorManager
	{
	private static KomparatorParser parser;
	private static KomparisonRuleset ruleset;

	/**
	 * Register a parser to be used.
	 * 
	 * @param parser
	 *            The parser to be used to parse the input files.
	 */
	public static void setParser(KomparatorParser parser)
		{
		KomparatorManager.parser = parser;
		}

	public static KomparatorParser getParser()
		{
		return parser;
		}

	public static KomparisonRuleset getRuleset()
		{
		return ruleset;
		}

	public static void setRuleset(KomparisonRuleset ruleset)
		{
		KomparatorManager.ruleset = ruleset;
		}

	}
