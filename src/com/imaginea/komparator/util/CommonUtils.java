package com.imaginea.komparator.util;

import java.util.List;

public class CommonUtils
	{
	public static boolean isListEmpty(List<?> list)
		{
		if (list == null || list.size() == 0)
			{
			return true;
			}
		else
			{
			return false;
			}
		}

	public static boolean isEmpty(String string)
		{
		if (string == null || string.trim().length() == 0)
			{
			return true;
			}
		else
			{
			return false;
			}
		}
	}
