package com.imaginea.komparator.interfaces.nodes;

import java.util.List;

public interface KomparatorNode
	{
	public int getRuleId();
	public String getName();
	public List<KomparatorAttribute> getAttributes();
	public List<KomparatorNode> getChildren();
	
	/*// So that it is imperative for the node to implement hascode method.
	public int hashcode();
	
	// So that it is imperative for the node to implement equals method.
	public boolean equals(Object object);*/
	}
