package com.superum.db.account;

import static com.superum.db.generated.timestar.Tables.ACCOUNT;

import java.util.Arrays;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

public class Account {

	// PUBLIC API

	@JsonProperty("id")
	public int getId() {
		return id;
	}
	
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}
	
	@JsonProperty("accountType")
	public String getAccountType() {
		return accountType.name();
	}
	
	@JsonIgnore
	public String getPassword() {
		return StringUtils.toStr(password);
	}
	
	@JsonIgnore
	public void erasePassword() {
		StringUtils.erase(password);
		password = null;
	}
	
	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
				"Account ID: " + id,
				"Username: " + username,
				"Account Type: " + accountType,
				"Password: [PROTECTED]");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Account))
			return false;

		Account other = (Account) o;

		return this.id == other.id
				&& Objects.equals(this.username, other.username)
				&& Objects.equals(this.accountType, other.accountType)
				&& Arrays.equals(this.password, other.password);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + (username == null ? 0 : username.hashCode());
		result = (result << 5) - result + (accountType == null ? 0 : accountType.hashCode());
		result = (result << 5) - result + (password == null ? 0 : Arrays.hashCode(password));
		return result;
	}

	// CONSTRUCTORS

	@JsonCreator
	public Account(@JsonProperty("id") int id,
				   @JsonProperty("username") String username,
				   @JsonProperty("accountType") String accountType,
				   @JsonProperty("password") char[] password) {
		this.id = id;
		this.username = username;
		this.accountType = AccountType.valueOf(accountType);
		this.password = password;
	}
	
	public static Account valueOf(Record accountRecord) {
		if (accountRecord == null)
			return null;
		
		int id = accountRecord.getValue(ACCOUNT.ID);
		String username = accountRecord.getValue(ACCOUNT.USERNAME);
		String accountType = accountRecord.getValue(ACCOUNT.ACCOUNT_TYPE);
		char[] password = accountRecord.getValue(ACCOUNT.PASSWORD).toCharArray();
		return new Account(id, username, accountType, password);
	}

	// PRIVATE
	
	private final int id;
	
	@NotNull
	private final String username;
	
	private final AccountType accountType;
	
	@NotEmpty
	private char[] password;
	
}
