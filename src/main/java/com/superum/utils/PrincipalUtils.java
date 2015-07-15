package com.superum.utils;

import java.security.Principal;

public class PrincipalUtils {

	public static final char SEPARATOR = '.';
	public static final String SEPARATOR_STR = String.valueOf(SEPARATOR);
	
	public static String makeName(String name, int partitionId) {
		return partitionId + SEPARATOR_STR + name;
	}
	
	public static int partitionId(Principal principal) {
		return Integer.parseInt(principal.getName().substring(0, separationIndex(principal)));
	}
	
	public static String username(Principal principal) {
		return principal.getName().substring(separationIndex(principal) + 1);
	}
	
	// PRIVATE
	
	private static int separationIndex(Principal principal) {
		return separationIndex(principal.getName());
	}
	
	private static int separationIndex(String name) {
		return name.indexOf(SEPARATOR);
	}

}
