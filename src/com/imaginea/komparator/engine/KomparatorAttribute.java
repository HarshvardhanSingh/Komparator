package com.imaginea.komparator.engine;

/**
 * Represents an attribute in the komparator node.
 * 
 * @author harshavardhansingh
 * 
 */
public class KomparatorAttribute
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

	}
