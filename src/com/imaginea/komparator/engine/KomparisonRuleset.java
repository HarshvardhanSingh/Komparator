package com.imaginea.komparator.engine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.imaginea.komparator.util.KomparatorDOMParser;

/**
 * Manager class for the comparision rule set as formulated from the rule xml file.
 * 
 * @author harshavardhansingh
 * 
 */
public class KomparisonRuleset
	{
	/**
	 * Map of rule id and rule.
	 */
	private Map<Integer, KomparatorRule> rules;
	private Logger logger;
	/**
	 * It is set to true if the ruleset is corrupted.
	 */
	private boolean isRulesetCorrupted = false;

	/**
	 * Creates the ruleset based on the passed rules xml file.
	 * 
	 * @param inputStream
	 *            The input stream from which the file shall be read.
	 */
	public KomparisonRuleset(InputStream inputStream)
		{
		rules = new HashMap<Integer, KomparatorRule>();
		logger = LoggerFactory.getLogger(this.getClass());

		Element root = KomparatorDOMParser.parseDOM(inputStream);
		if(root == null)
			{
			isRulesetCorrupted = true;
			logger.error("There was an error parsing the rule set.");
			return;
			}
		logger.debug("Root node is: {}", root.getNodeName());
		NodeList nodes = root.getChildNodes();
		for (int nodeCounter = 0; nodeCounter < nodes.getLength(); nodeCounter++)
			{
			Node node = nodes.item(nodeCounter);
			logger.debug("Reading node: {}", node.getNodeName());
			if ("rule".equals(node.getNodeName()))
				{
				KomparatorRule rule = parseRule(node); // Parses the rule.
				rules.put(rule.getId(), rule);
				}
			}
		}

	/**
	 * Parses a rule node to get the rule object.
	 * 
	 * @param ruleNode
	 *            The rule node to be parsed.
	 * @return The rule object.
	 */
	private KomparatorRule parseRule(Node ruleNode)
		{
		if ("rule".equalsIgnoreCase(ruleNode.getNodeName()))
			{
			KomparatorRule rule = new KomparatorRule();
			NamedNodeMap namedNodeMap = ruleNode.getAttributes();

			// Parse the attributes of rule to get the rule id and rule name.
			for (int attributeCounter = 0; attributeCounter < namedNodeMap.getLength(); attributeCounter++)
				{
				Node attribute = namedNodeMap.item(attributeCounter);
				logger.debug("Reading attribute: {} and value {}", attribute.getNodeName(), attribute.getNodeValue());
				if ("id".equals(attribute.getNodeName()))
					{
					rule.setId(Integer.parseInt(attribute.getNodeValue()));
					}
				else if ("name".equals(attribute.getNodeName()))
					{
					rule.setName(attribute.getNodeValue());
					}
				}
			logger.debug("Parsed rule with id {} and name {}.", rule.getId(), rule.getName());

			// Parse the children of rule to get the rule body.
			NodeList children = ruleNode.getChildNodes();
			for (int nodeCounter = 0; nodeCounter < children.getLength(); nodeCounter++)
				{
				Node childNode = children.item(nodeCounter);
				String nodeName = childNode.getNodeName().intern();
				String nodeValue = childNode.getNodeValue();
				boolean isRuleOn;
				if ("0".equals(nodeValue))
					{
					isRuleOn = false;
					}
				else
					{
					isRuleOn = true;
					}
				if ("needs-order" == nodeName)
					{
					rule.setNeedsOrder(isRuleOn);
					}
				else if ("required" == nodeName)
					{
					rule.setRequired(isRuleOn);
					}
				else if ("need-attributes-order" == nodeName)
					{
					rule.setNeedAttributesOrder(isRuleOn);
					}
				else if ("all-attributes-required" == nodeName)
					{
					rule.setAllAttributesRequired(isRuleOn);
					}
				else if ("all-attributes-match" == nodeName)
					{
					rule.setAllAttributesMatch(isRuleOn);
					}
				else if ("children-ordering" == nodeName)
					{
					rule.setChildrenOrdering(isRuleOn);
					}
				else if ("children-all-required" == nodeName)
					{
					rule.setAllAttributesRequired(isRuleOn);
					}
				}
			logger.info("Completely parsed rule: {}", rule);
			return rule;
			}
		else
			{
			return null;
			}
		}

	/**
	 * It is set to true if there has been an exception while reading the ruleset. Hence the ruleset is marked as
	 * corrupted and must not be used.
	 * 
	 * @return true if the ruleset is corrupted. false otherwise.
	 */
	public boolean isRulesetCorrupted()
		{
		return isRulesetCorrupted;
		}
	}