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
import com.imaginea.komparator.util.NodeMatchingUtils;

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
	 * @param ruleset
	 *            The ruleset that needs to be used to compare the documents.
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

		// Attribute comparison for the root nodes.
		if (!compareAttributes(root1.getAttributes(), root2.getAttributes(), root1.getRuleId()))
			{
			logger.info("The root nodes attributes do not follow the rule.");
			}

		// Compare the children.
		KomparatorRule rule = KomparatorManager.getRuleset().getRule(root1.getRuleId());
		if (!compareChildrenNodes(root1.getChildren(), root2.getChildren(), rule.isChildrenOrdering(), rule.isChildrenRequired()))
			{
			logger.info("The root node's children differ the rule.");
			}

		return comparisons;
		}

	private boolean compareChildrenNodes(List<KomparatorNode> nodes1, List<KomparatorNode> nodes2, boolean childrenOrdering, boolean childrenAllRequired)
		{
		if ((nodes1 == null || nodes1.size() == 0) && (nodes2 == null || nodes2.size() == 0))
			{
			// Both the lists are empty. Hence return as true.
			return true;
			}

		KomparatorRule rule = null;
		int ruleid1 = 0;
		if (nodes1 != null && nodes1.size() > 0)
			{
			ruleid1 = nodes1.get(0).getRuleId();
			}
		else
			{
			// The node list is empty.
			return false;
			}

		int ruleid2 = 0;
		if (nodes2 != null && nodes2.size() > 0)
			{
			ruleid2 = nodes2.get(0).getRuleId();
			}
		else
			{
			// The node list is empty.
			return false;
			}

		if (ruleid1 != ruleid2)
			{
			logger.error("There are two different rules for nodes from two files. Node in file 1 is {} and node in file 2 is {}", nodes1.get(0).getName(), nodes2.get(0).getName());
			return false;
			}

		rule = ruleset.getRule(ruleid1);

		boolean result = false;
		boolean needsOrder = rule.isNeedsOrder();
		if(childrenOrdering == true)
			{
			// Because parent's children ordering takes more preference over node's needs order.
			needsOrder = true;
			}
		boolean required = rule.isRequired();
		if (needsOrder && required)
			{
			// Ordering and all attributes are required. So perform a counter match.
			result = NodeMatchingUtils.singleCounterMatch(nodes1, nodes2);
			}
		else if (needsOrder && !required)
			{
			// Ordering is required, but not all the attributes are required. Perform 2 counter match.
			result = NodeMatchingUtils.doubleCounterMatch(nodes1, nodes2);
			}
		else if (!needsOrder && required)
			{
			// Ordering is not required, but all the attributes are required. Perform sort and then single counter match.
			Collections.sort(nodes1);
			Collections.sort(nodes2);
			result = NodeMatchingUtils.singleCounterMatch(nodes1, nodes2);
			}
		else
			{
			// No ordering is required, and even all the attributes are not required. So sort and do 2 counter match only to check values.
			Collections.sort(nodes1);
			Collections.sort(nodes2);
			result = NodeMatchingUtils.doubleCounterMatch(nodes1, nodes2);
			}

		if (result == true)
			{
			// All the children nodes match. Now we need to proceed to their children.
			for (int counter1 = 0, counter2 = 0; counter1 < nodes1.size() && counter2 < nodes2.size();)
				{
				KomparatorNode node1 = nodes1.get(counter1);
				KomparatorNode node2 = nodes2.get(counter2);
				if(node1 == null || node2 == null)
					{
					// Either of the nodes is null. So there is no point in comparison anymore.
					break;
					}
				int nameCompare = node1.getName().compareTo(node2.getName());
				if (nameCompare == 0)
					{
					// Node names are same. Need to check differentiator
					if (node1.getDifferentiatorValue() != null && node2.getDifferentiatorValue() != null)
						{
						int differentiatorCompare = node1.getDifferentiatorValue().compareTo(node2.getDifferentiatorValue());
						if (differentiatorCompare == 0)
							{
							// The nodes are same.
							counter1++;
							counter2++;
							// recurse.
							compareChildrenNodes(node1.getChildren(), node2.getChildren(), rule.isChildrenOrdering(), rule.isChildrenRequired());
							}
						else if (differentiatorCompare < 0)
							{
							counter1++;
							}
						else
							{
							counter2++;
							}
						}
					else
						{
						// There is no differentiator value. So we should continue.

						// The nodes are same.
						counter1++;
						counter2++;
						// recurse.
						compareChildrenNodes(node1.getChildren(), node2.getChildren(), rule.isChildrenOrdering(), rule.isChildrenRequired());
						}
					}
				else if (nameCompare < 0)
					{
					counter1++;
					}
				else
					{
					counter2++;
					}
				}
			}
		return result;
		}

	/**
	 * Performs only attribute level validation.
	 * 
	 * @param attributes1
	 *            The first attributes list.
	 * @param attributes2
	 *            The second attributes list.
	 * @param ruleId
	 *            The rule that needs to be applied.
	 * @return true if the attributes confirm to the rules. false if any of the rule is violated.
	 * 
	 */
	private boolean compareAttributes(List<KomparatorAttribute> attributes1, List<KomparatorAttribute> attributes2, int ruleId)
		{
		if (attributes1.size() == 0 && attributes2.size() == 0)
			{
			// Both the attribute lists are empty. Hence we do not need any comparison.
			return true;
			}
		KomparatorRule rule = ruleset.getRule(ruleId);

		boolean result = false;
		if (rule.isNeedAttributesOrder() && rule.isAllAttributesRequired())
			{
			// Ordering and all attributes are required. So perform a counter match.
			result = AttributeMatchingUtils.singleCounterMatch(attributes1, attributes2, rule.isAllAttributesMatch());
			}
		else if (rule.isNeedAttributesOrder() && !rule.isAllAttributesRequired())
			{
			// Ordering is required, but not all the attributes are required. Perform 2 counter match.
			result = AttributeMatchingUtils.doubleCounterMatch(attributes1, attributes2, rule.isAllAttributesMatch());
			}
		else if (!rule.isNeedAttributesOrder() && rule.isAllAttributesRequired())
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
	}