package com.imaginea.komparator.interfaces.parser;

import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

/**
 * Represents the structure of class that shall parse the input file and represent it in a node format.
 * 
 * @author harshavardhansingh
 */
public interface KomparatorParser
	{
	public KomparatorNode parseDocument(String documentPath);
	}
