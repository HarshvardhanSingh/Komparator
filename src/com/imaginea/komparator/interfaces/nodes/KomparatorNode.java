package com.imaginea.komparator.interfaces.nodes;

import java.util.List;

public interface KomparatorNode extends Comparable<KomparatorNode>
	{
	public int getRuleId();
	public String getName();
	public List<KomparatorAttribute> getAttributes();
	public List<KomparatorNode> getChildren();
	public String getDifferentiatorValue();
	public String toString();
	}
