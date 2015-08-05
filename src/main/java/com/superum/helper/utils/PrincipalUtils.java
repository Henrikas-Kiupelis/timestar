package com.superum.helper.utils;

import java.security.Principal;

/**
 * @deprecated Should be re-made into a helper class, which would use objects and not static methods
 */
@Deprecated
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

	// PRIVATE

	private PrincipalUtils() {
		throw new AssertionError("You should not be instantiating this class, use static methods/fields instead!");
	}

}
