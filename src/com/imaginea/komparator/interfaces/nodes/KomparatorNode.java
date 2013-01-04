package com.imaginea.komparator.interfaces.nodes;

import com.imaginea.komparator.interfaces.nodes.types.KomparatorNodeType;

public interface KomparatorNode
	{
	public int getNodeId();
	public String getNodeName();
	public KomparatorNodeType getNodeType();
	
	
	// So that it is imperative for the node to implement hascode method.
	public int hashcode();
	
	// So that it is imperative for the node to implement equals method.
	public boolean equals(Object object);
	}
