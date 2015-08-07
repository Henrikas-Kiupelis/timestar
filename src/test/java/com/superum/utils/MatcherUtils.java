package com.superum.utils;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.either;

public class MatcherUtils {

	public static Matcher<Long> is(Long value) {
		if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
			return org.hamcrest.core.Is.is(value);
				
	    return either(org.hamcrest.core.Is.is(value)).or(org.hamcrest.core.Is.is(value.intValue()));
	}

	// PRIVATE

	private MatcherUtils() {
		throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
	}

}
