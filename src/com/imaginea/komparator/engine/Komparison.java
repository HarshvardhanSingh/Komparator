package com.imaginea.komparator.engine;

import java.util.List;

import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

public class Komparison
	{
	private KomparatorNode node1;
	private KomparatorNode node2;
	// These are the attributes that differ.
	private List<String> differences;

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

	public List<String> getDifferences()
		{
		return differences;
		}

	public void setDifferences(List<String> differences)
		{
		this.differences = differences;
		}

	}
