package com.imaginea.komparator.java.node;

import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;

public class JavaAttribute implements KomparatorAttribute
	{
	/**
	 * Name of the attribute.
	 */
	private String name;
	/**
	 * Value of the attribute.
	 */
	private String value;

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

	public int compareTo(KomparatorAttribute o)
		{
		
		return 0;
		}
	}
