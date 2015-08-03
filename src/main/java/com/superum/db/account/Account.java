package com.superum.db.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.ObjectUtils;
import com.superum.utils.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jooq.Record;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.ACCOUNT;

@JsonIgnoreProperties(ignoreUnknown = true)
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
		return accountType == null ? null : accountType.name();
	}

	@JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
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
		return "Account" + StringUtils.toString(
				"Account ID: " + id,
				"Username: " + username,
				"Account Type: " + accountType,
				"Password: [PROTECTED]",
                "Created at: " + createdAt,
                "Updated at: " + updatedAt);
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
        return ObjectUtils.hash(id, username, accountType, password);
	}

	// CONSTRUCTORS

	@JsonCreator
	public Account(@JsonProperty("id") int id,
				   @JsonProperty("username") String username,
				   @JsonProperty("accountType") String accountType,
				   @JsonProperty("password") char[] password) {
		this(id, username, accountType, password, null, null);
	}

    public Account(int id, String username, String accountType, char[] password, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.accountType = accountType == null ? null : AccountType.valueOf(accountType);
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
	
	public static Account valueOf(Record accountRecord) {
		if (accountRecord == null)
			return null;
		
		int id = accountRecord.getValue(ACCOUNT.ID);
		String username = accountRecord.getValue(ACCOUNT.USERNAME);
		String accountType = accountRecord.getValue(ACCOUNT.ACCOUNT_TYPE);
		char[] password = accountRecord.getValue(ACCOUNT.PASSWORD).toCharArray();

		long createdTimestamp = accountRecord.getValue(ACCOUNT.CREATED_AT);
		Date createdAt = new Date(createdTimestamp);
		long updatedTimestamp = accountRecord.getValue(ACCOUNT.UPDATED_AT);
		Date updatedAt = new Date(updatedTimestamp);
		return new Account(id, username, accountType, password, createdAt, updatedAt);
	}

	// PRIVATE
	
	private final int id;
	
	@NotNull
	private final String username;
	
	private final AccountType accountType;
	
	@NotEmpty
	private char[] password;

    private final Date createdAt;
    private final Date updatedAt;
	
}
