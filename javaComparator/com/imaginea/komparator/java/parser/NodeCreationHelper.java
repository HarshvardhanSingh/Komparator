package com.imaginea.komparator.java.parser;

import java.io.File;

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
	final static int abstractHash = "abstract".hashCode();

	private static Logger logger = LoggerFactory.getLogger(NodeCreationHelper.class);

	public static JavaNode createRootNode(File file)
		{
		JavaNode root = new JavaNode();
		root.setName("javafile");
		root.setParent(null);
//		root.getAttributes().add(new JavaAttribute("file-path", file.getAbsolutePath()));
//		root.setDifferentiatorValue(file.getAbsolutePath());
		root.setRuleId(1);
		return root;
		}

	public static JavaNode createMethodNode(String statement)
		{
		boolean isMethodReturnTypeCounted = false;

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
			else if (tokenHash == abstractHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("abstract", "true"));
				tokens[counter] = null;
				}
			else
				{
				// Else we have method return type, name and parameter list.
				if (!isMethodReturnTypeCounted)
					{
					// This means it is a method return type as the return type always preceeds the name.
					isMethodReturnTypeCounted = true;
					javaNode.getAttributes().add(new JavaAttribute("return-type", token));
					tokens[counter] = null;
					continue;
					}
				else
					{
					// We have a name and a parameter list. Calculating the name.
					int indexOfRoundBracketStart = token.indexOf('(');
					if (indexOfRoundBracketStart == -1)
						{
						javaNode.getAttributes().add(new JavaAttribute("name", token));
						}
					else
						{
						javaNode.getAttributes().add(new JavaAttribute("name", token.substring(0, indexOfRoundBracketStart)));
						}
					// We have extracted the name. So we need to break through the loop so that we can go ahead for parameters.
					break;
					}
				}
			}

		// Calculating parameters now directly through the statement that was passed to us.
		int indexOfRoundBracketStart = statement.indexOf('(');
		int indexOfRoundBracketEnd = statement.indexOf(')');
		if (indexOfRoundBracketEnd == -1 || indexOfRoundBracketStart == -1)
			{
			logger.error("The method parameters couldn't be read properly. The brackets start at {} and end at {}.", indexOfRoundBracketStart, indexOfRoundBracketEnd);
			}
		else
			{
			String methodParameterString = statement.substring(indexOfRoundBracketStart, indexOfRoundBracketEnd);
			String parameters[] = methodParameterString.split(",");
			for (int paramCount = 0; paramCount < parameters.length; paramCount++)
				{
				String parameter = parameters[paramCount];
				String parameterName = parameter.split(" ")[0].replace("(", "");
				String parameterValue = parameter.split(" ")[1];
				JavaNode paramNode = new JavaNode();
				paramNode.getAttributes().add(new JavaAttribute("paramType", parameterName));
				paramNode.getAttributes().add(new JavaAttribute("paramValue", parameterValue));
				paramNode.setParent(javaNode);
				javaNode.getChildren().add(paramNode);
				paramNode.setDifferentiatorValue(parameterValue);
				paramNode.setName("parameter");
				paramNode.setRuleId(6);
				}
			}

		javaNode.getAttributes().add(new JavaAttribute("access-modifier", accessModifier));
		javaNode.getAttributes().add(new JavaAttribute("signature", statement));
		javaNode.setDifferentiatorValue(statement);
		javaNode.setName("method");
		javaNode.setRuleId(5);
		return javaNode;
		}

	public static JavaNode createStaticBlockNode(String statement)
		{
		JavaNode node = new JavaNode();
		node.getAttributes().add(new JavaAttribute("block-type", "static"));
		node.setDifferentiatorValue("static");
		node.setName("static-block");
		node.setRuleId(7);
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
			else if (tokenHash == abstractHash)
				{
				javaNode.getAttributes().add(new JavaAttribute("abstract", "true"));
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
				extendsNode.setRuleId(8);
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
						implementNode.setRuleId(9);
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
		javaNode.setRuleId(3);
		return javaNode;
		}

	public static JavaNode createConditionNode(String statement)
		{
		if (CommonUtils.isEmpty(statement))
			{
			return null;
			}

		JavaNode javaNode = new JavaNode();
		int indexOfRoundBracketStart = statement.indexOf('(');
		int indexOfRoundBracketEnd = statement.indexOf(')');
		String conditionName = statement.substring(0, indexOfRoundBracketStart);
		String condition = statement.substring(indexOfRoundBracketStart, indexOfRoundBracketEnd);
		String differentiatorValue = condition + conditionName;
		javaNode.setName("condition");
		javaNode.getAttributes().add(new JavaAttribute("type", conditionName));
		javaNode.getAttributes().add(new JavaAttribute("condition", condition));
		javaNode.getAttributes().add(new JavaAttribute("differentiator", differentiatorValue));
		javaNode.setDifferentiatorValue(differentiatorValue);
		javaNode.setRuleId(10);
		return javaNode;
		}

	public static JavaNode createCommentNode(String statement)
		{
		JavaNode javaNode = new JavaNode();
		javaNode.setName("coment");
		if (statement.startsWith("/*"))
			{
			// Multiline coment.
			javaNode.getAttributes().add(new JavaAttribute("type", "multiline"));
			}
		else if (statement.startsWith("//"))
			{
			// Single line comment.
			javaNode.getAttributes().add(new JavaAttribute("type", "singleline"));
			}
		else
			{
			// It is not a coment.
			logger.error("Encountered a coment whose type we are not able to identify. The comment is {}", statement);
			}
		javaNode.getAttributes().add(new JavaAttribute("value", statement));
		javaNode.setDifferentiatorValue(statement);
		javaNode.setRuleId(4);
		return javaNode;
		}

	public static JavaNode createStatementNode(String statement)
		{
		JavaNode javaNode = new JavaNode();
		javaNode.setName("statement");
		javaNode.getAttributes().add(new JavaAttribute("value", statement));
		javaNode.setDifferentiatorValue(statement);
		javaNode.setRuleId(2);
		return javaNode;
		}

	}
