package com.superum.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {

	ADMIN,
	TEACHER;
	
	public String fullName() {
		return "ROLE_" + this.name();
	}
	
	public static List<String> allFullNames() {
		return Arrays.stream(Role.values())
				.map(Role::fullName)
				.collect(Collectors.toList());
	}
	
}
