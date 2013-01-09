package com.imaginea.komparator.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CharacterReader
	{
	BufferedReader reader;
	String line;
	String newLine;
	int counter;
	public CharacterReader(InputStream stream)
		{
		 reader = new BufferedReader(new InputStreamReader(stream));
		}
	
	public char getNextChar()
		{
		return ' ';
		}
	
	public boolean hasNext() throws IOException
		{
		if(counter < line.length())
			{
			return true;
			}
		else
			{
			// Current line has ended. We need to read next line.
			readNextLine();
			if(newLine == null)
				{
				// The next line has been read and is still null. So the file has ended. And we do not have any more characters.
				return false;
				}
			}
		return true;
		}
	
	private void readNextLine() throws IOException
		{
		counter = 0;
		newLine = reader.readLine();
		}
	}
