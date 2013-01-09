package com.imaginea.komparator.java.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.komparator.java.node.JavaAttribute;
import com.imaginea.komparator.java.node.JavaNode;
import com.imaginea.komparator.java.utils.CharType;
import com.imaginea.komparator.java.utils.JavaParserUtils;
import com.imaginea.komparator.java.utils.LexemeType;
import com.imaginea.komparator.util.CommonUtils;

public class JavaFiniteStateAutomaton
	{
	BufferedReader reader;
	Logger logger = LoggerFactory.getLogger(getClass());
	JavaNode root = new JavaNode();
	JavaNode currentNode = root;

	public static void main(String args[]) throws FileNotFoundException, IOException
		{
		JavaFiniteStateAutomaton javaFiniteStateAutomaton = new JavaFiniteStateAutomaton(new FileInputStream(
				"E:\\JavaStuff\\Workspace\\WorkspaceFirst\\Komparator\\javaComparator\\com\\imaginea\\komparator\\java\\files\\File1.java"));
		}

	public JavaFiniteStateAutomaton(InputStream stream) throws IOException
		{
		logger.debug("Starting the java finite state automation.");
		reader = new BufferedReader(new InputStreamReader(stream));

		boolean isCommentMultiline = false;
		boolean isCommentSingleLine = false;
		boolean isClass = false;
		boolean isMethod = false;
		boolean isCondition = false;
		boolean isStaticBlock = false;

		CharType currChar, prevChar = CharType.SPACE;
		StringBuilder lexeme = new StringBuilder();
		int currentCharInt = 0;
		while ((currentCharInt = reader.read()) != -1)
			{
			char currentChar = (char) currentCharInt;

			currChar = JavaParserUtils.typeOfChar(currentChar);
			logger.debug("Identified the character '{}' as of type {}.", currentChar, currChar);
			switch (currChar)
				{
				case FORWARD_SLASH:
					lexeme.append(currentChar);
					if (prevChar == CharType.FORWARD_SLASH)
						{
						// We are working on a single line comment.
						isCommentSingleLine = true;
						}
					else if (prevChar == CharType.STAR && isCommentMultiline)
						{
						// We are ending a multiline comment
						isCommentMultiline = false;

						// We completed a comment.
						addLexeme(lexeme, LexemeType.COMMENT);
						}
					else
						{
						// It is a simple statement character.
						}
					break;
				case STAR:
					lexeme.append(currentChar);
					if (prevChar == CharType.FORWARD_SLASH)
						{
						// We are working on a multi line comment.
						isCommentMultiline = true;
						}
					else
						{
						// It is a simple statement character.
						lexeme.append(currentChar);
						}
					break;
				case STATEMENT_CHARACTER:
					lexeme.append(currentChar);
					break;
				case SPACE:
					lexeme.append(currentChar);
					break;
				case SEMI_COLON:
					lexeme.append(currentChar);
					// A statement ends here.
					addLexeme(lexeme, LexemeType.STATEMENT);
				case OTHER_WHITE_SPACE:
					// We omit this.
					break;
				case ROUND_BRACKET_START:
					lexeme.append(currentChar);
					// Round bracket starts. We are either making condition(if, while, for), or a method.
					if (!(isCommentMultiline || isCommentSingleLine))
						{
						String createdLexeme = lexeme.toString().trim().intern();
						if (createdLexeme == "if" || createdLexeme == "for" || createdLexeme == "while")
							{
							isCondition = true;
							}
						else
							{
							// This means a method parameters
							isMethod = true;
							}
						}
					break;
				case ROUND_BRACKET_END:
					// Round brackets end. Nothing special in this.
					lexeme.append(currentChar);
					break;
				case CURLY_BRACKET_START:
					// Curly brackets start. This could be a condition, a method or a class. Or may be just a simple block.
					if (!(isCommentMultiline || isCommentSingleLine))
						{
						if (isCondition)
							{
							// Condition.
							addLexeme(lexeme, LexemeType.CONDITION);
							break;
							}
						else if (isMethod)
							{
							addLexeme(lexeme, LexemeType.METHOD);
							break;
							}

						// Now either it is a class or a static block or a simple block.
						String createdLexeme = lexeme.toString().trim().intern();
						if (createdLexeme == "static")
							{
							isStaticBlock = true;
							addLexeme(lexeme, LexemeType.STATIC_BLOCK);
							}
						else if (createdLexeme.contains(" class "))
							{
							// This is a class.
							isClass = true;
							addLexeme(lexeme, LexemeType.CLASS);
							}
						else
							{
							logger.error("Encountered a lexical notation which we dont know about.");
							}
						}
					break;
				case CURLY_BRACKET_END:
					// A curly bracket may end for a static block, a condition, a method or a class.
					lexeme.append(currentChar);
					if (isStaticBlock)
						{
						isStaticBlock = false;
						addLexeme(lexeme, LexemeType.STATIC_BLOCK);
						}
					else if (isCondition)
						{
						isCondition = false;
						addLexeme(lexeme, LexemeType.CONDITION);
						}
					else if (isMethod)
						{
						isMethod = false;
						addLexeme(lexeme, LexemeType.METHOD);
						}
					else if (isClass)
						{
						isClass = false;
						addLexeme(lexeme, LexemeType.CLASS);
						}
					else
						{
						logger.error("Closing a curly brace we have no idea about.");
						}
					break;
				case NEW_LINE:
					if (isCommentSingleLine)
						{
						// The single line coment ends here.
						isCommentSingleLine = false;
						// Add this token to the tree as comment node.
						addLexeme(lexeme, LexemeType.COMMENT);
						}
					if (isCommentMultiline)
						{
						// It is multi line comment. So we continue without doing any changes.
						lexeme.append(currentChar);
						}
					else
						{
						// We do not know for sure if it must end.
						// addLexeme(lexeme, LexemeType.STATEMENT);
						}
					break;
				default:
					logger.error("Encountered a char we do not know about.");
				}
			prevChar = currChar;
			}
		logger.debug("The java finite state automation complete.");
		}

	private void addLexeme(StringBuilder lexeme, LexemeType lexemeType)
		{
		if ("}".equals(lexeme.toString().trim()))
			{
			// We are closing and hence must move a step up.
			currentNode = currentNode.getParent();
			}
		else
			{
			switch (lexemeType)
				{
				case CLASS:
					JavaNode classNode = NodeCreationHelper.createClassNode(lexeme.toString().trim());
					currentNode.getChildren().add(classNode);
					classNode.setParent(currentNode);
					currentNode = classNode;
					break;
				case METHOD:
					JavaNode methodNode = NodeCreationHelper.createMethodNode(lexeme.toString().trim());
					currentNode.getChildren().add(methodNode);
					methodNode.setParent(currentNode);
					currentNode = methodNode;
					break;
				case STATIC_BLOCK:
					JavaNode staticNode = NodeCreationHelper.createStaticBlockNode(lexeme.toString().trim());
					currentNode.getChildren().add(staticNode);
					staticNode.setParent(currentNode);
					currentNode = staticNode;
					break;
				case CONDITION:
					break;
				case COMMENT:
					break;
				case STATEMENT:
					break;
				default:
					break;
				}
			}
		if (lexeme.toString().trim().length() == 0)
			{
			// Not a string worth paying attention to.
			return;
			}
		logger.info("LexemeType: " + lexemeType + " | Lexeme: " + lexeme);
		lexeme.setLength(0);
		}
	}