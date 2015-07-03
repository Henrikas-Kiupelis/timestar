package com.superum.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {

	public static String toString(Object... objects) {
		return "{" +
				Arrays.stream(objects)
					.map(String::valueOf)
					.collect(Collectors.joining("; "))
				+ "}";
	}
	
	public static String toStr(char[] chars) {
		StringBuilder builder = new StringBuilder();
		for (char c : chars)
			builder.append(c);
		
		return builder.toString();
	}
	
	public static void erase(char[] chars) {
		for (int i = 0; i < chars.length; i++)
			chars[i] = '?';
	}
	
}
