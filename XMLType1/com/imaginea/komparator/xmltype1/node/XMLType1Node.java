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
	private String differentiatorValue;

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

	public String getDifferentiatorValue()
		{
		return differentiatorValue;
		}

	public void setDifferentiatorValue(String differentiatorValue)
		{
		this.differentiatorValue = differentiatorValue;
		}

	public int compareTo(KomparatorNode o)
		{
		if (!(o instanceof XMLType1Node))
			{
			return -1;
			}
		XMLType1Node secondNode = (XMLType1Node) o;
		int comparison = name.compareTo(secondNode.getName());
		if (comparison == 0)
			{
			// The node name is same. Now we need to check the differentiator.
			if (differentiatorValue != null)
				{
				comparison = differentiatorValue.compareTo(secondNode.getDifferentiatorValue());
				}
			}
		return comparison;
		}

	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append("Name: ");
		builder.append(name);
		builder.append(" | Differentiator Value: ");
		builder.append(differentiatorValue);
		builder.append(" | Rule Id: ");
		builder.append(ruleId);
		return builder.toString();
		}
	}
