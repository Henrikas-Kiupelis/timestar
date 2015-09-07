package com.superum.db.account;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.InstantSerializer;
import com.google.common.base.MoreObjects;
import com.google.common.primitives.Chars;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Instant;
import org.jooq.Record;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.ACCOUNT;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Account {

	// PUBLIC API

	@JsonProperty("id")
	public Integer getId() {
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
	@JsonSerialize(using = InstantSerializer.class)
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    @JsonSerialize(using = InstantSerializer.class)
    public Instant getUpdatedAt() {
        return updatedAt;
    }
	
	@JsonIgnore
	public String getPassword() {
        return Chars.join("", password);
	}
    @JsonIgnore
    public char[] getPasswordArray() {
        return password;
    }
	
	@JsonIgnore
	public void erasePassword() {
        Arrays.fill(password, '?');
	}

	// CONSTRUCTORS

	@JsonCreator
	public Account(@JsonProperty("id") Integer id,
				   @JsonProperty("username") String username,
				   @JsonProperty("accountType") String accountType,
				   @JsonProperty("password") char[] password) {
		this(id, username, accountType, password, null, null);
	}

    public Account(Integer id, String username, String accountType, char[] password, Instant createdAt, Instant updatedAt) {
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

		Integer id = accountRecord.getValue(ACCOUNT.ID);
		String username = accountRecord.getValue(ACCOUNT.USERNAME);
		String accountType = accountRecord.getValue(ACCOUNT.ACCOUNT_TYPE);
		char[] password = accountRecord.getValue(ACCOUNT.PASSWORD).toCharArray();

		long createdTimestamp = accountRecord.getValue(ACCOUNT.CREATED_AT);
		Instant createdAt = new Instant(createdTimestamp);
		long updatedTimestamp = accountRecord.getValue(ACCOUNT.UPDATED_AT);
		Instant updatedAt = new Instant(updatedTimestamp);
		return new Account(id, username, accountType, password, createdAt, updatedAt);
	}

    public static Builder builder() {
        return new Builder();
    }

    public static IdStep stepBuilder() {
        return new Builder();
    }

	// PRIVATE
	
	private final Integer id;
	
	@NotNull
	private final String username;
	
	private final AccountType accountType;
	
	@NotEmpty
	private char[] password;

    private final Instant createdAt;
    private final Instant updatedAt;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Account")
                .add("Account id", id)
                .add("Username", username)
                .add("Account Type", accountType)
                .addValue("Password: [PROTECTED]")
                .add("Created at", createdAt)
                .add("Updated at", updatedAt)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(username, account.username) &&
                Objects.equals(accountType, account.accountType) &&
                Arrays.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, accountType, Arrays.asList(password));
    }

    // GENERATED

	public interface IdStep {
		UsernameStep id(Integer id);
        UsernameStep noId();
	}

	public interface UsernameStep {
		AccountTypeStep username(String username);
	}

	public interface AccountTypeStep {
		PasswordStep accountType(String accountType);
	}

	public interface PasswordStep {
        BuildStep password(char... password);
	}

	public interface BuildStep {
        BuildStep createdAt(Instant createdAt);
        BuildStep createdAt(long createdAt);
		BuildStep updatedAt(Instant updatedAt);
        BuildStep updatedAt(long updatedAt);
		Account build();
	}

	public static class Builder implements IdStep, UsernameStep, AccountTypeStep, PasswordStep, BuildStep {
		private Integer id;
		private String username;
		private String accountType;
		private char[] password;

		private Instant createdAt;
		private Instant updatedAt;

		private Builder() {}

		@Override
		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

        @Override
        public Builder noId() {
            this.id = null;
            return this;
        }

        @Override
		public Builder username(String username) {
			this.username = username;
			return this;
		}

		@Override
		public Builder accountType(String accountType) {
			this.accountType = accountType;
			return this;
		}

		@Override
		public Builder password(char... password) {
			this.password = password;
			return this;
		}

		@Override
		public Builder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

        @Override
        public Builder createdAt(long createdAt) {
            this.createdAt = new Instant(createdAt);
            return this;
        }

        @Override
		public Builder updatedAt(Instant updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

        @Override
        public Builder updatedAt(long updatedAt) {
            this.updatedAt = new Instant(updatedAt);
            return this;
        }

        @Override
		public Account build() {
			return new Account(id, username, accountType, password, createdAt, updatedAt);
		}
	}
}
