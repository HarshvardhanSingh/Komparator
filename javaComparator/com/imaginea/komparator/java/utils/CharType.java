package com.imaginea.komparator.java.utils;

public enum CharType
	{
	// These are used to find out comment.
	FORWARD_SLASH, STAR, 
	// These are simply used.
	STATEMENT_CHARACTER, 
	// Marks end of statement
	SEMI_COLON, 
	// For conditions like if, for, while etc
	ROUND_BRACKET_START, ROUND_BRACKET_END, 
	// For blocks of statements
	CURLY_BRACKET_START, CURLY_BRACKET_END, 
	// Simple space. Will also be neglected.
	SPACE, 
	// A new line character
	NEW_LINE,
	// Other whitespace characters to be negected.
	OTHER_WHITE_SPACE
	}
