package com.imaginea.komparator.xmltype1.node;

import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;

/**
 * Represents an attribute in the komparator node.
 * 
 * @author harshavardhansingh
 * 
 */
public class XMLType1Attribute implements KomparatorAttribute
	{
	/**
	 * Name of the attribute.
	 */
	private String name;
	/**
	 * Value of the attribute.
	 */
	private String value;

	public XMLType1Attribute()
		{

		}

	public XMLType1Attribute(String name, String value)
		{
		this.name = name;
		this.value = value;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getValue()
		{
		return value;
		}

	public void setValue(String value)
		{
		this.value = value;
		}

	public int compareTo(Object o)
		{
		if (!(o instanceof XMLType1Attribute))
			{
			return -1;
			}
		XMLType1Attribute secondAttr = (XMLType1Attribute) o;
		return name.compareTo(secondAttr.getName());
		}

	}
