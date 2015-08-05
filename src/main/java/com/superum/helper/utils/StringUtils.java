package com.superum.helper.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @deprecated These methods can be found in other Utils classes (i.e. Guava)
 */
@Deprecated
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

	public static String fieldStrings(Object... objects) {
        if ((objects.length & 1) != 0)
            throw new IllegalArgumentException("Only field name/value pairs are allowed.");

		StringBuilder fieldBuilder = new StringBuilder().append('{');

        boolean first = true;
        for (int i = 0; i < objects.length; i += 2) {
            if (first)
                first = false;
            else
                fieldBuilder.append(", ");

            fieldBuilder.append(objects[i])
                    .append(": ")
                    .append(objects[i + 1]);
        }
        return fieldBuilder.append('}').toString();
	}

	// PRIVATE

	private StringUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
	}

}
