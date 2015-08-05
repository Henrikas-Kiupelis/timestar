package com.superum.helper.utils;

import java.security.SecureRandom;

/**
 * @deprecated Should be re-made into a helper class, which would use objects and not static methods
 */
@Deprecated
public class RandomUtils {

	// PUBLIC API

	public static int randomNumber(int limit) {
		return RANDOM.nextInt(limit) + 1;
	}
	
	public static long randomNumber(long limit) {
		return Math.abs(RANDOM.nextLong()) % limit + 1;
	}
	
	public static char randomChar() {
		return STRING_CHARS[RANDOM.nextInt(STRING_CHARS.length)];
	}
	
	public static String randomString(int length) {
		StringBuilder random = new StringBuilder();
		for (int i = 0; i < length; i++)
			random.append(randomChar());
		
		return random.toString();
	}
	
	public static char[] randomPassword(int length) {
		char[] pwd = new char[length];
		for (int i = 0; i < length; i++)
			pwd[i] = randomChar();
		
		return pwd;
	}

	// PRIVATE
	
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final char[] STRING_CHARS = makeChars();
	
	private static char[] makeChars() {
		StringBuilder chars = new StringBuilder();
	    for (char ch = '0'; ch <= '9'; ++ch)
	    	chars.append(ch);
	    
	    for (char ch = 'a'; ch <= 'z'; ++ch)
	    	chars.append(ch);
	    
	    for (char ch = 'A'; ch <= 'Z'; ++ch)
	    	chars.append(ch);
	    
	    return chars.toString().toCharArray();
	}

	// PRIVATE

	private RandomUtils() {
        throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
	}

}
