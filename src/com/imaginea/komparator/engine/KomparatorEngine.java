package com.imaginea.komparator.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.komparator.KomparatorManager;
import com.imaginea.komparator.interfaces.nodes.KomparatorAttribute;
import com.imaginea.komparator.interfaces.nodes.KomparatorNode;
import com.imaginea.komparator.util.AttributeMatchingUtils;
import com.imaginea.komparator.util.CommonUtils;

public class KomparatorEngine
	{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private KomparisonRuleset ruleset;
	List<Komparison> comparisons;

	public KomparatorEngine()
		{
		ruleset = KomparatorManager.getRuleset();
		comparisons = new ArrayList<Komparison>();
		}

	/**
	 * Compares the document trees that have been created by their respective parsers. The comparison is done based on
	 * the ruleset provided by the comparison framework registered.
	 * 
	 * @param root1
	 *            The tree1 that needs to be compared.
	 * @param root2
	 *            The tree2 that needs to be compared.
	 * @return A list of differences as Komparison objects.
	 */
	public List<Komparison> compareDocuments(KomparatorNode root1, KomparatorNode root2)
		{
		if (root1 == null || root2 == null)
			{
			logger.error("The entered tree(s) is empty.");
			}

		// Comparison logic goes here.
		// We consider the two roots as equal.
		// If the node doesnot have a differentiator in rule, we cannot perform any node comparison on it.

		compareNodes(root1, root2);
		return comparisons;
		}

	public void compareNodes(KomparatorNode node1, KomparatorNode node2)
		{
		// First obtain the rule that is applicable to both the nodes.
		KomparatorRule rule = null;
		int ruleId = getRuleId(node1, node2);
		switch (ruleId)
			{
			case -1: // Both the nodes are null. Hence return true.
				logger.error("Both the root nodes are null.");
				return;
			case -2: // Node1 is null. Hence the tree is not same here.
				comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_NODE, null, null, node2, null));
				return;
			case -3: // Node2 is null. Hence the tree is not same here.
				comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_NODE, node1, null, null, null));
				return;
			default:
				// There is a rule.
				rule = ruleset.getRule(ruleId);
			}
		if (rule == null)
			{
			// No applicable rule could be found for the ruleId.
			logger.error("No applicable rule could be found for file1 node {} and file2 node {}", node1, node2);
			}

		// If we are here, that means we have a rule that is applicable.
		// We need to perform attribute comparison.
		// Attribute comparison for the root nodes.
		compareAttributes(node1, node2, rule);

		// Now we compare the children.
		compareChildrenNodes(node1, node2, rule.isChildrenOrdering());
		}

	/**
	 * Compares the two subtrees for given nodes.
	 * 
	 * @param node1
	 *            The node 1.
	 * @param node2
	 *            The node 2.
	 * @param childrenOrdering
	 *            Whether the parent rule says that child ordering is required.
	 * @return true if the comparisons were successful. false if the comparisons failed.
	 */
	private boolean compareChildrenNodes(KomparatorNode node1, KomparatorNode node2, boolean childrenOrdering)
		{
		List<KomparatorNode> nodes1 = node1.getChildren();
		List<KomparatorNode> nodes2 = node2.getChildren();

		// One of the lists are empty. We need to check it was only 1 list empty of both were empty.
		if (CommonUtils.isListEmpty(nodes1) && CommonUtils.isListEmpty(nodes2))
			{
			// Both the lists are empty.
			return true;
			}

		/*
		 * // Checking if the children exist for the nodes.
		 * if (CommonUtils.isListEmpty(nodes1) || CommonUtils.isListEmpty(nodes2))
		 * {
		 * // One of the lists are empty. We need to check it was only 1 list empty of both were empty.
		 * if (CommonUtils.isListEmpty(nodes1) && CommonUtils.isListEmpty(nodes2))
		 * {
		 * // Both the lists are empty.
		 * return true;
		 * }
		 * // Any one of the lists was empty - and hence no match. So return false.
		 * logger.error(
		 * "Number of children mismatch: The node {} of file1 has {} children. The node {} of file 2 has {} children.",
		 * node1, node1.getChildren().size(), node2, node2.getChildren()
		 * .size());
		 * return false;
		 * }
		 */

		// Comparing children lists.
		boolean result = false;

		// Perform an ordering test first.
		checkOrderingMismatch(nodes1, nodes2, null);

		// We do not need ordering. So for our benefit, we perform sorting and then proceed.
		Collections.sort(nodes1);
		Collections.sort(nodes2);

		// Now we proceed on a node by node basis, comparing the required node and the node match rules.
		// We use double counter matching.
		// For all the children nodes that match, we need to proceed to their children.
		int counter1 = 0;
		int counter2 = 0;
		for (; counter1 < nodes1.size() && counter2 < nodes2.size();)
			{
			KomparatorNode child1 = nodes1.get(counter1);
			KomparatorNode child2 = nodes2.get(counter2);

			KomparatorRule child1Rule = KomparatorManager.getRuleset().getRule(child1.getRuleId());
			KomparatorRule child2Rule = KomparatorManager.getRuleset().getRule(child2.getRuleId());

			int nodeCompare = compareTo(child1, child2);
			if (nodeCompare == 0)
				{
				// Both the nodes are equal or same.
				counter1++;
				counter2++;

				// Both nodes are equal. Hence we proceed on to their comparison. 
				compareNodes(child1, child2);
				}
			else if (nodeCompare < 0)
				{
				// Child 1 is less than child2
				counter1++;

				// Perform required check.
				if (child1Rule.isRequired())
					{
					// Child 1 is required but missing in file2.
					comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_NODE, child1, null, null, null));
					// logger.error("Mismatch: The node {} in file1 is missing in file2.", child1);
					}
				}
			else
				{
				// Child 2 is less than child 1
				counter2++;

				// Perform required check.
				if (child2Rule.isRequired())
					{
					// Child 2 is required but missing in file1.
					comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_NODE, null, null, child2, null));
					// logger.error("Mismatch: The node {} in file2 is missing in file1.", child2);
					}
				}
			}
		while (counter1 < nodes1.size())
			{
			KomparatorNode child1 = nodes1.get(counter1);
			KomparatorRule child1Rule = KomparatorManager.getRuleset().getRule(child1.getRuleId());
			// Perform required check.
			if (child1Rule.isRequired())
				{
				// Child 1 is required but missing in file2.
				comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_NODE, child1, null, null, null));
				// logger.error("Mismatch: The node {} in file1 is missing in file2.", child1);
				}
			counter1++;
			}
		while (counter2 < nodes2.size())
			{
			KomparatorNode child2 = nodes2.get(counter2);
			KomparatorRule child2Rule = KomparatorManager.getRuleset().getRule(child2.getRuleId());
			// Perform required check.
			if (child2Rule.isRequired())
				{
				// Child 1 is required but missing in file2.
				comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_NODE, null, null, child2, null));
				// logger.error("Mismatch: The node {} in file2 is missing in file1.", child2);
				}
			counter2++;
			}
		return result;
		}
	
	/**
	 * Checks the ordering of both attribute or the nodes.
	 * 
	 * @param <E>
	 *            The type.
	 * @param list1
	 *            The first list.
	 * @param list2
	 *            The second list.
	 * @param node
	 *            Pass it only when comparing attributes. Else, pass null when comparing nodes. This will be used for
	 *            logging which node's attributed didn't match.
	 */
	private <E extends Comparable> void checkOrderingMismatch(List<E> list1, List<E> list2, KomparatorNode node)
		{
		int counter1 = 0;
		int counter2 = 0;
		for (; counter1 < list1.size() && counter2 < list2.size();)
			{
			E e1 = list1.get(counter1);
			E e2 = list2.get(counter2);

			int comparison = e1.compareTo(e2);
			if (comparison == 0)
				{
				// We are good. Go ahead.
				counter1++;
				counter2++;
				}
			else if (comparison < 0)
				{
				// item1 is smaller. Search for it in list2.
				for (int counter3 = counter2; counter3 < list2.size(); counter3++)
					{
					E e3 = list2.get(counter3);
					if (e1.compareTo(e3) == 0)
						{
						// We found a match.
						if (node == null)
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_NODE_ORDER, (KomparatorNode) e1, null, null, null));
							// logger.error("Order mismatch: The node {} in file1 is not in same order as in file2.", e1);
							}
						else
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_ATTRIBUTE_ORDER, node, (KomparatorAttribute) e1, null, null));
							// logger.error("Order mismatch: The attribute {} if node {} in file1 is not in same order as in file2.", e1, node);
							}
						}
					}
				counter1++;
				}
			else
				{
				// item2 is smaller. Search for it in list1.
				for (int counter3 = counter2; counter3 < list1.size(); counter3++)
					{
					E e3 = list1.get(counter3);
					if (e2.compareTo(e3) == 0)
						{
						// We found a match.
						if (node == null)
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_NODE_ORDER, null, null, (KomparatorNode) e2, null));
							// logger.error("Order mismatch: The node {} in file2 is not in same order as in file1.", e2);
							}
						else
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_ATTRIBUTE_ORDER, null, null, node, (KomparatorAttribute) e2));
							// logger.error("Order mismatch: The attribute {} if node {} in file2 is not in same order as in file1.", e2, node);
							}
						}
					}
				counter2++;
				}
			}
		}

	/**
	 * Checks the ordering of both attribute or the nodes.
	 * 
	 * @param <E>
	 *            The type.
	 * @param list1
	 *            The first list.
	 * @param list2
	 *            The second list.
	 * @param node
	 *            Pass it only when comparing attributes. Else, pass null when comparing nodes. This will be used for
	 *            logging which node's attributed didn't match.
	 */
	private <E extends Comparable> void checkOrderingMismatch_old(List<E> list1, List<E> list2, KomparatorNode node)
		{
		int counter1 = 0;
		int counter2 = 0;
		for (; counter1 < list1.size() && counter2 < list2.size();)
			{
			E e1 = list1.get(counter1);
			E e2 = list2.get(counter2);

			int comparison = e1.compareTo(e2);
			if (comparison == 0)
				{
				// We are good. Go ahead.
				counter1++;
				counter2++;
				}
			else if (comparison < 0)
				{
				// item1 is smaller. Search for it in list2.
				for (int counter3 = counter2; counter3 < list2.size(); counter3++)
					{
					E e3 = list2.get(counter3);
					if (e1.compareTo(e3) == 0)
						{
						// We found a match.
						if (node == null)
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_NODE_ORDER, (KomparatorNode) e1, null, null, null));
							// logger.error("Order mismatch: The node {} in file1 is not in same order as in file2.", e1);
							}
						else
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_ATTRIBUTE_ORDER, node, (KomparatorAttribute) e1, null, null));
							// logger.error("Order mismatch: The attribute {} if node {} in file1 is not in same order as in file2.", e1, node);
							}
						}
					}
				counter1++;
				}
			else
				{
				// item2 is smaller. Search for it in list1.
				for (int counter3 = counter2; counter3 < list1.size(); counter3++)
					{
					E e3 = list1.get(counter3);
					if (e2.compareTo(e3) == 0)
						{
						// We found a match.
						if (node == null)
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_NODE_ORDER, null, null, (KomparatorNode) e2, null));
							// logger.error("Order mismatch: The node {} in file2 is not in same order as in file1.", e2);
							}
						else
							{
							comparisons.add(new Komparison(DifferenceType.IMPROPER_ATTRIBUTE_ORDER, null, null, node, (KomparatorAttribute) e2));
							// logger.error("Order mismatch: The attribute {} if node {} in file2 is not in same order as in file1.", e2, node);
							}
						}
					}
				counter2++;
				}
			}
		}

	/**
	 * It compares whether two nodes are equal or not.
	 * 
	 * @param node1
	 *            The node1.
	 * @param node2
	 *            The node2.
	 * @return
	 *         <ul>
	 *         <li>0 - Both the nodes are equal.</li>
	 *         <li>Negative - Node1 is smaller than node2.</li>
	 *         <li>Positive > 0 - Node1 is greater than node2.</li>
	 *         </ul>
	 */
	private int compareTo(KomparatorNode node1, KomparatorNode node2)
		{
		if (node1 == null && node2 == null)
			{
			return 0;
			}
		if (node1 == null)
			{
			return -1;
			}
		if (node2 == null)
			{
			return 1;
			}

		int nameCompare = node1.getName().compareTo(node2.getName());
		if (nameCompare == 0)
			{
			String diffVal1 = node1.getDifferentiatorValue();
			String diffVal2 = node2.getDifferentiatorValue();

			if (diffVal1 == null && diffVal2 == null)
				{
				// Both values are blank.
				return 0;
				}
			if (diffVal1 == null)
				{
				return -1;
				}
			if (diffVal2 == null)
				{
				return 1;
				}
			// Node names are same. Need to check differentiator
			return diffVal1.compareTo(diffVal2);
			}
		return nameCompare;
		}

	/**
	 * Performs only attribute level validation.
	 * 
	 * @param attributes1
	 *            The first attributes list.
	 * @param attributes2
	 *            The second attributes list.
	 * @param rule
	 *            The rule that needs to be applied.
	 * @return true if the attributes confirm to the rules. false if any of the rule is violated.
	 * 
	 */
	private void compareAttributes(KomparatorNode node1, KomparatorNode node2, KomparatorRule rule)
		{
		List<KomparatorAttribute> attributes1 = node1.getAttributes();
		List<KomparatorAttribute> attributes2 = node2.getAttributes();
		if (CommonUtils.isListEmpty(attributes1) && CommonUtils.isListEmpty(attributes2))
			{
			// Both the attribute lists are empty. Hence we do not need any comparison.
			return;
			}

		// Perform ordering test.
		checkOrderingMismatch(attributes1, attributes2, node1);

		// If we do not need ordering, we should arrange attributes by name.
		Collections.sort(attributes1);
		Collections.sort(attributes2);

		// We perform no null check on the attributes list because they can't be null. In worst case, they will have 0 size().
		int counter1 = 0;
		int counter2 = 0;
		for (; counter1 < attributes1.size() && counter2 < attributes2.size();)
			{
			KomparatorAttribute attrib1 = attributes1.get(counter1);
			KomparatorAttribute attrib2 = attributes2.get(counter2);

			int comparison = attrib1.compareTo(attrib2);
			if (comparison == 0)
				{
				// Both the attributes are same.
				if (rule.isAllAttributesMatch())
					{
					// The values must match
					if (!attrib1.getValue().equals(attrib2.getValue()))
						{
						// Values mismatch when the rule says they must match.
						comparisons.add(new Komparison(DifferenceType.MISMATCHED_ATTRIBUTE_VALUE, node1, attrib1, node2, attrib2));
						//						logger.error("Attribute value mismatch: For node {} in file1 and node {} in file2, the attribute {} has different values - file1 has {} and file2 has {}.", node1, node2,
						//								attrib1.getName(), attrib1.getValue(), attrib2.getValue());
						}
					}
				counter1++;
				counter2++;
				}
			else
				{
				// The values do not match
				if (comparison < 0)
					{
					// attrib1 is smaller than attrib2
					if (rule.isAllAttributesRequired())
						{
						comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_ATTRIBUTE, node1, attrib1, null, null));
						// logger.error("Attribute mismatch: For node {} and attribute {} of file1, no attribute exists in file2.", node1, attrib1.getName());
						}
					counter1++;
					}
				else
					{
					// attrib1 is greater than attrib2
					if (rule.isAllAttributesRequired())
						{
						comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_ATTRIBUTE, null, null, node2, attrib2));
						// logger.error("Attribute mismatch: For node {} and attribute {} of file2, no attribute exists in file1.", node2, attrib2.getName());
						}
					counter2++;
					}
				}
			}

		// Write logs for the remaining items in any of the list.
		if (rule.isAllAttributesRequired())
			{
			// We do this because we can only check if all attributes are required. Match can't be performed obviously.
			while (counter1 < attributes1.size())
				{
				KomparatorAttribute attrib1 = attributes1.get(counter1);
				comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_ATTRIBUTE, node1, attrib1, null, null));
				// logger.error("Attribute mismatch: For node {} and attribute {} of file1, no attribute exists in file2.", node1, attrib1.getName());
				counter1++;
				}
			while (counter2 < attributes2.size())
				{
				KomparatorAttribute attrib2 = attributes2.get(counter2);
				comparisons.add(new Komparison(DifferenceType.MISSING_REQUIRED_ATTRIBUTE, null, null, node2, attrib2));
				// logger.error("Attribute mismatch: For node {} and attribute {} of file2, no attribute exists in file1.", node2, attrib2.getName());
				counter2++;
				}
			}
		}

	/**
	 * Fetches a rule based on the two nodes passed.
	 * 
	 * @param node1
	 *            The first node.
	 * @param node2
	 *            The second node.
	 * @return The positive result should be a rule id if the same rule is applied on both the nodes. A negative result
	 *         would mean an error condition. These are as follows:
	 *         <ul>
	 *         <li>Anything &gt; 0 - the rule id that must be applied.</li>
	 *         <li>-1 - Both the nodes are null.</li>
	 *         <li>-2 - Node1 is null.</li>
	 *         <li>-3 - Node2 is null.</li>
	 *         <li>-4 - The rules in both nodes differ.</li>
	 *         </ul>
	 */
	private int getRuleId(KomparatorNode node1, KomparatorNode node2)
		{
		if (node1 == null && node2 == null)
			{
			// Any of the nodes are null. Return null as we cannot find their rule.
			return -1;
			}
		if (node1 == null)
			{
			return -2;
			}
		if (node2 == null)
			{
			return -3;
			}

		int ruleid1 = node1.getRuleId();
		int ruleid2 = node2.getRuleId();
		if (ruleid1 != ruleid2)
			{
			logger.error("There are two different rules for nodes from two files. Node in file 1 is {} and node in file 2 is {}", node1.getName(), node2.getName());
			return -4;
			}

		return ruleid1;
		}
	}