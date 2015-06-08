package com.superum.utils;

import java.util.Random;

public class RandomUtils {

	// PUBLIC API

	public static int randomNumber(int limit) {
		return random.nextInt(limit) + 1;
	}
	
	public static long randomNumber(long limit) {
		return Math.abs(random.nextLong()) % limit + 1;
	}

	// PRIVATE
	
	private static final Random random = new Random();

}
