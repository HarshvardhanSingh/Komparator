package com.imaginea.komparator.java.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.komparator.java.node.JavaAttribute;
import com.imaginea.komparator.java.node.JavaNode;
import com.imaginea.komparator.util.CommonUtils;

public class NodeCreationHelper
	{
	final static int staticHash = "static".hashCode();
	final static int finalHash = "final".hashCode();
	final static int publicHash = "public".hashCode();
	final static int privateHash = "private".hashCode();
	final static int protectedHash = "protected".hashCode();
	final static int classHash = "class".hashCode();
	final static int extendsHash = "extends".hashCode();
	final static int implementsHash = "implements".hashCode();

	private static Logger logger = LoggerFactory.getLogger(NodeCreationHelper.class);

	public static JavaNode createMethodNode(String statement)
		{
		if (CommonUtils.isEmpty(statement))
			{
			return null;
			}

		JavaNode javaNode = new JavaNode();
		statement = statement.trim();
		String[] tokens = statement.split(" ");
		String accessModifier = "default";

		for (int counter = 0; counter < tokens.length; counter++)
			{
			String token = tokens[counter];
			int tokenHash = token.hashCode();
			if (tokenHash == staticHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("static", "true"));
				tokens[counter] = null;
				}
			else if (tokenHash == finalHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("final", "true"));
				tokens[counter] = null;
				}
			else if (tokenHash == publicHash)
				{
				accessModifier = "public";
				tokens[counter] = null;
				}
			else if (tokenHash == protectedHash)
				{
				accessModifier = "protected";
				tokens[counter] = null;
				}
			else if (tokenHash == privateHash)
				{
				accessModifier = "private";
				tokens[counter] = null;
				}
			else if (counter == tokens.length - 1)
				{
				// This is the last token and hence the name of method.
				int indexOfRoundBracketStart = token.indexOf('(');
				if(indexOfRoundBracketStart != -1)
					{
					javaNode.getAttributes().add(new JavaAttribute("name", token.substring(0, indexOfRoundBracketStart)));
					}
				else
					{
					javaNode.getAttributes().add(new JavaAttribute("name", token));
					}				
				}
			else
				{
				logger.error("While constructing node for method, encountered a token '{}' that we do not know about.", token);
				}
			}
		int indexOfRoundBracketStart = statement.indexOf('(');
		int indexOfRoundBracketEnd = statement.indexOf(')');
		if(indexOfRoundBracketEnd == -1 || indexOfRoundBracketStart == -1)
			{
			logger.error("The method parameters couldn't be read properly. The brackets start at {} and end at {}.", indexOfRoundBracketStart, indexOfRoundBracketEnd);
			}
		else
			{
			String methodParameterString = statement.substring(indexOfRoundBracketStart, indexOfRoundBracketEnd);
			String parameters[] = methodParameterString.split(",");
			for(int paramCount = 0; paramCount < parameters.length; paramCount++)
				{
				String parameter = parameters[paramCount];
				String parameterName = parameter.split(" ")[0];
				String parameterValue = parameter.split(" ")[1];
				JavaNode paramNode = new JavaNode();
				paramNode.getAttributes().add(new JavaAttribute("paramName", parameterName));
				paramNode.getAttributes().add(new JavaAttribute("paramValue", parameterValue));
				paramNode.setParent(javaNode);
				javaNode.getChildren().add(paramNode);
				paramNode.setDifferentiatorValue(parameterValue);
				paramNode.setName("parameter");
				paramNode.setRuleId(-1);
				}
			}		

		javaNode.getAttributes().add(new JavaAttribute("access-modifier", accessModifier));
		javaNode.setDifferentiatorValue(tokens[tokens.length - 1]);
		javaNode.setName("method");
		javaNode.setRuleId(-1);
		return javaNode;
		}

	public static JavaNode createStaticBlockNode(String statement)
		{
		JavaNode node = new JavaNode();
		node.setName("static-block");
		node.setRuleId(-1);
		return node;
		}

	public static JavaNode createClassNode(String statement)
		{
		if (CommonUtils.isEmpty(statement))
			{
			return null;
			}
		if (!statement.contains(" class "))
			{
			// This is not for a class.
			return null;
			}
		JavaNode javaNode = new JavaNode();
		statement = statement.trim();
		String[] tokens = statement.split(" ");
		String accessModifier = "default";

		for (int counter = 0; counter < tokens.length; counter++)
			{
			String token = tokens[counter];
			int tokenHash = token.hashCode();
			if (tokenHash == staticHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("static", "true"));
				}
			else if (tokenHash == finalHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("final", "true"));
				}
			else if (tokenHash == publicHash)
				{
				accessModifier = "public";
				}
			else if (tokenHash == classHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("name", tokens[++counter]));
				}
			else if (tokenHash == extendsHash)
				{
				String extendsClass = tokens[++counter];
				JavaNode extendsNode = new JavaNode();
				extendsNode.getAttributes().add(new JavaAttribute("name", extendsClass));
				extendsNode.setParent(javaNode);
				javaNode.getChildren().add(extendsNode);
				extendsNode.setDifferentiatorValue(extendsClass);
				extendsNode.setName("extends");
				extendsNode.setRuleId(-1);
				}
			else if (tokenHash == implementsHash)
				{
				while (counter < tokens.length)
					{
					String implementClasses[] = token.split(",");
					for (int implementCounter = 0; implementCounter < implementClasses.length; implementCounter++)
						{
						String implementClass = implementClasses[implementCounter];
						JavaNode implementNode = new JavaNode();
						implementNode.getAttributes().add(new JavaAttribute("name", implementClass));
						implementNode.setParent(javaNode);
						javaNode.getChildren().add(implementNode);
						implementNode.setDifferentiatorValue(implementClass);
						implementNode.setName("implements");
						implementNode.setRuleId(-1);
						}
					}
				}
			else
				{
				logger.error("While creating node for a class, encountered token '{}' that we do not know about.");
				}
			}
		javaNode.getAttributes().add(new JavaAttribute("access-modifier", accessModifier));
		javaNode.setDifferentiatorValue(tokens[tokens.length - 1]);
		javaNode.setName("class");
		javaNode.setRuleId(-1);
		return javaNode;
		}
	}
