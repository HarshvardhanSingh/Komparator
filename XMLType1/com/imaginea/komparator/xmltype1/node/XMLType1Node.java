package com.imaginea.komparator.xmltype1.node;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

/**
 * Represents a node in the komparator tree that shall be compared. This is a node from XMLType1 XML.
 * 
 * @author harshavardhansingh
 * 
 */
public class XMLType1Node implements KomparatorNode
	{
	private String name;
	private int ruleId;
	private List<KomparatorAttribute> attributes = new ArrayList<KomparatorAttribute>();
	private List<KomparatorNode> children = new ArrayList<KomparatorNode>();

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public int getRuleId()
		{
		return ruleId;
		}

	public void setRuleId(int ruleId)
		{
		this.ruleId = ruleId;
		}

	public List<KomparatorAttribute> getAttributes()
		{
		return attributes;
		}

	public void setAttributes(List<KomparatorAttribute> attributes)
		{
		this.attributes = attributes;
		}

	public List<KomparatorNode> getChildren()
		{
		return children;
		}

	public void setChildren(List<KomparatorNode> children)
		{
		this.children = children;
		}

	}
