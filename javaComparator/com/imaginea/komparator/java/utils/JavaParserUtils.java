package com.imaginea.komparator.java.utils;

public class JavaParserUtils
	{
	/**
	 * Finds out the type of character this is.
	 * 
	 * @param ch
	 *            The character.
	 * @return The character type.
	 */
	public static CharType typeOfChar(char ch)
		{
		switch (ch)
			{
			case '/':
				return CharType.FORWARD_SLASH;
			case '*':
				return CharType.STAR;
			case ';':
				return CharType.SEMI_COLON;
			case '(':
				return CharType.ROUND_BRACKET_START;
			case ')':
				return CharType.ROUND_BRACKET_END;
			case '{':
				return CharType.CURLY_BRACKET_START;
			case '}':
				return CharType.CURLY_BRACKET_END;
			case ' ':
				return CharType.SPACE;
			case '\n':
				return CharType.NEW_LINE;
			default:
				if (isWhitespaceChar(ch))
					{
					return CharType.OTHER_WHITE_SPACE;
					}
				else
					{
					return CharType.STATEMENT_CHARACTER;
					}
			}

		}

	/**
	 * Checks whether a character is a whitespace character or not.
	 * 
	 * @param ch
	 *            The character to be tested.
	 * @return true if the character is a whitespace. false otherwise.
	 */
	public static boolean isWhitespaceChar(char ch)
		{
		if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r')
			{
			return true;
			}
		return false;
		}
	}
