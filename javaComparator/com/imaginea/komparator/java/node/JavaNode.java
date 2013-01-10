package com.imaginea.komparator.java.node;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

public class JavaNode implements KomparatorNode
	{
	private String name;
	private int ruleId;
	private List<KomparatorAttribute> attributes = new ArrayList<KomparatorAttribute>();
	private List<KomparatorNode> children = new ArrayList<KomparatorNode>();
	private String differentiatorValue;
	private JavaNode parent;

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

	public JavaNode getParent()
		{
		return parent;
		}

	public void setParent(JavaNode parent)
		{
		this.parent = parent;
		}

	public int compareTo(KomparatorNode secondNode)
		{
		int nameCompare = name.compareTo(secondNode.getName());
		if (nameCompare == 0)
			{
			return differentiatorValue.compareTo(secondNode.getDifferentiatorValue());
			}
		return nameCompare;
		}
	
	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append("Name: ");
		builder.append(name);
		builder.append(" | Differentiator: ");
		builder.append(differentiatorValue);
		builder.append(" | RuleId: ");
		builder.append(ruleId);
		return builder.toString();
		}

	public String toXML()
		{
		JavaAttribute attributeToBeValue = null;
		StringBuilder builder = new StringBuilder();
		builder.append("<");
		builder.append(name);
		builder.append(" ");
		for (int attributeCounter = 0; attributeCounter < getAttributes().size(); attributeCounter++)
			{
			JavaAttribute attribute = (JavaAttribute) getAttributes().get(attributeCounter);
			if ("value".equals(attribute.getName()) && attribute.getValue().contains("\n"))
				{
				attributeToBeValue = attribute;
				}
			else
				{
				builder.append(attribute.getName());
				builder.append("=\"");
				builder.append(attribute.getValue());
				builder.append("\" ");
				}
			}

		// All the attributes have been printed. We need to see if there is any attribute that needs to come as value in tags.
		if (attributeToBeValue != null)
			{
			builder.append(">\n<attribute name=\"");
			builder.append(attributeToBeValue.getName());
			builder.append("\">\n");
			builder.append(attributeToBeValue.getValue());
			builder.append("\n</attribute>");
			}
		if ((getChildren() == null || getChildren().size() == 0) && attributeToBeValue == null)
			{
			// There are no children. Hence we can close here itself.
			builder.append("/>");
			}
		else
			{
			if (attributeToBeValue == null)
				{
				// If we did not write values in the tags, then we can close the tag. Otherwise the tag has already been closed.
				builder.append(">\n");
				}
			for (int childrenCounter = 0; childrenCounter < getChildren().size(); childrenCounter++)
				{
				JavaNode child = (JavaNode) getChildren().get(childrenCounter);
				builder.append(child.toXML());
				builder.append("\n");
				}
			builder.append("</");
			builder.append(name);
			builder.append(">");
			}
		return builder.toString();
		}
	}
