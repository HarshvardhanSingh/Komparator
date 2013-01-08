package com.imaginea.komparator.engine;

import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

enum DifferenceType
	{
	MISSING_REQUIRED_NODE, IMPROPER_NODE_ORDER, MISSING_REQUIRED_ATTRIBUTE, MISMATCHED_ATTRIBUTE_VALUE, IMPROPER_ATTRIBUTE_ORDER
	}

public class Komparison
	{
	private KomparatorNode node1;
	private KomparatorNode node2;
	private KomparatorAttribute attr1;
	private KomparatorAttribute attr2;
	private DifferenceType differenceType;

	public Komparison(DifferenceType differenceType, KomparatorNode node1, KomparatorAttribute attr1, KomparatorNode node2, KomparatorAttribute attr2)
		{
		this.differenceType = differenceType;
		this.node1 = node1;
		this.node2 = node2;
		this.attr1 = attr1;
		this.attr2 = attr2;
		}

	public KomparatorNode getNode1()
		{
		return node1;
		}

	public void setNode1(KomparatorNode node1)
		{
		this.node1 = node1;
		}

	public KomparatorNode getNode2()
		{
		return node2;
		}

	public void setNode2(KomparatorNode node2)
		{
		this.node2 = node2;
		}

	public KomparatorAttribute getAttr1()
		{
		return attr1;
		}

	public void setAttr1(KomparatorAttribute attr1)
		{
		this.attr1 = attr1;
		}

	public KomparatorAttribute getAttr2()
		{
		return attr2;
		}

	public void setAttr2(KomparatorAttribute attr2)
		{
		this.attr2 = attr2;
		}

	public DifferenceType getDifferenceType()
		{
		return differenceType;
		}

	public void setDifferenceType(DifferenceType differenceType)
		{
		this.differenceType = differenceType;
		}

	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append("ERROR - ");
		builder.append(differenceType);
		builder.append(" - ");
		switch (differenceType)
			{
			case MISSING_REQUIRED_NODE:
				if (node1 == null)
					{
					builder.append("There is no node in file1 for corresponding node ");
					builder.append(node2);
					builder.append(" in file2.");
					}
				else
					{
					builder.append("There is no node in file2 for corresponding node ");
					builder.append(node1);
					builder.append(" in file1.");
					}
				break;
			case IMPROPER_NODE_ORDER:
				if (node1 == null)
					{
					builder.append("The node ");
					builder.append(node2);
					builder.append(" in file2 has a different order than the node in file1.");
					}
				else
					{
					builder.append("The node ");
					builder.append(node1);
					builder.append(" in file1 has a different order than the node in file2.");
					}
				break;
			case IMPROPER_ATTRIBUTE_ORDER:
				if (node1 == null)
					{
					builder.append("The node ");
					builder.append(node2);
					builder.append(" has an attribute ");
					builder.append(attr2.getName());
					builder.append(" in file2 has a different order than the node in file1.");
					}
				else
					{
					builder.append("The node ");
					builder.append(node1);
					builder.append(" has an attribute ");
					builder.append(attr1.getName());
					builder.append(" in file1 has a different order than the node in file2.");
					}
				break;
			case MISMATCHED_ATTRIBUTE_VALUE:
				builder.append("The node ");
				builder.append(node1);
				builder.append(" with attribute ");
				builder.append(attr1.getName());
				builder.append(" has value ");
				builder.append(attr1.getValue());
				builder.append(". Where as the node ");
				builder.append(node2);
				builder.append(" with attribute ");
				builder.append(attr2.getName());
				builder.append(" has value ");
				builder.append(attr2.getValue());
				break;
			case MISSING_REQUIRED_ATTRIBUTE:
				if (node1 == null)
					{
					builder.append("The node ");
					builder.append(node2);
					builder.append(" with attribute ");
					builder.append(attr2.getName());
					builder.append(" in file2 is missing from file1 ");
					}
				else
					{
					builder.append("The node ");
					builder.append(node1);
					builder.append(" with attribute ");
					builder.append(attr1.getName());
					builder.append(" in file1 is missing from file2 ");
					}
				break;
			default:
				builder.append("Unable to fetch the error");
			}
		return builder.toString();
		}

	}