package com.imaginea.komparator;

import com.imaginea.komparator.interfaces.parser.KomparatorParser;

public class KomparatorManager
	{
	private static KomparatorParser parser;

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
	}
