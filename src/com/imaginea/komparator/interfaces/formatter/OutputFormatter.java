package com.imaginea.komparator.interfaces.formatter;

import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

public interface OutputFormatter
	{
	public String formatOutput(KomparatorNode node1, KomparatorNode node2, String mismatchIdentifier);
	}
