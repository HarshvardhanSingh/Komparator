package com.imaginea.komparator.java.node;

import java.util.ArrayList;
import java.util.List;

import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.java.utils.LexemeType;

public class JavaNode implements KomparatorNode
	{
	private String name;
	private int ruleId;
	private List<KomparatorAttribute> attributes = new ArrayList<KomparatorAttribute>();
	private List<KomparatorNode> children = new ArrayList<KomparatorNode>();
	private String differentiatorValue;
	private JavaNode parent;
	private LexemeType lexemeType;
	
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

	public LexemeType getLexemeType()
		{
		return lexemeType;
		}

	public void setLexemeType(LexemeType lexemeType)
		{
		this.lexemeType = lexemeType;
		}

	public int compareTo(KomparatorNode o)
		{
		if (!(o instanceof JavaNode))
			{
			return -1;
			}
		JavaNode secondNode = (JavaNode) o;
		if (lexemeType.ordinal() < secondNode.getLexemeType().ordinal())
			{
			return -1;
			}
		else if (lexemeType.ordinal() > secondNode.getLexemeType().ordinal())
			{
			return 1;
			}
		else
			{
			// Both lexeme types are same. Hence we compare the names.
			return name.compareTo(secondNode.getName());
			}
		}
	}
