package com.imaginea.komparator.java.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.imaginea.komparator.java.utils.CharacterReader;

public class JavaTokenizer
	{
	public static String[] getTokens(InputStream stream) throws IOException
		{
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		boolean isStatement = false;
		boolean isComment = false;
		boolean isNewConstruct = true;
		
		String line = null;
		while(true)
			{
			if(isNewConstruct)
				{
				// We are starting a new construct.
				
				// We need to check if we have a whitespace or a character.
				}
			}

		}
	}
