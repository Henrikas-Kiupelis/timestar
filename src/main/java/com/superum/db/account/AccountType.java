package com.superum.db.account;

import com.superum.config.SecurityConfig.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AccountType {

	ADMIN(Role.ADMIN, Role.TEACHER),
	TEACHER(Role.TEACHER);
	
	public List<Role> roles() {
		return roles;
	}
	
	public List<String> roleNames() {
		return roles.stream()
				.map(Role::fullName)
				.collect(Collectors.toList());
	}

	public static Set<String> names() {
        return Stream.of(AccountType.values()).map(AccountType::name).collect(Collectors.toSet());
    }
	
	// PRIVATE
	
	private final List<Role> roles;
	
	AccountType(Role... roles) {
		this.roles = Arrays.asList(roles);
	}

}
