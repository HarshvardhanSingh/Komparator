package com.imaginea.komparator.engine;

/**
 * Represents a comparator rule.
 * 
 * @author harshavardhansingh
 * 
 */
public class KomparatorRule
	{
	/**
	 * Id of the rule.
	 */
	private int id;
	/**
	 * Name of the rule.
	 */
	private String name;
	private boolean needsOrder;
	private boolean needAttributesORder;
	private boolean allAttributesRequired;
	private boolean allAttributesMatch;
	private boolean childrenOrdering;
	private boolean childrenRequired;

	public int getId()
		{
		return id;
		}

	public void setId(int id)
		{
		this.id = id;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public boolean isNeedsOrder()
		{
		return needsOrder;
		}

	public void setNeedsOrder(boolean needsOrder)
		{
		this.needsOrder = needsOrder;
		}

	public boolean isNeedAttributesORder()
		{
		return needAttributesORder;
		}

	public void setNeedAttributesORder(boolean needAttributesORder)
		{
		this.needAttributesORder = needAttributesORder;
		}

	public boolean isAllAttributesRequired()
		{
		return allAttributesRequired;
		}

	public void setAllAttributesRequired(boolean allAttributesRequired)
		{
		this.allAttributesRequired = allAttributesRequired;
		}

	public boolean isAllAttributesMatch()
		{
		return allAttributesMatch;
		}

	public void setAllAttributesMatch(boolean allAttributesMatch)
		{
		this.allAttributesMatch = allAttributesMatch;
		}

	public boolean isChildrenOrdering()
		{
		return childrenOrdering;
		}

	public void setChildrenOrdering(boolean childrenOrdering)
		{
		this.childrenOrdering = childrenOrdering;
		}

	public boolean isChildrenRequired()
		{
		return childrenRequired;
		}

	public void setChildrenRequired(boolean childrenRequired)
		{
		this.childrenRequired = childrenRequired;
		}
	}
