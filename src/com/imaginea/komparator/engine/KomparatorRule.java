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
	/**
	 * Whether we need this node during comparison. If yes, then the node ordering is considered. Else false.
	 * This overrides by the 'childrenRequired' attribute of parent.
	 */
	private boolean required;
	/**
	 * Whether the attributes need to be in order.
	 */
	private boolean needAttributesOrder;
	/**
	 * Whether all the attributes are required.
	 */
	private boolean allAttributesRequired;
	/**
	 * Whether the values for all attributes need to match.
	 */
	private boolean allAttributesMatch;
	/**
	 * Whether the children need to be ordered. This overrides 'needsORder' of child node.
	 */
	private boolean childrenOrdering;
	/**
	 * The attribute that acts as differentiator between same name nodes.
	 */
	private String differentiator;

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

	

	public boolean isRequired()
		{
		return required;
		}

	public void setRequired(boolean required)
		{
		this.required = required;
		}

	public boolean isNeedAttributesOrder()
		{
		return needAttributesOrder;
		}

	public void setNeedAttributesOrder(boolean needAttributesOrder)
		{
		this.needAttributesOrder = needAttributesOrder;
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

	

	public String getDifferentiator()
		{
		return differentiator;
		}

	public void setDifferentiator(String differentiator)
		{
		this.differentiator = differentiator;
		}

	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append("Id: ");
		builder.append(id);
		builder.append(" | Name: ");
		builder.append(name);
		builder.append(" | Required: ");
		builder.append(required);
		builder.append(" | NeedAttributeOrder: ");
		builder.append(needAttributesOrder);
		builder.append(" | AllAttributesRequired: ");
		builder.append(allAttributesRequired);
		builder.append(" | AllAttributesMatch: ");
		builder.append(allAttributesMatch);
		builder.append(" | ChildrenOrdering: ");
		builder.append(childrenOrdering);
		builder.append(" | Differentiator: ");
		builder.append(differentiator);

		return builder.toString();
		}
	}
