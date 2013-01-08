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

	public KomparatorEngine()
		{
		ruleset = KomparatorManager.getRuleset();
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
		List<Komparison> comparisons = new ArrayList<Komparison>();

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
				logger.error("There is no node in file1 for corresponding node {} in file2.", node2);
				return;
			case -3: // Node2 is null. Hence the tree is not same here.
				logger.error("There is no node in file2 for corresponding node {} in file1.", node1);
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
		if (!compareAttributes(node1, node2, rule))
			{
			logger.info("The root nodes attributes do not follow the rule.");
			}

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

		// Checking if the children exist for the nodes.
		if (CommonUtils.isListEmpty(nodes1) || CommonUtils.isListEmpty(nodes2))
			{
			// One of the lists are empty. We need to check it was only 1 list empty of both were empty.
			if (CommonUtils.isListEmpty(nodes1) && CommonUtils.isListEmpty(nodes2))
				{
				// Both the lists are empty.
				return true;
				}
			// Any one of the lists was empty - and hence no match. So return false.
			logger.error("Number of children mismatch: The node {} of file1 has {} children. The node {} of file 2 has {} children.", node1, node1.getChildren().size(), node2, node2.getChildren()
					.size());
			return false;
			}

		// Comparing children lists.
		boolean result = false;
		// Following is for readability purpose.
		if (childrenOrdering)
			{
			// We perform the comparison on children as is.
			}
		else
			{
			// We do not need ordering. So for our benefit, we perform sorting and then proceed.
			Collections.sort(nodes1);
			Collections.sort(nodes2);
			}

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
					logger.error("Mismatch: The node {} in file1 is missing in file2.", child1);
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
					logger.error("Mismatch: The node {} in file2 is missing in file1.", child2);
					}
				}
			}
		while(counter1 < nodes1.size())
			{
			KomparatorNode child1 = nodes1.get(counter1);
			KomparatorRule child1Rule = KomparatorManager.getRuleset().getRule(child1.getRuleId());
			// Perform required check.
			if (child1Rule.isRequired())
				{
				// Child 1 is required but missing in file2.
				logger.error("Mismatch: The node {} in file1 is missing in file2.", child1);
				}
			counter1++;
			}
		while(counter2 < nodes2.size())
			{
			KomparatorNode child2 = nodes2.get(counter2);
			KomparatorRule child2Rule = KomparatorManager.getRuleset().getRule(child2.getRuleId());
			// Perform required check.
			if (child2Rule.isRequired())
				{
				// Child 1 is required but missing in file2.
				logger.error("Mismatch: The node {} in file2 is missing in file1.", child2);
				}
			counter2++;			
			}
		return result;
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
	private boolean compareAttributes(KomparatorNode node1, KomparatorNode node2, KomparatorRule rule)
		{
		List<KomparatorAttribute> attributes1 = node1.getAttributes();
		List<KomparatorAttribute> attributes2 = node2.getAttributes();
		if (CommonUtils.isListEmpty(attributes1) && CommonUtils.isListEmpty(attributes2))
			{
			// Both the attribute lists are empty. Hence we do not need any comparison.
			return true;
			}

		boolean result = false;
		// Following 2 are created only for the readability purpose.
		boolean needsAttributeOrder = rule.isNeedAttributesOrder();
		boolean allAttributesRequired = rule.isAllAttributesRequired();
		if (needsAttributeOrder && allAttributesRequired)
			{
			// Ordering and all attributes are required. So perform a counter match.
			result = AttributeMatchingUtils.singleCounterMatch(attributes1, attributes2, rule.isAllAttributesMatch());
			}
		else if (needsAttributeOrder && !allAttributesRequired)
			{
			// Ordering is required, but not all the attributes are required. Perform 2 counter match.
			result = AttributeMatchingUtils.doubleCounterMatch(attributes1, attributes2, rule.isAllAttributesMatch());
			}
		else if (!needsAttributeOrder && allAttributesRequired)
			{
			// Ordering is not required, but all the attributes are required. Perform sort and then single counter match.
			Collections.sort(attributes1);
			Collections.sort(attributes2);
			result = AttributeMatchingUtils.singleCounterMatch(attributes1, attributes2, rule.isAllAttributesMatch());
			}
		else
			{
			// No ordering is required, and even all the attributes are not required. So sort and do 2 counter match only to check values.
			Collections.sort(attributes1);
			Collections.sort(attributes2);
			result = AttributeMatchingUtils.doubleCounterMatch(attributes1, attributes2, rule.isAllAttributesMatch());
			}
		return result;
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