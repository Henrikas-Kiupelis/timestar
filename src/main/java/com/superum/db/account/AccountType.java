package com.superum.db.account;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.superum.config.Role;

public enum AccountType {

	COTEM(Role.ADMIN, Role.TEACHER),
	TEACHER(Role.TEACHER);
	
	public List<Role> roles() {
		return roles;
	}
	
	public List<String> roleNames() {
		return roles.stream()
				.map(Role::fullName)
				.collect(Collectors.toList());
	}
	
	// PRIVATE
	
	private final List<Role> roles;
	
	AccountType(Role... roles) {
		this.roles = Arrays.asList(roles);
	}

}
