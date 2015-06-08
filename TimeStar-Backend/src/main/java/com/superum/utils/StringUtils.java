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
	
}
