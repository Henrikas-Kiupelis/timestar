package com.superum.db.account.roles;

import java.util.List;
import java.util.Objects;

import com.superum.utils.StringUtils;

public class AccountRoles {

	// PUBLIC API

	public String getUsername() {
		return username;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Username: " + username,
				"Roles: " + roles);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof AccountRoles))
			return false;

		AccountRoles other = (AccountRoles) o;

		return Objects.equals(this.username, other.username)
				&& Objects.equals(this.roles, other.roles);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (username == null ? 0 : username.hashCode());
		result = (result << 5) - result + (roles == null ? 0 : roles.hashCode());
		return result;
	}

	// CONSTRUCTORS

	public AccountRoles(String username, List<String> roles) {
		this.username = username;
		this.roles = roles;
	}

	// PRIVATE
	
	private final String username;
	private final List<String> roles;

}
