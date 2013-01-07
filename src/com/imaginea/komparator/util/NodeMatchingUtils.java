package com.imaginea.komparator.util;

import java.util.List;

import com.imaginea.komparator.interfaces.nodes.KomparatorNode;

public class NodeMatchingUtils
	{
	public static boolean singleCounterMatch(List<KomparatorNode> list1, List<KomparatorNode> list2)
		{
		if (list1.size() != list2.size())
			{
			return false;
			}
		for (int counter = 0; counter < list1.size(); counter++)
			{
			// Loop
			KomparatorNode attr1 = list1.get(counter);
			KomparatorNode attr2 = list2.get(counter);
			if (!attr1.getName().equals(attr2.getName()))
				{
				// The attributes at same counter do not match.
				return false;
				}
			}
		return true;
		}

	public static boolean doubleCounterMatch(List<KomparatorNode> list1, List<KomparatorNode> list2)
		{
		if (list1.size() != list2.size())
			{
			return false;
			}
		for (int counter1 = 0, counter2 = 0; counter1 < list1.size() && counter2 < list2.size();)
			{
			// Loop
			KomparatorNode attr1 = list1.get(counter1);
			KomparatorNode attr2 = list2.get(counter2);
			if (!(attr1.getName().compareTo(attr2.getName()) == 0))
				{
				// The attributes at same counter do not match.
				if (attr1.getName().compareTo(attr2.getName()) < 0)
					{
					// attr1 is smaller. So its counter moves.
					counter1++;
					}
				else
					{
					// attr2 is smaller. So its counter moves.
					counter2++;
					}
				}
			else
				{
				// Both the attributes match.
				counter1++;
				counter2++;
				}
			}
		return true;
		}
	}
