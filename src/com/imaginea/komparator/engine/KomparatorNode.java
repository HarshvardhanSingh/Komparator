package com.imaginea.komparator.engine;

import java.util.List;

/**
 * Represents a node in the komparator tree that shall be compared.
 * 
 * @author harshavardhansingh
 * 
 */
public class KomparatorNode
	{
	/**
	 * The id of the rule applicable to this node. This matches the resultset.
	 */
	private int ruleId;
	/**
	 * The name of the node. It is used only for the output formatting purposes.
	 */
	private String name;
	/**
	 * The attributes to a komparator node.
	 */
	private List<KomparatorAttribute> attributes;

	public int getRuleId()
		{
		return ruleId;
		}

	public void setRuleId(int ruleId)
		{
		this.ruleId = ruleId;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public List<KomparatorAttribute> getAttributes()
		{
		return attributes;
		}

	public void setAttributes(List<KomparatorAttribute> attributes)
		{
		this.attributes = attributes;
		}

	}
